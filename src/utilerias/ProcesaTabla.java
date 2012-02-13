/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilerias;

import interfacebd.InterfaceBD;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Ing. Luis M. Navarro Rangel.
 * 
 */
public class ProcesaTabla {

    String tabla;
    String[] campos;
    String[] tipos;
    ResultSetMetaData rsmd;
    ResultSet resultado;
    String[] tiposString = new String[]{"VARCHAR","VARCHAR2","NVARCHAR",
                                        "NVARCHAR2","CHAR","NCHAR",
                                        "CHARACTER","CHARACTER VARYING","CLOB","NCLOB",};
    String[] tiposBigDecimal = new String[]{"NUMBER","INTEGER","DECIMAL",
                                          "INT","SMALLINT","LONG","FLOAT"};
    String[] tiposDate = new String[]{"DATE","TIME","TIMESTAMP"};
    String[] tiposFile = new String[]{"LONG RAW","BLOB","BFILE","RAW"};

    public ProcesaTabla(String tabla) {
        this.tabla = tabla;
        doAll();
    }

    public void doAll(){
        String consulta = "SELECT * FROM "+tabla;
        try
        {
            Statement SentenciaSQL =
                InterfaceBD.getConexion().createStatement(
                                                          ResultSet.TYPE_FORWARD_ONLY,
                                                          ResultSet.CONCUR_READ_ONLY);

            resultado = SentenciaSQL.executeQuery(consulta);
            rsmd = resultado.getMetaData();
            setCampos();
            setTipos();
            createFile();
        }catch(Exception e){
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al leer la tabla '"+tabla+"'.\n"+e.getMessage());
        }
    }
    
    public void setCampos() throws SQLException{
        campos = new String[rsmd.getColumnCount()];
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String campo = rsmd.getColumnName(i);
                campos[i-1] = campo;                
        }
    }

    public void setTipos() throws SQLException{
        tipos = new String[rsmd.getColumnCount()];
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String tipo = rsmd.getColumnTypeName(i);
                for (String tip : tiposString) {
                    if(tipo.equalsIgnoreCase(tip)){
                        tipos[i-1] = "String";
                    }
                }
                for (String tip : tiposBigDecimal) {
                    if(tipo.equalsIgnoreCase(tip)){
                        tipos[i-1] = "BigDecimal";
                    }
                }
                for (String tip : tiposDate) {
                    if(tipo.equalsIgnoreCase(tip)){
                        tipos[i-1] = "Date";
                    }
                }
                for (String tip : tiposFile) {
                    if(tipo.equalsIgnoreCase(tip)){
                        tipos[i-1] = "File";
                    }
                }                
        }
    }

    private void createFile() throws IOException {
        File claseTabla = new File(VariablesGlobales.getRutaDestino()+"\\"+tabla+".java");
        if(claseTabla.exists()){
            claseTabla.delete();
        }
            claseTabla.createNewFile();
            FileWriter fichero = new FileWriter(claseTabla);
            PrintWriter pw = new PrintWriter(fichero);

            /**CREDITOS**/
            pw.println("/*");
            pw.println("* Clase generada automaticamente en una aplicacion ");
            pw.println("* escrita por el Ing. Luis Manuel Navarro R. para la ");
            pw.println("* Procuraduria General de Justicia del Estado de Guanajuato. ");
            pw.println("*/");
            pw.println();
             
            /****ESCRITURA EN ARCHIVO****/
            pw.println("package "+VariablesGlobales.getPaqClases()+";");
            pw.println();
            pw.println("import "+VariablesGlobales.getPaqClaseTabla()+".*;");
            pw.println("import java.math.BigDecimal;");
            pw.println("import java.util.Date;");
            pw.println();
            pw.println();

            pw.println("public class "+tabla+" extends Tabla{");

            /****VARIABLES A NIVEL DE CLASE*****/
            for (int i = 0; i < campos.length; i++) {
                pw.println("     private "+tipos[i]+" "+campos[i]+";");
            }

            /****CONSTRUCTOR POR DEFECTO*****/
            pw.println();
            pw.println("     public "+tabla+" () {");
            pw.println("          campos = new String["+campos.length+"];");
            for (int i = 0; i < campos.length; i++) {
                pw.println("          campos["+i+"] = \""+campos[i]+"\";");
            }
            pw.println("     }");//cierre constructor por defecto


            /*****CONSTRUCTOR SOBRECARGADO*****/
            String st = "     public "+tabla+" (";
            for (int i = 0; i < campos.length; i++) {
                if(i!=0){
                    st += ", ";
                }
                st += tipos[i]+" "+campos[i]+"";
            }
            st += "){";
            pw.println();
            pw.println(st);
            pw.println("          this();");
            for (int i = 0; i < campos.length; i++) {
                pw.println("          this."+campos[i]+" = "+campos[i]+";");
            }
            pw.println("     }");//cierre constructor sobrecargado
            pw.println();

            /*****METODO setCampo()*****/
            pw.println("     @Override");
            pw.println("     public void setCampo(String p_nombrecampo, Object p_valorcampo){");
            for (int i = 0; i < campos.length; i++) {
                if(i==0){
                    pw.println("          if(p_nombrecampo.equals(\""+campos[i]+"\")){");
                }else{
                    pw.println("          }else if(p_nombrecampo.equals(\""+campos[i]+"\")){");
                }
                pw.println("               set"+campos[i]+"(("+tipos[i]+")p_valorcampo);");
                if(i==(campos.length-1)){
                    pw.println("          }");
                }
            }
            pw.println("     }");//cierre metodo setCampo()

            /*****METODO getSQLInsertar*****/
            pw.println();
            pw.println("     @Override");
            pw.println("     public String getSQLInsertar(){");
            pw.println("          String serializar;");
            pw.println("          serializar = \" VALUES(\"+");
            for (int i = 0; i < campos.length; i++) {
                String comilla = "";
                String c = campos[i];
                String t = tipos[i];
                String pre = "";
                String pos = "";
                if(t.equalsIgnoreCase("String")){
                    comilla = "'";
                }else{
                    comilla = "";
                }
                if(t.equalsIgnoreCase("Date")){
                    pre = "Util.convierteFechaBD(";
                    pos = ")";
                }else{
                    pre = "";
                    pos = "";
                }
                if(i==0){
                    pw.println("          \""+comilla+"\"+"+pre+"get"+c+"()"+pos+"+\""+comilla+"\"+");
                }else{
                    pw.println("          \", "+comilla+"\"+"+pre+"get"+c+"()"+pos+"+\""+comilla+"\"+");
                }
            }
            pw.println("          \")\";");//cierre de VALUES
            pw.println();
            pw.println("          return serializar;");
            pw.println("     }");//cierre de metodo getSQLInsertar



            /*****METODO getSQLModificar*****/
            pw.println();
            pw.println("     @Override");
            pw.println("     public String getSQLModificar(){");
            pw.println("          String serializar;");
            pw.println("          serializar = \" SET \"+");
            for (int i = 0; i < campos.length; i++) {
                String comilla = "";
                String c = campos[i];
                String t = tipos[i];
                String pre = "";
                String pos = "";
                if(t.equalsIgnoreCase("String")){
                    comilla = "'";
                }else{
                    comilla = "";
                }
                if(t.equalsIgnoreCase("Date")){
                    pre = "Util.convierteFechaBD(";
                    pos = ")";
                }else{
                    pre = "";
                    pos = "";
                }
                if(i==0){
                    pw.println("          \""+c+" = "+comilla+"\"+"+pre+"get"+c+"()"+pos+"+\""+comilla+"\"+");
                }else{
                    pw.println("          \", "+c+" = "+comilla+"\"+"+pre+"get"+c+"()"+pos+"+\""+comilla+"\"+");
                }
            }
            pw.println("          \"\";");
            pw.println();
            pw.println("          return serializar;");
            pw.println("     }");//cierre de metodo getSQLModificar

            /*****GETTER'S & SETTER'S*****/
            for (int i = 0; i < campos.length; i++) {
                pw.println("     public "+tipos[i]+" get"+campos[i]+"(){");
                if(tipos[i].equals("String")){
                    pw.println("          if("+campos[i]+"==null || "+campos[i]+".equalsIgnoreCase(\"null\")){");
                    pw.println("               return \"\";");
                    pw.println("          }else{");
                }
                pw.println("                  return "+campos[i]+";");
                if(tipos[i].equals("String")){
                    pw.println("          }");
                }
                pw.println("     }");
                pw.println();
                pw.println("     public void set"+campos[i]+"("+tipos[i]+" "+campos[i]+"){");
                pw.println("          this."+campos[i]+" = "+campos[i]+";");
                pw.println("     }");
                pw.println();
            }

            /*****METODO getID()*****/
            pw.println("     @Override");
            pw.println("     public String getID() {");
            pw.println("          return \""+campos[0]+"\";");
            pw.println("     }");
            pw.println();
            

            pw.println("}");//cierre de clase
            fichero.close();
        
    }

}
