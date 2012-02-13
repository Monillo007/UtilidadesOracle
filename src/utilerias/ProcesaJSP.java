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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JOptionPane;
import superclases.Combo;

/**
 *
 * @author zeus
 */
class ProcesaJSP {

    String tabla = null;
    String[] campos;
    String[] tipos;
    int[] tamanos;
    ResultSetMetaData rsmd;
    ResultSet resultado;
    String[] tiposString = new String[]{"VARCHAR","VARCHAR2","NVARCHAR",
                                        "NVARCHAR2","CHAR","NCHAR",
                                        "CHARACTER","CHARACTER VARYING"};
    String[] tiposBigDecimal = new String[]{"NUMBER","INTEGER","DECIMAL",
                                          "INT","SMALLINT","LONG","FLOAT"};
    String[] tiposDate = new String[]{"DATE","TIME","TIMESTAMP"};
    String[] tiposFile = new String[]{"LONG RAW","BLOB","CLOB","NCLOB","BFILE","RAW"};

    public ProcesaJSP(String tabla, ArrayList<String> camposA) {
        this.tabla = tabla;
        this.campos = camposA.toArray(new String[camposA.size()]);
        System.out.println("tabla: "+tabla);
        doAll();
    }

    private void doAll() {
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
            setTamanos();
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

    public void setTamanos() throws SQLException{
        tamanos = new int[rsmd.getColumnCount()];
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            int tamano = rsmd.getPrecision(i);
            tamanos[i-1] = tamano;
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
        File jspFile = new File(VariablesGlobales.getRutaDestino()+"\\"+VariablesGlobales.getNombreArchivo()+".jsp");
        if(jspFile.exists()){
            jspFile.delete();
        }

        jspFile.createNewFile();
        FileWriter fichero = new FileWriter(jspFile);
        PrintWriter pw = new PrintWriter(fichero);

        /***** CABECERA *****/
        pw.println("<%@ page contentType=\"text/html; charset=ISO-8859-1\" language=\"java\" import=\""+VariablesGlobales.getImportString()+"\" errorPage=\"\"%>");
        pw.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        pw.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        pw.println("<head>");
        pw.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
        pw.println("<title>"+VariablesGlobales.getNombreArchivo()+"</title>");

        /***** DEFINICION DE ESTILOS *****/
        pw.println("<!-- ");
        pw.println("LA DEFINICION DE ESTILOS Y JAVASCRIPT VA AQUI");
        pw.println("-->");
        pw.println("<link rel=stylesheet type=\"text/css\" href=\"../../../CSS/formsSIIA.css\"/>");
        pw.println("<script type=\"text/javascript\" src=\"../Javascript/\"></script>");
        pw.println("<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"../../../Modulos/calendario/calendar-blue.css\" title=\"win2k-cold-1\" />\n" +
                "<script type=\"text/javascript\" src=\"../../../Modulos/calendario/calendar.js\"></script>\n" +
                "<script type=\"text/javascript\"  src=\"../../../Modulos/calendario/lang/calendar-es.js\" ></script>\n" +
                "<script type=\"text/javascript\" src=\"../../../Modulos/calendario/calendar-setup.js\"></script>");

        /***** INICIALIZACION VARIABLES JAVA *****/
        pw.println("<%");        
        pw.println("String idUserSession= (String) session.getAttribute(\"idUserSession\");");
        pw.println("String accion = \"C\";");

        for(String campo : campos){
            pw.println("String "+campo+"=\"\";");
        }

        pw.println(tabla+" "+tabla.toLowerCase()+";");

        pw.println("     InterfaceBDPool bd = new InterfaceBDPool();");
        pw.println("     if(!bd.getEsquema().equals(\""+VariablesGlobales.getEsquema()+"\")){");
        pw.println("          bd.bdCierra();");
        pw.println("          bd.setEsquema(\""+VariablesGlobales.getEsquema()+"\");");
        pw.println("     }");
        pw.println("     bd.bdConexion();");
        pw.println("     InterfaceRegistrosBDPool registros = new InterfaceRegistrosBDPool();");
        pw.println("     registros.setConexion(bd.getConexion());");

        pw.println("if(request.getParameter(\""+campos[0]+"\")!=null && !request.getParameter(\""+campos[0]+"\").equals(\"\")){");
        pw.println("     "+campos[0]+" = request.getParameter(\""+campos[0]+"\");");
        pw.println("     accion = \"M\";");
        pw.println("     "+tabla.toLowerCase()+" = ("+tabla+")registros.getRegistro(\""+VariablesGlobales.getPaqClases()+"."+tabla+"\", \""+campos[0]+" = \"+"+campos[0]+");");
        int i = 0;
        for(String campo : campos){
            if(i!=0){
                if(tipos[i].equals("String")){
                    pw.println("     "+campo+" = "+tabla.toLowerCase()+".get"+campo+"();");
                }else if(tipos[i].equals("BigDecimal")){
                    pw.println("     "+campo+" = \"\"+"+tabla.toLowerCase()+".get"+campo+"();");
                }else if(tipos[i].equals("Date")){
                    pw.println("     "+campo+" = Util.convierteFechaMostrar("+tabla.toLowerCase()+".get"+campo+"());");
                }
            }

            i++;
        }

        pw.println("");
        pw.println("}");
        pw.println("%>");//FIN DE INICIALIZACION DE VARIABLES JAVA

        pw.println("");
        pw.println("</head>");
        pw.println("");

        /***** CUERPO DEL DOCUMENTO *****/
        pw.println("<body style=\"background-color:#d9e0f0; width:100%;\">");
        pw.println("<table align=\"center\" border=\"1px\" bordercolor=\"#000033\" width=\"95%\" style=\"background-color:#d9e0f0;\"><tr><td>");
        pw.println("<div id=\"contenedor\" style=\"width:100%; background:#d9e0f0;\">");
        pw.println("<form method=\"POST\" name=\""+VariablesGlobales.getNombreFormulario()+"\" id=\""+VariablesGlobales.getNombreFormulario()+"\">");
        pw.println("     <table id=\""+VariablesGlobales.getTituloTabla()+"\" name=\""+VariablesGlobales.getTituloTabla()+"\" align=\"center\" style=\"border:1px;width:100%\">");

        pw.println("          <tr>");
        pw.println("               <td colspan=\"4\" align=\"center\" class='Titulo'>");
        pw.println("                    "+VariablesGlobales.getTituloTabla());
        pw.println("               </td>");
        pw.println("          </tr>");

        int x = 0;
        for (int j = 0; j < campos.length; j++) {
            String campo = campos[j];
            boolean isCombo = false;
            Combo combo = null;

            for (Iterator it = VariablesGlobales.getCombos().iterator(); it.hasNext();) {
                Combo c = (Combo) it.next();
                if(campo.equals(c.toString())){
                    isCombo = true;
                    combo = c;
                }
            }

            if(!VariablesGlobales.getCamposOcultos().contains(campo)){
                x++;
                if(x%2!=0){pw.println("          <tr>");}
                pw.println("               <td class='NombreDeCampo'>"+campo+" :</td>");

                if(isCombo){
                    pw.println("               <td>");
                    pw.println("                   <%");
                    pw.println("                        out.println(UtilHTML.getComboRegistros(\""+combo.getTabla()+"\", \"1=1\", \""+combo.getId()+"\", \""+combo.getId()+"\", \""+combo.getEtiqueta()+"\", null, \"class='Combo' id='"+combo.getId()+"' name='"+combo.getId()+"' style=\\\"width:90%;\\\"\"));");
                    pw.println("                   %>");
                    pw.println("               </td>");
                }else if(tipos[j].equals("String")){
                    pw.println("               <td><input type=\"text\" name=\""+campo+"\" id=\""+campo+"\" class=\"CuadroTexto\" value=\"<%="+campo+"%>\" maxlength='"+tamanos[j]+"' style=\"width:90%;\"/></td>");
                }else if(tipos[j].equals("BigDecimal")){
                    pw.println("               <td><input type=\"text\" name=\""+campo+"\" id=\""+campo+"\" class=\"CuadroTexto\" value=\"<%="+campo+"%>\" style=\"width:90%;\"/></td>");
                }else if(tipos[j].equals("Date")){
                    pw.println("               <td>");
                    pw.println("                    <input type=\"text\" name=\""+campo+"\" id=\""+campo+"\" value=\"<%="+campo+"%>\" style=\"width:50%;\" class=\"CuadroTextoSL\" readonly='readonly' onclick =\"this.value=''\" />");
                    pw.println("                    <img src=\"../../../Imagenes/gif/imgCalendario.gif\" alt=\"CALENDARIO\" name=\"boton_"+campo+"\"  tabindex=\"14\" id=\"boton_"+campo+"\" style=\"cursor: pointer; border: 1px solid;\" align=\"bottom\" title=\"Selecci&oacute;n Fecha\" />");
                    pw.println("                    <script type=\"text/javascript\">\n" +
                            "                Calendar.setup({\n" +
                            "                inputField     :    \""+campo+"\",     // id del campo de texto\n" +
                            "                 ifFormat     :     \"%d/%m/%Y\",     // formato de la fecha que se escriba en el campo de texto\n" +
                            "                 button     :    \"boton_"+campo+"\"     // el id del bot&oacute;n que lanzar&aacute; el calendario\n" +
                            "                });\n" +
                            "           </script>\n");
                    pw.println("               </td>");
                }



                if(x%2==0){pw.println("          </tr>");}
            }
        }

        pw.println("     </table>");
        pw.println("<div align=\"center\">");
        for(Iterator it = VariablesGlobales.getCamposOcultos().iterator(); it.hasNext();){
            String campo = (String)it.next();
            if(!campo.equals("USUARIOCREACION") && !campo.equals("USUARIOMODIFICACION") && !campo.equals("FECHACREACION") && !campo.equals("FECHAMODIFICACION")){
                pw.println("          <input type=\"hidden\" id=\""+campo+"\" name=\""+campo+"\" value=\"<%="+campo+"%>\"/>");
            }
        }
        pw.println("          <input type=\"hidden\" id=\"idUser\" name=\"idUser\" value=\"<%=idUserSession%>\"/>");
        pw.println("          <input type=\"hidden\" id=\"accion\" name=\"accion\" value=\"<%=accion%>\"/>");
        pw.println("          <input type=\"hidden\" id=\"tablaBD\" name=\"tablaBD\" value=\""+VariablesGlobales.getPaqClases()+"."+tabla+"\"/>");

        pw.println("          <input type=\"hidden\" id=\"pagRespuesta\" name=\"pagRespuesta\" value=\"\"/><!--MODIFICAR ESTO-->");
        pw.println("          <input type=\"hidden\" id=\"condicion\" name=\"condicion\" value=\""+campos[0]+"= <%="+campos[0]+"%>\"/><!--MODIFICAR ESTO-->");
        pw.println("          <input type=\"hidden\" id=\"autoInc\" name=\"autoInc\" value=\"true\"/><!--MODIFICAR ESTO-->");
        pw.println("          <input type=\"hidden\" id=\"ESQUEMA\" name=\"ESQUEMA\" value=\""+VariablesGlobales.getEsquema()+"\"/>");
        pw.println("          <input type=\"hidden\" id=\"validaGrid\" name=\"validaGrid\" value=\"0\"/>");
        pw.println("          <img onmouseover=\"document.images['btnGuar'].src='../../../Imagenes/png/btnGuardar1.png'\" onmouseout=\"document.images['btnGuar'].src='../../../Imagenes/png/btnGuardar.png'\" name=\"btnGuar\" src=\"../../../Imagenes/png/btnGuardar.png\" onclick=\"eventoBoton('accion',document.getElementById('accion').value,'formDatos','../../../ManejadorBD');\"/><!--MODIFICAR ESTO-->");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</td></tr></table>");

        /******************** GRID DE MODIFICACION *************************/
        pw.println("<div align='center' id='gridDatos' name='gridDatos' style='OVERFLOW: scroll; width:100%; height:450px;'>");
        pw.println("     <form name='modificar' id='modificar' method='POST' action='"+VariablesGlobales.getNombreArchivo()+".jsp'>");
        pw.println("          <% out.println(UtilHTML.generarGrid(\""+tabla+"\",");
        String camposGrid = "";
        for (int j = 0; j < campos.length-5; j++) {
            String campo = campos[j];
            if(j==0)camposGrid += campo;
            if(j!=0)camposGrid += ","+campo;
        }
        pw.println("             \""+camposGrid+"\",");
        pw.println("             \""+camposGrid+"\",");
        pw.println("             \"NOT ESTATUSREGISTRO = 99\",");
        pw.println("             \""+campos[0]+"\",");
        pw.println("             \"width='80%' border='0' cellspacing='1' cellpadding='1'\",");
        pw.println("             null,");
        pw.println("             \"class='Encabezado'\",");
        pw.println("             null,");
        pw.println("             \"class='contenidoovertr'\",");
        pw.println("             true,");
        pw.println("             \""+campos[0]+"\",");
        pw.println("             \""+campos[0]+"\",");
        pw.println("             \""+VariablesGlobales.getEsquema()+"\"));");
        pw.println("%>");
        pw.println("<input type=\"hidden\" id=\"accion\" name=\"accion\" value=\"modificar\"/>");
        pw.println("<img name='btnModificar' border='0' onmouseover=\"document.images['btnModificar'].src='../../../Imagenes/gif/btnModificar1.gif'\" onmouseout=\"document.images['btnModificar'].src='../../../Imagenes/png/btnModificar.png'\" src='../../../Imagenes/png/btnModificar.png' alt='Modificar' onclick=\"eventoBoton('modificar',document.getElementById('accion').value,'modificar','"+VariablesGlobales.getNombreArchivo()+".jsp');\"/>");
        pw.println("</form></div>");


        pw.println("</body>");
        pw.println("</html>");


        fichero.close();

    }

}
