/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilerias;

import java.io.IOException;
import java.sql.ResultSetMetaData;
import interfacebd.InterfaceBD;
import interfacebd.InterfaceRegistrosBD;
import interfaz.CrearClases;
import interfaz.CrearSecuencias;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ing. Luis Manuel Navarro R.
 * Coordinación de Desarrollo de Sistemas
 * Direccion de Análisis y Tecnologías de Información
 * 
 */
public class Procedimientos implements Runnable {

    ArrayList<String> data;
    String proc = "clase";
    String scriptsFileString = "";

    public InterfaceBD getBd() {
        return bd;
    }

    public void setBd(InterfaceBD bd) {
        this.bd = bd;
    }
    InterfaceBD bd;

    public String getProc() {
        return proc;
    }

    public void setProc(String proc) {
        this.proc = proc;
    }

    public Procedimientos(ArrayList<String> data) {
        this.data = data;
    }

    public void run() {

        if (proc.equals("clase")) {
            int porcentaje = 0;
            CrearClases.pbBarra.setMaximum(100);
            CrearClases.pbBarra.setMinimum(0);
            CrearClases.pbBarra.setVisible(true);
            CrearClases.pbBarra.setValue(porcentaje);
            for (String tablaActual : data) {
                new ProcesaTabla(tablaActual);
                porcentaje += 100 / data.size();
                CrearClases.pbBarra.setValue(porcentaje);
            }
            CrearClases.pbBarra.setValue(100);
        } else if (proc.equals("jsp")) {
            Iterator it = VariablesGlobales.getTablas().iterator();
            if (it.hasNext()) {
                new ProcesaJSP((String) it.next(), data);
            }
        } else if (proc.equals("secuencias")) {
            int porcentaje = 0;

            CrearSecuencias.pbBarra.setMaximum(100);
            CrearSecuencias.pbBarra.setMinimum(0);
            CrearSecuencias.pbBarra.setVisible(true);
            CrearSecuencias.pbBarra.setValue(porcentaje);
            for (String tablaActual : data) {
                crearSecuencia(tablaActual);
                porcentaje += 100 / data.size();
                CrearSecuencias.pbBarra.setValue(porcentaje);
            }

            if (VariablesGlobales.getRutaDestino() != null && !VariablesGlobales.getRutaDestino().trim().equals("")) {
                crearArchivoSecuencias();
            }

            CrearSecuencias.pbBarra.setValue(100);
        }

        if (proc.equals("clases") || proc.equals("jsp") || (proc.equals("secuencias") && VariablesGlobales.getRutaDestino() != null && !VariablesGlobales.getRutaDestino().trim().equals(""))) {
            int resp = JOptionPane.showConfirmDialog(null, "Los archivos fueron creados exitosamente.\n¿Deseas abrir la carpeta para ver los archivos?", "Proceso finalizado", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                Process pr;
                String comando = "explorer " + VariablesGlobales.getRutaDestino().replace("\\\\", "\\");
                try {
                    pr = Runtime.getRuntime().exec(comando);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (proc.equals("secuencias") && (VariablesGlobales.getRutaDestino() == null || VariablesGlobales.getRutaDestino().trim().equals(""))) {
            JOptionPane.showMessageDialog(null, "Se han creado las secuencias.", "Proceso terminado.", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void crearSecuencia(String tablaActual) {
        String borrar = "DROP SEQUENCE " + tablaActual + "_SEC";
        String script = "CREATE SEQUENCE " + tablaActual + "_SEC INCREMENT BY 1 START WITH *** NOMAXVALUE MINVALUE 1 NOCYCLE NOCACHE ORDER";

        if (VariablesGlobales.getTipoConsecutivo().equals("IniciaEn")) {
            script = script.replace("***", "" + VariablesGlobales.getNumConsecutivo());
        } else if (VariablesGlobales.getTipoConsecutivo().equals("OnDeFlai")) {
            InterfaceRegistrosBD registros = new InterfaceRegistrosBD();
            int siguiente = registros.getSiguienteSecuencia(tablaActual, getIDTabla(tablaActual), "1=1");
            siguiente += new Integer(VariablesGlobales.getNumConsecutivo());
            script = script.replace("***", "" + siguiente);
        }

        scriptsFileString += "\n" + borrar + ";";
        scriptsFileString += "\n" + script + ";";
        bd.bdEjesql(borrar);
        bd.bdEjesql(script);
    }

    private String getIDTabla(String tabla) {
        ResultSetMetaData rsmd;
        String id = null;
        Statement SentenciaSQL = null;
        String consulta = "SELECT * FROM " + tabla + " WHERE ROWNUM <= 2";
        ResultSet resultado = null;
        try {
            SentenciaSQL =
                    InterfaceBD.getConexion().createStatement(
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);

            resultado = SentenciaSQL.executeQuery(consulta);
            rsmd = resultado.getMetaData();
            id = rsmd.getColumnName(1);
        } catch (Exception e) {
            System.out.println("No se pudo verificar el id de la tabla " + tabla);
        } finally {
            try {
                resultado.close();
                SentenciaSQL.close();
            } catch (SQLException ex) {
            }
        }
        return id;
    }

    private void crearArchivoSecuencias() {

        File scriptFile = null;
        PrintWriter pw = null;
        scriptFile = new File(VariablesGlobales.getRutaDestino() + "\\SecuenciasSQL.txt");
        if (scriptFile.exists()) {
            scriptFile.delete();
        }
        try {
            scriptFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Procedimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter fichero = null;
        try {
            fichero = new FileWriter(scriptFile);
        } catch (IOException ex) {
            Logger.getLogger(Procedimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw = new PrintWriter(fichero);
        pw.println(scriptsFileString);
        try {
            fichero.close();
        } catch (IOException ex) {
            Logger.getLogger(Procedimientos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
