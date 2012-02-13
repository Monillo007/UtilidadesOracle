/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilerias;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author zeus
 */
public class Util {

    /**
     * Redimensiona las columnas para establecer sus tamaños de acuerdo al porcentaje especificado
     * con el método setTamanoColumnas(), si no se ha llamado dicho método se establecen todos los tamaños
     * con el mismo valor.
     */
    public static void setAnchoColumnas(JTable tabla,int... porcentaje){
        JViewport scroll =  (JViewport) tabla.getParent();
        int ancho = scroll.getWidth();
        int anchoColumna;
        TableColumnModel modeloColumna = tabla.getColumnModel();
        TableColumn columnaTabla;

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            columnaTabla = modeloColumna.getColumn(i);
            if(porcentaje!=null){ anchoColumna = ((porcentaje[i])*ancho)/100;}
            else{ anchoColumna = ancho /tabla.getColumnCount();}
            columnaTabla.setPreferredWidth(anchoColumna);
        }

    }
}
