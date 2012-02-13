/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manejadores;

import basedatos.Tabla;
import interfacebd.InterfaceBD;
import interfaz.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import oracle.jdbc.OracleDatabaseMetaData;
import utilerias.VariablesGlobales;

/**
 *
 * @author zeus
 */
public class mnjListaCampos {
    InterfaceBD bd;
    ListaCampos lista;
    OracleDatabaseMetaData dbmd;
    static Object[][] datosTabla;

    public mnjListaCampos(InterfaceBD bd) {
        this.bd = bd;
        inicializarDatosTabla();
        new ListaCampos(this);
    }

    public void reset(){
        bd.bdCierra();
    }

    public void inicializarDatosTabla(){
        ResultSet wrkResultSet = null;
        ResultSetMetaData md = null;
        Tabla wrkTabla = null;
        Class clase;
        String strSQL = "";
        ArrayList<String> lista = VariablesGlobales.getTablas();


        Iterator<String> it = lista.iterator();

        if(it.hasNext()){
            String tabla = it.next();

            strSQL = "SELECT *";
            strSQL = strSQL + " FROM " + tabla ;
            strSQL = strSQL + " WHERE NOT 1=1";

            System.out.println(strSQL);
            try
            {
                Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);                

                wrkResultSet = SentenciaSQL.executeQuery(strSQL);
                md = wrkResultSet.getMetaData();
            }
            catch(SQLException e){
                System.out.println("m:" + e.getMessage());
                System.out.println("e:" + e);
            }
            try {                
                datosTabla = new Object[md.getColumnCount()][6];
                for (int i = 0; i < md.getColumnCount(); i++) {
                    String campo = md.getColumnName(i+1);
                    System.out.println("Campo "+(i+1)+": "+campo);
                    datosTabla[i][0] = new Boolean(false);
                    datosTabla[i][1] = new Boolean(false);
                    datosTabla[i][2] = campo;
                    datosTabla[i][3] = new String("");
                    datosTabla[i][4] = new String("");
                    datosTabla[i][5] = new String("");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Object[][] getDatosTabla() {
        return datosTabla;
    }

    public static void setDatosTabla(Object[][] datosTabla) {
        mnjListaCampos.datosTabla = datosTabla;
    }

    public void siguiente() {
        new CrearJSP(bd);
    }

    public InterfaceBD getBd() {
        return bd;
    }

    public void setBd(InterfaceBD bd) {
        this.bd = bd;
    }
    

}
