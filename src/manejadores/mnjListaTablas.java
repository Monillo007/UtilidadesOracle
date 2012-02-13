/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manejadores;

import oracle.jdbc.driver.OracleDatabaseMetaData;
import interfacebd.InterfaceBD;
import interfaz.CrearClases;
import interfaz.ListaTablas;
import interfaz.SeleccionProceso;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import utilerias.VariablesGlobales;

/**
 *
 * @author zeus
 */
public class mnjListaTablas {
    InterfaceBD bd;
    ListaTablas lista;
    OracleDatabaseMetaData dbmd;
    static Object[][] datosTabla;


    public mnjListaTablas(InterfaceBD bd) {
        this.bd = bd;
        inicializarDatosTabla();
        lista = new ListaTablas(this);
    }

    public void reset(){
        bd.bdCierra();
    }

    public void inicializarDatosTabla(){
        String nombreTablas = "%";
        String tipos[] = new String[]{"TABLE","VIEW"};
        ResultSet tablas;
        
        ArrayList<String> datos = new ArrayList();
        try {
            dbmd = (OracleDatabaseMetaData)InterfaceBD.getConexion().getMetaData();
            tablas = dbmd.getTables(null, VariablesGlobales.getEsquema(), nombreTablas, tipos);            
            int cont = tablas.getRow();                        
            while(tablas.next()){
                datos.add(tablas.getString(tablas.findColumn("TABLE_NAME")));
            }
            datosTabla = new Object[datos.size()][2];
            int x = 0;
            for (Iterator<String> it = datos.iterator(); it.hasNext();) {                
                String string = it.next();                
                datosTabla[x][0] = new Boolean(false);
                datosTabla[x][1] = string;
                x++;
            }            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Object[][] getDatosTabla() {
        return datosTabla;
    }

    public static void setDatosTabla(Object[][] datosTabla) {
        mnjListaTablas.datosTabla = datosTabla;
    }

    public void siguiente() {
        new SeleccionProceso(bd);
    }
}
