/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package interfacebd;

import basedatos.Tabla;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;

/**
 *
 * @author Ing. Luis Navarro
 */
public class InterfaceRegistrosBD {
    InterfaceBD objBD = new InterfaceBD();
    
    public InterfaceRegistrosBD(){
        
    }
    
    public Tabla getRegistro(String p_tabla, String p_condicion){
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        ResultSet wrkResultSet = null;
        ResultSetMetaData md;
        Tabla wrkTabla = null;
        Class clase;
        String wrkcampos[];
        
        strSQL = "SELECT *";
        strSQL = strSQL + " FROM " + nombreTabla[nombreTabla.length-1] ;
        strSQL = strSQL + " WHERE " + p_condicion;
        
        System.out.println(strSQL);
        try
        {
            Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            //SentenciaSQL.setQueryTimeout(1);
            
            wrkResultSet = SentenciaSQL.executeQuery(strSQL);
            md = wrkResultSet.getMetaData();
            if(!wrkResultSet.next()){
             return null;   
            }
        }
        catch(SQLException e){
            System.out.println("m:" + e.getMessage());
            System.out.println("e:" + e);
            return null;
        }
        try {
            clase = Class.forName(p_tabla);
            wrkTabla = (Tabla) clase.newInstance();
            wrkcampos = wrkTabla.getCampos();
            
            for (String string : wrkcampos) {
                System.out.println(string);               
            }
            
            for(int i = 1; i <= wrkcampos.length; i++){
                wrkTabla.setTamano((i - 1), md.getPrecision(i));
                if (wrkResultSet.getObject(i) instanceof String){
                    String cadena;
                    cadena = wrkResultSet.getString(i).trim();
                    wrkTabla.setCampo(wrkcampos[i-1], (Object) cadena);
                }else{
                    if(md.getColumnTypeName(i).equals("LONG RAW")){
                        wrkTabla.setCampo(wrkcampos[i-1], null);
                    }else{wrkTabla.setCampo(wrkcampos[i-1], wrkResultSet.getObject(i));}
                }
            }
            //wrkResultSet.close();
        } catch (Exception ex) {
            Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (wrkTabla);
    }

    public static Object getCampoBD(String tabla, String condicion, String campo){
        String strSQL;
        String [] nombreTabla = tabla.split("\\.");
        ResultSet wrkResultSet = null;

        strSQL = "SELECT "+ campo;
        strSQL = strSQL + " FROM " + nombreTabla[nombreTabla.length-1] ;
        strSQL = strSQL + " WHERE " + condicion;

        System.out.println(strSQL);
        try
        {
            Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            SentenciaSQL.setQueryTimeout(1);
            wrkResultSet = SentenciaSQL.executeQuery(strSQL);
            if(!wrkResultSet.next()){
                return null;
            }else{
                return wrkResultSet.getObject(campo);
            }
        }
        catch(SQLException e){
            System.out.println("m:" + e.getMessage());
            System.out.println("e:" + e);
            return null;
        }
    }
    
    public Tabla getRegistroLock(String p_tabla, String p_condicion){
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        ResultSet wrkResultSet = null;
        ResultSetMetaData md;
        Tabla wrkTabla = null;
        Class clase;
        String wrkcampos[];
        
        strSQL = "SELECT *";
        strSQL = strSQL + " FROM " + nombreTabla[nombreTabla.length-1] ;
        strSQL = strSQL + " WHERE " + p_condicion;
        strSQL = strSQL + " FOR UPDATE ";

        
        System.out.println(strSQL);
        try
        {
            InterfaceBD.getConexion().commit();
            
            Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE); 
            SentenciaSQL.setQueryTimeout(1);
            
            wrkResultSet = SentenciaSQL.executeQuery(strSQL);
            md = wrkResultSet.getMetaData();
            wrkResultSet.next();
        }
        catch(SQLException e){
            System.out.println("m:" + e.getMessage());
            System.out.println("e:" + e);
            return null;
        }
        try {
            clase = Class.forName("Tablas." + p_tabla);
            wrkTabla = (Tabla) clase.newInstance();
            wrkcampos = wrkTabla.getCampos();
            System.out.println("campos lock:"+ wrkcampos);
            for (String string : wrkcampos) {
                System.out.println(string);
            }            
            for(int i = 1; i <= wrkcampos.length; i++){
                wrkTabla.setTamano((i-1), md.getPrecision(i));
                wrkTabla.setCampo(wrkcampos[i-1], wrkResultSet.getObject(i));
            }
            //wrkResultSet.close();
        } catch (Exception ex) {
            Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (wrkTabla);
    }

     public List<? extends Tabla> getListaRegistros(String p_tabla, String p_condicion){
        return getListaRegistros(p_tabla, p_condicion,null);
     }
    
    public List<? extends Tabla> getListaRegistros(String p_tabla, String p_condicion, String p_orden){
        List<Tabla> registros = new ArrayList<Tabla>();
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        ResultSet wrkResultSet = null;
        ResultSetMetaData md;
        Class clase;
        String wrkcampos[];
        
        strSQL = "SELECT *";
        strSQL = strSQL + " FROM " + nombreTabla[nombreTabla.length-1] ;
        strSQL = strSQL + " WHERE " + p_condicion;
        if(p_orden!=null){
            strSQL += " ORDER BY " + p_orden;
        }
        
        System.out.println(strSQL);
        try
        {
            Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            //SentenciaSQL.setQueryTimeout(1);
            
            wrkResultSet = SentenciaSQL.executeQuery(strSQL);
            md = wrkResultSet.getMetaData();
            clase = Class.forName(p_tabla);
            while(wrkResultSet.next()){
                Tabla wrkTabla = (Tabla) clase.newInstance();
                wrkcampos = wrkTabla.getCampos();                
                for(int i = 1; i <= wrkcampos.length; i++){
                    wrkTabla.setTamano((i - 1), md.getPrecision(i));
                    if (wrkResultSet.getObject(i) instanceof String){
                        String cadena;
                        cadena = wrkResultSet.getString(i).trim();
                        wrkTabla.setCampo(wrkcampos[i-1], (Object) cadena);
                    }
                    else
                        wrkTabla.setCampo(wrkcampos[i-1], wrkResultSet.getObject(i));
                }
                registros.add(wrkTabla);
            }
            //wrkResultSet.close();
        }
        catch(SQLException e){
            System.out.println("m:" + e.getMessage());
            System.out.println("e:" + e);
            
        }
        catch(Exception ex){
            Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (registros);
    }


    public ResultSet getListaRegistrosJoin(String p_tablas, String p_condicion){
        return getListaRegistrosJoin(p_tablas, p_condicion, null);
    }

    public ResultSet getListaRegistrosJoin(String p_tablas){
        return getListaRegistrosJoin(p_tablas, null, null);
    }

    public ResultSet getListaRegistrosJoin(String p_tablas, String p_condicion, String p_orden){
        String strSQL;
        ResultSet wrkResultSet = null;
        ResultSetMetaData md;
        Class clase;
        String wrkcampos[];

        strSQL = "SELECT *";
        strSQL = strSQL + " FROM " + p_tablas ;
        if(p_condicion!=null) strSQL = strSQL + " WHERE " + p_condicion;
        if(p_orden!=null){
            strSQL += " ORDER BY " + p_orden;
        }

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
        catch(Exception ex){
            Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wrkResultSet;
    }
    
    
    public static Tabla setCaracteristicasTabla(String p_tabla) {
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = p_tabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }
        ResultSet wrkResultSet = null;
        ResultSetMetaData md;
        Tabla wrkTabla = null;
        Class clase;
        String wrkcampos[];
        
        strSQL = "SELECT *";
        strSQL = strSQL + " FROM " + nombre ;
        
        System.out.println(strSQL);
        try
        {
            Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
            SentenciaSQL.setQueryTimeout(1);
            
            wrkResultSet = SentenciaSQL.executeQuery(strSQL);
            md = wrkResultSet.getMetaData();
        }
        catch(SQLException e){
            System.out.println("m:" + e.getMessage());
            System.out.println("e:" + e);
            return null;
        }
        try {
            clase = Class.forName(p_tabla);
            wrkTabla = (Tabla) clase.newInstance();
            wrkcampos = wrkTabla.getCampos();
            
            for (String string : wrkcampos) {
                System.out.println(string);
            }
            
            for(int i = 1; i <= wrkcampos.length; i++){
                wrkTabla.setTamano((i - 1), md.getPrecision(i));
                System.out.println("Campo: "+md.getColumnName(i)+" Tipo: "+md.getColumnTypeName(i));
                if(md.columnNullable == md.isNullable(i)){
                    wrkTabla.setObligatorios(i-1, false);
                }else{
                    wrkTabla.setObligatorios(i-1, true);
                }                
                wrkTabla.setTipos(i-1, md.getColumnTypeName(i));
            }
        } catch (Exception ex) {
            Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (wrkTabla);
    }
    
    
    public void agregar(String p_tabla, Tabla p_registro){
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = p_tabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }
        String serializar;
        
        serializar = serializarSQLInsertar(p_registro);
        strSQL="INSERT INTO " + nombre + " " + serializar;
        
        objBD.bdEjesql(strSQL);

    }
    
    public void modificar(String p_tabla, Tabla p_registro, String p_condicion){
        String strSQL;
        String serializar;
        String [] nombreTabla = p_tabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = p_tabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }
        
        serializar = serializarSQLModificar(p_registro);
        strSQL = "UPDATE " + nombre + " " + serializar;
        strSQL = strSQL + " WHERE " + p_condicion;
        
        objBD.bdEjesql(strSQL);        
    }
    
    public void eliminar(String p_tabla, String p_condicion){
        String strSQL;
        String [] nombreTabla = p_tabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = p_tabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }
        
        strSQL = "DELETE FROM " + nombre;
        strSQL = strSQL + " WHERE " + p_condicion;
        
        objBD.bdEjesql(strSQL);
    }
    
    private String serializarSQLInsertar(Tabla p_tabla){
        String serializa0="";
        String serializa1="";
        
        String wrkcampos[];
      
        wrkcampos = p_tabla.getCampos();
        for(int i = 0; i < wrkcampos.length; i++){
            if (i > 0){
                serializa0 = serializa0 + ", ";
            }
            serializa0 = serializa0 + wrkcampos[i];
        }
        serializa0 = "(" + serializa0 + ") ";
        serializa1 = p_tabla.getSQLInsertar();
        
        return (serializa0 + serializa1);
    }
    
    private String serializarSQLModificar(Tabla p_tabla){
        String serializa0="";

        serializa0 = p_tabla.getSQLModificar();
        
        return (serializa0);
    }
    
    
    public int getSiguienteSecuencia(String partabla, String parcampo, String parcondicion){
        int numsec = 0;
        String strSQL;
        String [] nombreTabla = partabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = partabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }

        numsec = objBD.bdConreg(nombre, parcondicion);
        ResultSet wrkResultSet;     

        if (numsec != 0){
            strSQL = "SELECT max(" + parcampo + ")";
            strSQL = strSQL + " FROM " + nombre ;
            
            if(!parcondicion.equals("")){
                strSQL = strSQL + " WHERE " + parcondicion;
            }

            try{
                Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                wrkResultSet = SentenciaSQL.executeQuery(strSQL);

                if(wrkResultSet.next()){
                    numsec = wrkResultSet.getInt(1);
                }

                //wrkResultSet.close();
            }catch(SQLException e){
                System.out.println("m:" + e.getMessage());
                System.out.println("e:" + e);
            }catch(Exception ex){
                Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return (numsec + 1);

    }
    
    public BigDecimal getSiguienteSecuenciaDec(String partabla, String parcampo, String parcondicion){
        BigDecimal numsec = new BigDecimal(0);
        String strSQL;
        String [] nombreTabla = partabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = partabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }

        numsec = new BigDecimal(objBD.bdConreg(nombre, parcondicion));
        ResultSet wrkResultSet;     

        if (!numsec.equals(new BigDecimal(0))){
            strSQL = "SELECT max(" + parcampo + ")";
            strSQL = strSQL + " FROM " + nombre ;
            
            if(!parcondicion.equals("")){
                strSQL = strSQL + " WHERE " + parcondicion;
            }

            try{
                Statement SentenciaSQL = InterfaceBD.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_READ_ONLY);
                wrkResultSet = SentenciaSQL.executeQuery(strSQL);

                if(wrkResultSet.next()){
                    numsec = wrkResultSet.getBigDecimal(1);
                }

                //wrkResultSet.close();
            }catch(SQLException e){
                System.out.println("m:" + e.getMessage());
                System.out.println("e:" + e);
            }catch(Exception ex){
                Logger.getLogger(InterfaceRegistrosBD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return (numsec.add(new BigDecimal(1)));
    }

    public boolean guardarArchivoDB(String rutaArchivo, String tabla, String campoArchivo, String campoNombre, String condicion){
        File fBlob = new File(rutaArchivo);        
        String [] archivo = rutaArchivo.split("/");
        String nombreArchivo = archivo[archivo.length-1];
        String [] nombreTabla = tabla.split("\\.");
        String nombre="";
        if(nombreTabla.length==1){
            nombre = tabla;
        }else{
            nombre = nombreTabla[nombreTabla.length-1];
        }
        try {
           if(fBlob.exists()){
               PreparedStatement pstmt = InterfaceBD.getConexion().prepareStatement ("UPDATE "+nombre+" SET "+campoArchivo+" = ?, "+campoNombre+"= ? WHERE "+condicion);
               FileInputStream is = new FileInputStream ( fBlob );
               pstmt.setBinaryStream (1, is, (int) fBlob.length() );
               pstmt.setString(2, nombreArchivo);
               pstmt.execute ();
               InterfaceBD.getConexion().commit();
            }else{
               return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        fBlob.delete();
        return true;
    }

    public String getArchivoBD(String tabla, String campoArchivo, String campoNombre, String condicion){
        FileOutputStream archivoNuevo = null;
        String nombre = null;
        String [] nombreTabla = tabla.split("\\.");
        String nombreT="";
        if(nombreTabla.length==1){
            nombreT = tabla;
        }else{
            nombreT = nombreTabla[nombreTabla.length-1];
        }

        try {
            String sql = "SELECT * FROM "+nombreT+" WHERE "+condicion;
            System.out.println(sql);
            PreparedStatement pstmt = InterfaceBD.getConexion().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                byte[] archivoStream = rs.getBytes(campoArchivo);
                nombre = rs.getString(campoNombre);
                archivoNuevo = new FileOutputStream("D:\\Archivos y documentos de LUIS\\NetBeans Projects\\Plantilla\\Plantilla\\build\\web\\Web\\SIIA\\fotos\\"+nombre);
                archivoNuevo.write(archivoStream);
            }
        } catch (Exception e) {
            return null;
        }
        return nombre;
    }
    
}
