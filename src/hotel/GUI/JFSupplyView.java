/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.DB.HotelDB;
import hotel.Util.HError;
import hotel.Util.WindowLocation;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Linda Bundi
 */
public class JFSupplyView extends javax.swing.JFrame {

    /**
     * Creates new form JFSupplyView
     */
    public JFSupplyView() {
        initComponents();
        setTitle("Supplies");
        setSize(900, 600);
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
    }
    
    private void isBusy(boolean state){
        lblBusy.setVisible(state);
        lblBusy.setBusy(state);
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
               String sql = "SELECT * FROM purchases ORDER BY DateOfPurchase DESC";
               stmt = conn.createStatement();
               rs = stmt.executeQuery(sql);
               table.setModel(tableModel(rs));
               
               rs.close();
               stmt.close();
               conn.close();
 
               
           }catch(Exception ex){
              HError.showErrorMessage("SLV001: Database error.Cant fetch data.", this,ex.getMessage());
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
    
     private void populateTable(String date){
        Driver sqliteDriver;
           Connection conn = null;
           Statement stmt = null;
           ResultSet rs = null;
           
           setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));           

           try{
               sqliteDriver = new org.sqlite.JDBC();
               DriverManager.registerDriver(sqliteDriver);
               
               conn = HotelDB.getDBConnection();
               String sql = "SELECT * FROM purchases WHERE  DateOfPurchase = '" + date +"'";
               stmt = conn.createStatement();
               rs = stmt.executeQuery(sql);
               table.setModel(tableModel(rs));
               
               rs.close();
               stmt.close();
               conn.close();
 
               
           }catch(Exception ex){
               HError.showErrorMessage("SLV002: Database error.Cant fetch data.", this,ex.getMessage());
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
    
    /**
     * Creates a table model based on results from a ResultSet
     * @param rs the ResultSet
     * @return DefaultTableModel
     * @throws SQLException 
     */
    private  DefaultTableModel tableModel(ResultSet rs) throws SQLException{
        ResultSetMetaData rsmd = rs.getMetaData();
        
        //Get the column names
        Vector<String> columnNames = new Vector<>();
        int columnCount = rsmd.getColumnCount();
        
        for(int i = 1;i<=columnCount;i++){
            columnNames.add(rsmd.getColumnName(i));
        }
        
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
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        btnAll = new javax.swing.JButton();

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
        table.setIntercellSpacing(new java.awt.Dimension(2, 2));
        table.setRowHeight(20);
        jScrollPane1.setViewportView(table);

        btnExport.setText("Export To Excel");

        datePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePickerActionPerformed(evt);
            }
        });

        jLabel1.setText("Search By Date:");

        btnAll.setText("Show All");
        btnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblBusy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                .addComponent(btnAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExport)
                .addGap(16, 16, 16))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblBusy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExport)
                        .addComponent(btnAll)))
                .addContainerGap(323, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(104, 104, 104)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datePickerActionPerformed

        SwingWorker worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                isBusy(true);
                Date date = new Date();
                DateFormat  dateFormat = new SimpleDateFormat(hotel.DB.HotelDB.getSQLiteDateFormat());
                date = datePicker.getDate();
                populateTable(dateFormat.format(date));
                return null;
            }

            @Override
            protected void done() {
                super.done();
                isBusy(false);
            }

        };

        worker.execute();
    }//GEN-LAST:event_datePickerActionPerformed

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        SwingWorker work = new SwingWorker() {

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

        work.execute();
    }//GEN-LAST:event_btnAllActionPerformed

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
            java.util.logging.Logger.getLogger(JFSupplyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFSupplyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFSupplyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFSupplyView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFSupplyView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnExport;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXBusyLabel lblBusy;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
