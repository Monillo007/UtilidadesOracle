/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilidades;

import interfaz.Principal;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Ing. Luis Navarro
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                                          "No se pudo cargar el estilo visual",
                                          "Error",
                                          JOptionPane.ERROR_MESSAGE);
        }
        new Principal();
    }

}
