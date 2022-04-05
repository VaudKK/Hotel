/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.Util.WindowLocation;
import hotel.DB.*;
import hotel.Util.HError;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Vaud Keith
 */
public class JFSupply extends javax.swing.JFrame {

    /**
     * Creates new form JFSupply
     */
    
    ArrayList<String> itemNames;
    int groupqty = 0;
    
    public JFSupply() {
        initComponents();
        setTitle("Supply");
        setResizable(false);
        setLocation(WindowLocation.getCenterX(this.getWidth()),WindowLocation.getCenterY(this.getHeight()));
        setTopImage();
        setDefaultValues();
        appendTextListener(txtQty);
        appendTextListener(txtPPU);
    }
    
    private void setTopImage(){
        SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
               Image topImageIcon = new ImageIcon(getClass().getResource("/hotel/Images/cropa.png")).getImage();
               picBox.setIcon(new ImageIcon(topImageIcon.getScaledInstance(picBox.getWidth(), picBox.getHeight(),Image.SCALE_SMOOTH)));
               return null;
            }
        };
        
        worker.execute();
        
    }
    
    private void appendTextListener(JTextField textField){
         textField.getDocument().addDocumentListener(new DocumentListener() {

             @Override
             public void insertUpdate(DocumentEvent e) {
                calculate();
             }

             @Override
             public void removeUpdate(DocumentEvent e) {
                calculate();
             }

             @Override
             public void changedUpdate(DocumentEvent e) {
                 calculate();
             }
             
             public void calculate(){
                 try{
                     int qty = Integer.parseInt(txtQty.getText());
                     int ppu = Integer.parseInt(txtPPU.getText());

                     txtTotal.setText(String.format("%d", qty * ppu));
                 }catch(Exception ex){
                     txtTotal.setText("");
                 }
             }
             
         });
    }
    
    
    private void setDefaultValues(){
        Date today  = new Date();
        today = Calendar.getInstance().getTime();   
        datePicker.setDate(today);
    }
    
            /**
     * Determines if a character is a whole number.
     * @param input the character to be evaluated.
     * @return true if the character is  a number,false otherwise.
     */
    private boolean isNumber(char input){
        
        boolean isNum = false;
        
        char[] numbers = "1234567890".toCharArray();
        for(int i = 0;i<numbers.length;i++){
            if(input == numbers[i]){
                return true;
            }
        }
        
        return isNum;
    }
    
    private boolean isTextFieldEmpty(){
        
        boolean isEmpty = false;
        
        for(Component c : this.getRootPane().getContentPane().getComponents()){
            if(c instanceof JTextField){
                if(((JTextField)c).getText().length() == 0){
                    isEmpty = true;
                }
            }
        }
        
        
        return isEmpty;
    }
    
    private void clearFields(){
        for(Component c : this.getRootPane().getContentPane().getComponents()){
            if(c instanceof JTextField){
                ((JTextField)c).setText("");
            }
        }
        
        txtName.requestFocus();
        setDefaultValues();
    }
    
    private void save(){
        
        if(!isTextFieldEmpty()){
            String name = txtName.getText();
            String supplier = txtSupplier.getText();
            String qty = "";
            String ppu = "";
            if(groupqty != 0){
              qty = groupqty+"";
            }else{
              qty = txtQty.getText();
            }
            if(groupqty != 0){
                Float unitprice = Float.parseFloat(txtPPU.getText()) / groupqty;
                ppu = unitprice+"";
            }else{
                ppu = txtPPU.getText();
            }
            String total= txtTotal.getText();        
            String date = new SimpleDateFormat(hotel.DB.HotelDB.getSQLiteDateFormat()).format(datePicker.getDate());
            
            try {
            
                HashMap<String,Object> supply = new HashMap<>();

                supply.put("PID",HotelDB.generateKey(HotelDB.PURCHASES_TABLE, "PID"));
                supply.put("Item", name);
                supply.put("Supplier", supplier);
                supply.put("Quantity", Integer.parseInt(qty));
                supply.put("Measurement", cboMeasure.getSelectedItem());
                supply.put("PPU", Float.parseFloat(ppu));
                supply.put("TotalPrice", Float.parseFloat(total));
                supply.put("DateOfPurchase", date);           
            
                HotelDB.insertTODB(HotelDB.PURCHASES_TABLE, supply);
                
                addToStock(name, Integer.parseInt(qty), cboMeasure.getSelectedItem().toString());
                
                JOptionPane.showMessageDialog(this,name + " has been added successfully.","Saved",JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                
            } catch (SQLException ex) {
               HError.showErrorMessage("SL001: Error occurred while saving your data.", this, ex.getMessage());
            }
            
        }else{
            JOptionPane.showMessageDialog(this,"Please fill all available fields.","Empty Field(s)",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void addToStock(String itemName,int quantity,String measurement){
        try {
            
                HashMap<String,Object> stock = new HashMap<>();
                
                stock.put("Item", itemName);
                stock.put("Quantity", getNewQuantity(itemName, quantity));
                stock.put("Measurement",measurement);          
            
                HotelDB.insertTODB(HotelDB.STOCK_TABLE, stock);
                
            } catch (SQLException ex) {
                HError.showErrorMessage("SL002: Error adding items to stock.", this, ex.getMessage());
            }
    }
    
    
    private int getNewQuantity(String itemName,int qty){
        
        Driver driver = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        int val = 0;
        
        try{
               driver = HotelDB.createDriver();
               conn = HotelDB.getDBConnection();
               String sql = "SELECT Quantity FROM stock WHERE Item = '" + itemName + "' COLLATE NOCASE  LIMIT 1";
               stmt = conn.createStatement();
               
               rs = stmt.executeQuery(sql);              
               
               if(rs.next()){
                   val = rs.getInt(1);
                   val += qty;
               }else{
                   val = qty;
               }               
               
           }catch(Exception ex){
               HError.showErrorMessage("SL003: Sorry error occurred could not save record", this,ex.getMessage());
           }finally{
               if(stmt != null){
                   try{
                       stmt.close();
                   }catch(Exception ex){
                       //DO NOTHING
                   }
               }
               if(conn != null){
                   try{
                       conn.close();
                   }catch(Exception ex){
                       //DO NOTHING
                   }
               }
           }
        
        return val;
    }
    
    private float getTotal(int qty,int ppu){
        return qty *ppu;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        txtQty = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPPU = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        picBox = new javax.swing.JLabel();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel8 = new javax.swing.JLabel();
        cboMeasure = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Item:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Supplier:");

        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Quantity:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("PricePerUnit:");

        txtPPU.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPPUKeyTyped(evt);
            }
        });

        txtTotal.setEditable(false);
        txtTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotalKeyTyped(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("TotalPrice:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Date Of Purchase:");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnView.setText("Table View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        picBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Measurement:");

        cboMeasure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Boxes", "Packets", "Litres", "2KG Packets", "1KG Packets", "3Litre Bottles", "5Litre Bottles", "Sacks", "300 ml Crates", "500 ml Crates", "1Litre Crates", "Dozen", "Bale", "Not Specified" }));
        cboMeasure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMeasureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(picBox, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPPU, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtQty, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addComponent(cboMeasure, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnView, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(picBox, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnView))
                    .addComponent(btnSave))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPPU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboMeasure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        save();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JFSupplyView view = new JFSupplyView();
        view.setVisible(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtTotalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKeyTyped
        if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtTotalKeyTyped

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void txtPPUKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPPUKeyTyped
        if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtPPUKeyTyped

    private void cboMeasureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMeasureActionPerformed
        String[] items =  new String[]{"300 ml Crates", "500 ml Crates", "1Litre Crates", "Dozen", "Bale"};
        for(String item : items){
            if(cboMeasure.getSelectedItem().toString().equalsIgnoreCase(item)){
                String amount = JOptionPane.showInputDialog("Enter amount of items: ");
                try{
                    groupqty = Integer.parseInt(amount);
                }catch(Exception ex){
                    HError.showErrorMessage("Error please ensure the value you entered is a number", this, ex.getMessage());
                    groupqty = 0;
                }
            }
        }      
    }//GEN-LAST:event_cboMeasureActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFSupply.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFSupply().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox cboMeasure;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel picBox;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPPU;
    private javax.swing.JTextField txtQty;
    private javax.swing.JTextField txtSupplier;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
