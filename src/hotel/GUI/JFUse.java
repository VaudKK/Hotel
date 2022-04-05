/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.DB.HotelDB;
import hotel.Util.AutoComplete;
import hotel.Util.HError;
import hotel.Util.WindowLocation;
import java.awt.Component;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Vaud Keith
 */
public class JFUse extends javax.swing.JFrame {

    /**
     * Creates new form JFUse
     */ 
    private static final String COMMIT_ACTION = "commit";
    
    ArrayList<String> stockItems = new ArrayList<>();
    int availableQty = 0;
    
    public JFUse() {
        initComponents();
        setTitle("Daily Use");
        setResizable(false);
        setLocation(WindowLocation.getCenterX(this.getWidth()),WindowLocation.getCenterY(this.getHeight()));
        //appendTextListener(txtName);
        lblResponse.setVisible(false);
        setDefaultValues();
        getStockItems();
        addAutoComplete(txtName);
    }
    
    private void addAutoComplete(JTextField textField){
        textField.setFocusTraversalKeysEnabled(false);
        AutoComplete autoComplete = new AutoComplete(textField,stockItems);
        textField.getDocument().addDocumentListener(autoComplete);
        
        //Maps the tab key to the commit action, which finishes the autocomplete
        //when given a suggestion
        
        textField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        textField.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
    }
    
     private void getStockItems(){
        Driver sqliteDriver;
           Connection conn = null;
           Statement stmt = null;
           ResultSet rs = null;
           
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));           

           try{
               sqliteDriver = new org.sqlite.JDBC();
               DriverManager.registerDriver(sqliteDriver);
               
               conn = HotelDB.getDBConnection();
               String sql = "SELECT Item FROM " + HotelDB.STOCK_TABLE;
               stmt = conn.createStatement();
               rs = stmt.executeQuery(sql);
               
               while(rs.next()){
                   stockItems.add(rs.getString(1));
               }
               
               rs.close();
               stmt.close();
               conn.close(); 
               
           }catch(Exception ex){
               HError.showErrorMessage("U001: Database error.Cant fetch data.", this,ex.getMessage());
           }finally{
               if(rs != null){
                   try{
                       rs.close();
                   }catch(Exception ex){
                       //DO NOTHING
                   }
               }
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
               setCursor(Cursor.getDefaultCursor());
           }
    }
    
    private void appendTextListener(JTextField textField){
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
               getItems(textField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               getItems(textField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
              getItems(textField.getText());
            }
            
             private void getItems( String itemName){
        
                Driver driver = null;
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                String measurement = "";

                if(itemName.length() >0){
                try{
                       driver = HotelDB.createDriver();
                       conn = HotelDB.getDBConnection();
                       String sql = "SELECT Measurement FROM stock WHERE Item = '" + itemName + "' COLLATE NOCASE LIMIT 1";
                       stmt = conn.createStatement();

                       rs = stmt.executeQuery(sql);              

                       if(rs.next()){
                           measurement = rs.getString(1);
                           lblResponse.setVisible(false);
                           cboMeasure.setSelectedItem(measurement);
                       }else{
                           lblResponse.setText("Item not found");
                           lblResponse.setVisible(true);
                       } 

                       rs.close();
                       stmt.close();
                       conn.close();

                   }catch(Exception ex){

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
                }else{
                    lblResponse.setVisible(false);
                }
        
    }
            
        });
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
     
     private void setDefaultValues(){
        Date today  = new Date();
        today = Calendar.getInstance().getTime();   
        datePicker.setDate(today);
        txtName.requestFocus();
    } 
     
    private void clearFields(){
        for(Component c : this.getRootPane().getContentPane().getComponents()){
            if(c instanceof JTextField){
                ((JTextField)c).setText("");
            }
        }
        
        txtName.requestFocus();
    } 
    
    private void save(){
        if(!isTextFieldEmpty()){
            
            String name = txtName.getText();
            String qty = txtQty.getText();;        
            String date = new SimpleDateFormat(hotel.DB.HotelDB.getSQLiteDateFormat()).format(datePicker.getDate());
            
            
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
                availableQty =  (Integer) HotelDB.getValue(HotelDB.STOCK_TABLE,"Quantity",false);
                
                if(Integer.parseInt(qty) > availableQty){
                    HError.showErrorMessage("The quantity you entered exceeds the current amount available.", this);
                    txtQty.requestFocus();
                    return;
                }
                
                HashMap<String,Object> use = new HashMap<>();
 
                use.put("Item", name);
                use.put("Quantity", Integer.parseInt(qty));
                use.put("Measurement", cboMeasure.getSelectedItem());
                use.put("DateOfUse", date);      
            
                HotelDB.insertTODB(HotelDB.DAILYUSE_TABLE, use);
                updateStock(name, Integer.parseInt(qty));
                
                JOptionPane.showMessageDialog(this,"Record saved","Saved",JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                
            } catch (SQLException ex) {
                HError.showErrorMessage("U002: Sorry an error occurred while saving your data.", this,ex.getMessage());
            }finally{
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }else{
            HError.showErrorMessage("Please fill available fields", this);
        }
    }
    
    
    private void updateStock(String itemName,int qty){
        
        if(availableQty > 0){
        
            int newValue = availableQty - qty;
            
            LinkedHashMap<String,Object> item = new LinkedHashMap<>();

            item.put("Quantity", newValue);
            item.put("Item", itemName);

            try {
                HotelDB.updateDB(HotelDB.STOCK_TABLE, item);
            } catch (SQLException ex) {
                 HError.showErrorMessage("U002: Sorry an error occurred while updating your data.", this,ex.getMessage());
            }
        }
        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JTextField();
        cboMeasure = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtQty = new javax.swing.JTextField();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel7 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblResponse = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cboMeasure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Boxes", "Packets", "Litres", "2KG Packets", "1KG Packets", "3Litre Bottles", "5Litre Bottles", "Sacks", "300 ml Crates", "500 ml Crates", "1Litre Crates", "Dozen", "Bale", "Not Specified" }));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Measurement:");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Item:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Quantity:");

        txtQty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtyKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Date:");

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

        lblResponse.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblResponse.setForeground(new java.awt.Color(255, 0, 0));
        lblResponse.setText("Response");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(cboMeasure, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblResponse)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)
                        .addGap(56, 56, 56))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboMeasure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel)
                    .addComponent(lblResponse))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtQtyKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtyKeyTyped
        if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtQtyKeyTyped

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

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
            java.util.logging.Logger.getLogger(JFUse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFUse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFUse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFUse.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFUse().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cboMeasure;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblResponse;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtQty;
    // End of variables declaration//GEN-END:variables
}
