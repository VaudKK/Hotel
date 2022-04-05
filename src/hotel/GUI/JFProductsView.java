/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import com.sun.glass.events.KeyEvent;
import hotel.DB.HotelDB;
import hotel.Util.DirectoryManager;
import hotel.Util.ExportData;
import hotel.Util.HError;
import hotel.Util.ImportData;
import hotel.Util.WindowLocation;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vaud Keith
 */

public class JFProductsView extends javax.swing.JDialog {

    /**
     * Creates new form JFProductsView
     */
    
    private boolean isSearching = false;
    private boolean allowSearch = false;
    
    String filePath = "";
    File file;
    static String mode = "view";
    
    Vector<String> columnTitles;
    DefaultTableModel model;
    
    public JFProductsView(String mode) {
        initComponents();
        lblNote.setVisible(false);
        this.mode = mode;
        setModal(true);
        setTitle("Products");
        
        lblBusy.setVisible(false);
        Color color = Color.GRAY;
        txtSearch.setForeground(color);
        
        appendTextListener(txtSearch);
        
        setLocation(WindowLocation.getCenterX(getWidth()),WindowLocation.getCenterY(getHeight()));
        SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                isBusy(true);
                populateTable();
                return null;
            }

            @Override
            protected void done() {
                super.done(); 
                isBusy(false);
            }
            
            
        };
        
        worker.execute();
        
        btnImport.requestFocus();
        
        if("edit".equalsIgnoreCase(mode)){lblNote.setVisible(true);}else{ lblNote.setVisible(false);}
    }
    
    private void isBusy(boolean status){
        lblBusy.setVisible(status);
        lblBusy.setBusy(status);
        isSearching = status;
        txtSearch.setEditable(!status);
        btnImport.setEnabled(!status); 
        btnExport.setEnabled(!status);
    }
   
    
    
    /**
     * Populates the table with data from the data source
     */
    private void populateTable(){
        Driver sqliteDriver;
           Connection conn = null;
           Statement stmt = null;
           ResultSet rs = null;
           
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));           

           try{
               sqliteDriver = new org.sqlite.JDBC();
               DriverManager.registerDriver(sqliteDriver);
               
               conn = HotelDB.getDBConnection();
               String sql = "SELECT ProductID,ProductName,Price,Allergens FROM products";
               stmt = conn.createStatement();
               rs = stmt.executeQuery(sql);
               table.setModel(tableModel(rs));
               
               rs.close();
               stmt.close();
               conn.close();
 
               
           }catch(Exception ex){
               HError.showErrorMessage("Database error.Cant fetch data.", null, ex.getMessage());
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
    
     private void populateTable(String itemName){
        Driver sqliteDriver;
           Connection conn = null;
           Statement stmt = null;
           ResultSet rs = null;
           
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));           

           try{
               sqliteDriver = new org.sqlite.JDBC();
               DriverManager.registerDriver(sqliteDriver);
               
               conn = HotelDB.getDBConnection();
               String sql = "SELECT ProductID,ProductName,Price,Allergens FROM products "
                       + "WHERE ProductName LIKE '%" + itemName + "%' COLLATE NOCASE";
               stmt = conn.createStatement();
               rs = stmt.executeQuery(sql);
               table.setModel(tableModel(rs));
               
               rs.close();
               stmt.close();
               conn.close();
 
               
           }catch(Exception ex){
               //For Debugging purposes
               System.out.println(ex.getMessage());
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
        
        textField.getDocument().addDocumentListener( new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                fetch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fetch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fetch();
            }
            
            public void fetch(){
                 if(!isSearching && allowSearch){  
                     try{                    

                         SwingWorker worker = new SwingWorker() {

                             @Override
                             protected Object doInBackground() throws Exception {

                                  if(textField.getText().length() > 0){

                                      isBusy(true);
                                      populateTable(textField.getText());
                                      

                                  }else{
                                      isBusy(true);
                                      populateTable();
                                  }
                                  return null;
                             }

                             @Override
                             protected void done() {
                                 super.done();
                                 isBusy(false);
                             }



                         };

                         worker.execute();

                     }catch(Exception ex){
                        HError.logError(ex.getMessage());
                     }
             }
             }
            
        });
        
    }
    
    /**
     * Creates a table model based on results from a ResultSet
     * @param rs the ResultSet
     * @return DefaultTableModel
     * @throws SQLException 
     */
    private  DefaultTableModel tableModel(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        
        //Get the column names
        columnTitles = new Vector<>();
        Vector<String> columnNames = new Vector<>();
        int columnCount = rsmd.getColumnCount();
        
        for(int i = 1;i<=columnCount;i++){
            columnNames.add(rsmd.getColumnName(i));
        }
        
        columnTitles = columnNames;
        
        //Get the data
        Vector<Vector<Object>> data = new Vector<>();
        while(rs.next()){
            Vector<Object> dataObj = new Vector<>();
            for(int columnIndex =1;columnIndex <= columnCount;columnIndex ++){
                dataObj.add(rs.getObject(columnIndex));
            }
            data.add(dataObj);
        }
        
        return new DefaultTableModel(data,columnNames);
    
    }
    
        private boolean export(){ 
            
            boolean succeeded = false;
            try {
                Random rand = new Random();
                int num = rand.nextInt(100000);
                file = new File(DirectoryManager.getExportDirectory()+"\\products"+num+".xlsx");
                filePath = file.toPath().toString();
                ExportData.writeToExcel(table,file.toPath());
                succeeded = true;
            } catch (Exception ex) {
               HError.showErrorMessage("PV001: Error exporting your data.", null, ex.getMessage());
               succeeded = false;
            }

            return succeeded;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnExport = new javax.swing.JButton();
        lblBusy = new org.jdesktop.swingx.JXBusyLabel();
        txtSearch = new javax.swing.JTextField();
        btnImport = new javax.swing.JButton();
        lblNote = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setGridColor(new java.awt.Color(102, 102, 102));
        table.setIntercellSpacing(new java.awt.Dimension(2, 2));
        table.setRowHeight(20);
        table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        btnExport.setText("Export To Excel");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        txtSearch.setText("Type product name here");
        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtSearchFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSearchFocusLost(evt);
            }
        });

        btnImport.setText("Import From Excel");
        btnImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportActionPerformed(evt);
            }
        });

        lblNote.setText("Select  the row to edit then press insert on the key board.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblNote)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblBusy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExport)
                        .addGap(26, 26, 26))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBusy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExport)
                        .addComponent(btnImport)))
                .addGap(4, 4, 4)
                .addComponent(lblNote)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        SwingWorker worker = new SwingWorker() {    
            
            boolean succeeded;
            
            @Override
            protected Object doInBackground() throws Exception {
                isBusy(true);
                succeeded = export();
                return null;
            }

            @Override
            protected void done() {
                isBusy(false);
              if(succeeded){
                Object[] options = {"Open file","Cancel"};
                int option = JOptionPane.showOptionDialog(null,"Export succeeded do you wish to "
                        + "open the file?","Export", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);
                if(option == JOptionPane.OK_OPTION){
                    if(Desktop.isDesktopSupported()){
                        Desktop desktop = Desktop.getDesktop();
                        if(file != null){
                            if(file.exists()){
                                try {
                                    desktop.open(file);
                                } catch (IOException ex) {
                                   JOptionPane.showMessageDialog(null,"Error opening file","Error",
                                           JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                    else{
                        JOptionPane.showConfirmDialog(null,"Error opening file","Error",
                                           JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE);
                    }
                }
              }
            }
            
            
        };
        
        worker.execute();
    }//GEN-LAST:event_btnExportActionPerformed

    private void tableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_INSERT){
          if(mode.equalsIgnoreCase("edit")){
            if(table.getSelectedRowCount() == 1){;
                try{
                    Window[] window = JFrame.getWindows();
                        for (Window win : window) {
                            if(win.getClass().toString().contains("hotel.GUI.JFProduct")){

                                String id = table.getValueAt(table.getSelectedRow(), 0).toString();
                                String name = table.getValueAt(table.getSelectedRow(),1).toString();
                                String price = table.getValueAt(table.getSelectedRow(), 2).toString();
                                String allergens = table.getValueAt(table.getSelectedRow(),3).toString();

                                String[] nargs = new String[]{id,name,price,allergens,"none"};

                                for(Method method : win.getClass().getDeclaredMethods()){
                                    if(method.getName().equals("getValuesToEdit")){
                                        Method add = method;
                                        add.invoke(win,nargs);
                                    }
                                }                   
                            }
                        }
                    }catch(SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex){
                        HError.logError(ex.getMessage());
                    }
            }
            
            dispose();
          }
        }
    }//GEN-LAST:event_tableKeyPressed

    
    private void txtSearchFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusGained
        txtSearch.setForeground(Color.BLACK);
        if(txtSearch.getText().equalsIgnoreCase("Type product name here")){
           txtSearch.setText("");
        }
        allowSearch = true;
    }//GEN-LAST:event_txtSearchFocusGained

    private void txtSearchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSearchFocusLost
       allowSearch = false;
        if(txtSearch.getText().equalsIgnoreCase("")){
           txtSearch.setForeground(Color.GRAY);
           txtSearch.setText("Type product name here");
       }
    }//GEN-LAST:event_txtSearchFocusLost

    private void btnImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle("Open Excel File");
        chooser.setAcceptAllFileFilterUsed(false);
        
        String[] extension = new String[]{".xlsx"};
        chooser.getFileSystemView().getDefaultDirectory();
        chooser.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if(f.isDirectory()){
                    return true;
                }
                
                String fileName = f.getName().toLowerCase();               
                
                for(String end : extension){
                    if(fileName.endsWith(end))
                       return true;
                }
                
                return false;
                
            }

            @Override
            public String getDescription() {
                 return "Excel Files(*.xlsx)";
            }
        });
        
        int option = chooser.showDialog(this,"Open File");
        
        if(option == JFileChooser.APPROVE_OPTION){                     
            
            SwingWorker worker = new SwingWorker() {

                @Override
                protected Object doInBackground() throws Exception {
                    isBusy(true);
                    try {
                        model = ImportData.readFromFile(new FileInputStream(chooser.getSelectedFile().getAbsolutePath()),0,columnTitles);
                        table.setModel(model);
                    } catch (IOException | NullPointerException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    super.done();
                    isBusy(false);
                }


            }; 

            worker.execute();
            
        }
        
    }//GEN-LAST:event_btnImportActionPerformed

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
            java.util.logging.Logger.getLogger(JFProductsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFProductsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFProductsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFProductsView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFProductsView(mode).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnImport;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXBusyLabel lblBusy;
    private javax.swing.JLabel lblNote;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
