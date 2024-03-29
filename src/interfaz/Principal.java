/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Principal.java
 *
 * Created on 21/07/2009, 11:47:34 AM
 */

package interfaz;

import interfacebd.InterfaceBD;
import java.awt.Component;
import javax.swing.JOptionPane;
import manejadores.mnjListaTablas;
import utilerias.VariablesGlobales;

/**
 *
 * @author zeus
 */
public class Principal extends javax.swing.JFrame {

    InterfaceBD bd;
    /** Creates new form Principal */
    public Principal() {
        initComponents();
        this.setLocationRelativeTo(null);
        txBD.requestFocus();
        this.setVisible(true);
    }

    public boolean validar(){
        String campo = "";
        String foco = "";
        int error = 0;
        if(txServidor.getText().equals("")){
            campo = "Servidor";
            foco = "txServidor";
            error = 1;
        }
        if(error ==0 && txPuerto.getText().equals("")){
            campo = "Puerto";
            foco = "txPuerto";
            error = 1;
        }
        if(error ==0 && txBD.getText().equals("")){
            campo = "BD";
            foco = "txBD";
            error = 1;
        }
        if(error ==0 && txEsquema.getText().equals("")){
            campo = "Esquema";
            foco = "txEsquema";
            error = 1;
        }
        if(error ==0 && txUsuario.getText().equals("")){
            campo = "Usuario";
            foco = "txUsuario";
            error = 1;
        }
        if(error ==0 && txContra.getText().equals("")){
            campo = "Contraseña";
            foco = "txContra";
            error = 1;
        }
        if(error!=0){

            JOptionPane.showMessageDialog(this, "El campo "+campo+" no debe quedar vacío.");
            /*Component[] componentes = this.getRootPane().getComponents();
            for (Component component : componentes) {
                System.out.println(component.getName());
                if(component.getName().equals(foco)){
                    System.out.println("coincide");
                    component.requestFocusInWindow();
                }
            }*/
            return false;
        }else{
            return true;
        }
    }

    public boolean crearConexion(){
        boolean crear = false;
        if(validar()){
            bd = new InterfaceBD(txServidor.getText(),
                                             txPuerto.getText(),
                                             txBD.getText(),
                                             txUsuario.getText(),
                                             new String(txContra.getPassword()),
                                             txEsquema.getText());
            if(bd.bdConexion()){
                crear =  true;
            }else{
                JOptionPane.showMessageDialog(this, "No se pudo crear la conexión","Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        return crear;
    }

    private void establecerValoresGlobales() {
        VariablesGlobales.setServidor(txServidor.getText());
        VariablesGlobales.setPuerto(txPuerto.getText());
        VariablesGlobales.setBD(txBD.getText());
        VariablesGlobales.setEsquema(txEsquema.getText());
        VariablesGlobales.setUsuario(txUsuario.getText());
        VariablesGlobales.setContra(new String(txContra.getText()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txServidor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txPuerto = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txBD = new javax.swing.JTextField();
        txEsquema = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txUsuario = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txContra = new javax.swing.JPasswordField();
        btProbar = new javax.swing.JButton();
        btSiguiente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Configurar Conexión");
        setResizable(false);

        jLabel1.setText("Servidor:");

        txServidor.setText("192.168.202.203");
        txServidor.setName("txServidor"); // NOI18N
        txServidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txServidorActionPerformed(evt);
            }
        });

        jLabel2.setText("Puerto:");

        txPuerto.setText("1521");
        txPuerto.setName("txPuerto"); // NOI18N

        jLabel3.setText("BD:");

        txBD.setName("txBD"); // NOI18N
        txBD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txBDActionPerformed(evt);
            }
        });

        txEsquema.setText("PGJDEVELOP");
        txEsquema.setName("txEsquema"); // NOI18N
        txEsquema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txEsquemaActionPerformed(evt);
            }
        });

        jLabel4.setText("Esquema:");

        jLabel5.setText("Usuario:");

        txUsuario.setText("PGJDEVELOP");
        txUsuario.setName("txUsuario"); // NOI18N

        jLabel6.setText("Contraseña:");

        txContra.setText("pgjDevTWK");
        txContra.setName("txContra"); // NOI18N
        txContra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txContraActionPerformed(evt);
            }
        });

        btProbar.setFont(new java.awt.Font("Tahoma", 0, 10));
        btProbar.setText("Probar");
        btProbar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btProbarActionPerformed(evt);
            }
        });

        btSiguiente.setFont(new java.awt.Font("Tahoma", 0, 10));
        btSiguiente.setText("Siguiente>");
        btSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSiguienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txEsquema, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txServidor, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txBD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txContra, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btProbar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(41, 41, 41))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(161, Short.MAX_VALUE)
                .addComponent(btSiguiente)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txEsquema, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(txUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(txContra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btProbar)
                .addGap(29, 29, 29)
                .addComponent(btSiguiente)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txServidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txServidorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txServidorActionPerformed

    private void btProbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btProbarActionPerformed
        if(crearConexion()){
            JOptionPane.showMessageDialog(this, "Conexión Exitosa");
            bd.bdCierra();
        }
    }//GEN-LAST:event_btProbarActionPerformed

    private void btSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSiguienteActionPerformed
        if(crearConexion()){
            establecerValoresGlobales();
            this.dispose();
            new mnjListaTablas(bd);
        }
    }//GEN-LAST:event_btSiguienteActionPerformed

    private void txEsquemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txEsquemaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txEsquemaActionPerformed

    private void txBDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txBDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txBDActionPerformed

    private void txContraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txContraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txContraActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btProbar;
    private javax.swing.JButton btSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txBD;
    private javax.swing.JPasswordField txContra;
    private javax.swing.JTextField txEsquema;
    private javax.swing.JTextField txPuerto;
    private javax.swing.JTextField txServidor;
    private javax.swing.JTextField txUsuario;
    // End of variables declaration//GEN-END:variables

}
