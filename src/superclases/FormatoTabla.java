/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package superclases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author zeus
 */
public class FormatoTabla extends JFormattedTextField implements TableCellRenderer{

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JFormattedTextField campoTexto = new JFormattedTextField();
        campoTexto.setBorder(BorderFactory.createEmptyBorder());
        if (value instanceof String){
            campoTexto.setText((String)value);
        }
        if(isSelected){
            campoTexto.setBackground(Color.orange);
        }
        return campoTexto;
    }

}
