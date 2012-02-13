/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package superclases;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author zeus
 */
public class ModeloLista extends AbstractTableModel{
    
    private String[] columnNames;
    private Object[][] data;
    private boolean habilitarCamposCombo = false;
    private boolean combo = true;
    private String tipo;
      
    public ModeloLista(java.lang.Object[][] data,java.lang.String[] columnNames, String tipo){
        this.columnNames = columnNames;
        this.data = data;
        this.tipo = tipo;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    public boolean isCellEditable(int row, int col) {        
        if (col == 0 || col == 1 || ((col==3 || col == 4 || col == 5) && habilitarCamposCombo)) {
            return true;
        } else {
            return false;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        if(col == 1){
            habilitarCamposCombo = (Boolean)value;
            if(!(Boolean)value){
                setValueAt("", row, 3);
                setValueAt("", row, 4);
                setValueAt("", row, 5);                
            }else{
                if(tipo.equals("campos")){
                    setValueAt(false, row, 0);
                }
            }
        }
        if(col == 0){
            if((Boolean)value){
                combo = false;
                if(tipo.equals("campos")){
                    setValueAt(false, row, 1);
                }
            }else{
                combo = true;
            }
        }
        fireTableCellUpdated(row, col);
    }
    


}
