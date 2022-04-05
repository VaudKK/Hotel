/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.DB.HotelDB;
import hotel.Util.HError;
import hotel.Util.RowItem;
import hotel.Util.WindowLocation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXBusyLabel;

/**
 *
 * @author Vaud Keith
 */
public class Sales extends JDialog{
    
    float totalPrice = 0f;
    float balance = 0f;
    
    boolean isSearching = false;
    
    JPanel itemsPanel;
    JTextField searchText;
    JTextField amountPaid;
    JLabel amountPaidLabel;
    JLabel balanceLabel;
    JLabel searchLabel;
    JLabel totalLabel;
    
    JXBusyLabel lblBusy;
    
    JTable table;
    DefaultTableModel tableModel;
    
    JScrollPane scrollPane;
    JScrollPane pane;
    
    GridBagConstraints gbc;
    
    ArrayList<RowItem> items;
    
    Font font = new Font(Font.SANS_SERIF,Font.PLAIN, 12);
    
    public Sales(){
        setFont(font);
        getItems();
        initComponents();
        appendTextListener(amountPaid);
        amountPaid.requestFocus();
        appendSearchTextListener(searchText);
    }
    
    
    private void initComponents(){
        setTitle("Sales");
        setSize(1000, 660);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setLayout(null);
        setLocation(WindowLocation.getCenterX(getWidth()),WindowLocation.getCenterY(getHeight()));
        
        
        searchLabel = new JLabel();
        searchLabel.setText("Search: ");
        searchLabel.setBounds(40, 100, 50, 20);        
        add(searchLabel);
        
        searchText = new JTextField();
        searchText.setBounds(100, 101, 500, 20);               
        add(searchText); 
        
        lblBusy = new JXBusyLabel();
        lblBusy.setBounds(610, 101, 26, 26);
        lblBusy.setVisible(false);
        add(lblBusy);
        
        itemsPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        itemsPanel.setLayout(layout);
        pane = new JScrollPane(itemsPanel);
        pane.setBounds(40,130,610,450);
        add(pane);
        
        table = new JTable();
        scrollPane = new JScrollPane();
        tableModel = new DefaultTableModel(
          new String[][]{},new String[]{"Item","Quantity","Price"}
        ){

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        tableModel.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
               try{
                 totalPrice = 0f;
                 for(int i = 0;i<tableModel.getRowCount();i++){  
                   float price = Float.parseFloat(tableModel.getValueAt(i, 2).toString());
                   totalPrice += price;
                   totalLabel.setText(String.format("Total: Ksh: %.2f", totalPrice));
                 }
               }catch(Exception ex){
                   ex.printStackTrace();
               }
            }
        });
        table.setModel(tableModel);    
        table.setRowMargin(3);
        
        //Key Event Listeners
        table.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if(table.getSelectedRows().length == 1){
                    if(e.getKeyCode() == KeyEvent.VK_DELETE){
                        tableModel.removeRow(table.getSelectedRow());
                        if(table.getRowCount() == 0){
                            totalLabel.setText("Total: Ksh: 0.00");
                        }
                    }
                    
                    /**float currentTotal = Float.parseFloat(tableModel.getValueAt(table.getSelectedRow(),2).toString());
                    int qty = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 1).toString());
                    //float unitPrice = currentTotal/qty;
                    
                    if(e.getKeyCode() == KeyEvent.VK_LEFT){
                        
                        if(qty > 1){
                            qty--;
                            tableModel.setValueAt(qty, table.getSelectedRow(),1);
                            //updateValues(qty, unitPrice);
                        }
                    }
                    
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT){                      
                        qty++;
                        tableModel.setValueAt(qty, table.getSelectedRow(),1);
                        //updateValues(qty, unitPrice);
                    }*/
                }
            }
            
        });
        
        
        scrollPane.setBounds(675, 130, 280, 360);
        scrollPane.setViewportView(table);
        add(scrollPane);
        
        amountPaidLabel = new JLabel();
        amountPaidLabel.setText("AmoutPaid: ");
        amountPaidLabel.setBounds(675,500,70,20);
        add(amountPaidLabel);
        
        amountPaid = new JTextField();
        amountPaid.addKeyListener(new KeyAdapter() {

            //Ensure the input value is a number
            @Override
            public void keyTyped(KeyEvent e) {
                char value = e.getKeyChar();
                if(!isNumber(value)){
                    e.consume();
                }
            } 

            @Override
            public void keyPressed(KeyEvent e) {
               if(e.getKeyCode() == KeyEvent.VK_ENTER){
                   saveData();
               }
            }
            
            
        
        });
        amountPaid.setBounds(745,500,210,20);        
        add(amountPaid);
        
        totalLabel = new JLabel();
        totalLabel.setText("Total: Ksh:0.00");
        totalLabel.setBounds(675,530,120,20);
        add(totalLabel);
        
        balanceLabel = new JLabel();
        balanceLabel.setText("Balance: Ksh:0.00");
        balanceLabel.setBounds(675,555,120,20);
        add(balanceLabel);
        
        populateAndArrange();
        
        //setVisible(true);
    }
    
    private void updateValues(int qty,float unitPrice){
        float total = unitPrice * qty;
        totalLabel.setText(String.format("Total: Ksh: %.2f", total));
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
                    float amtPaid = Float.parseFloat(amountPaid.getText());
                    balance = amtPaid - totalPrice;
                    balanceLabel.setText(String.format("Balance: Ksh: %.2f", balance));
                     
                 }catch(Exception ex){
                     balanceLabel.setText("Balance: Ksh: 0.00");
                 }
             }
             
         });
    }
    
    private void appendSearchTextListener(JTextField textField){
        
        textField.getDocument().addDocumentListener(new DocumentListener() {

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
                 if(!isSearching){  
                     try{                    

                         SwingWorker worker = new SwingWorker() {

                             @Override
                             protected Object doInBackground() throws Exception {

                                  if(textField.getText().length() > 0){

                                      isBusy(true);
                                      getItems(textField.getText());
                                      populateAndArrange();

                                  }else{
                                      isBusy(true);
                                      getItems();
                                      populateAndArrange();
                                  }
                                  return null;
                             }

                             @Override
                             protected void done() {
                                 super.done();
                                 isBusy(false);
                                 try{
                                 finalize();
                                 }catch(Throwable ex){
                                     //DO NOTHING
                                 }
                             }



                         };

                         worker.execute();

                     }catch(Exception ex){

                     }
             }
             }
             
         });
        
    }
    
    private void isBusy(boolean state){
        lblBusy.setVisible(state);
        lblBusy.setBusy(state);
        isSearching = state;
        searchText.setEditable(!state);
    }
    
    
    /**
     * Populates and arranges row items on to the items panel
     */
    private void populateAndArrange(){
        int row = 0;
        int column = 0;
        int count = 1;

        itemsPanel.removeAll();
        itemsPanel.repaint();

          for(RowItem rowItem : items){
                JPanel item = rowItem.createRowItem();
                gbc.gridx = row;
                gbc.gridy = column;
                itemsPanel.add(item,gbc);
                itemsPanel.repaint();
            if(count%4 == 0){
                row = 0;
                column++;
            }
            else{
              row++;
            } 

            count++;          
         }
    }
    
    /**
     * Fetches product items from data source
     */
    private void getItems(){
        
        Driver driver = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        items = new ArrayList<>();
        
        int id;
        String name,location;
        float price;
        
        try{
               driver = HotelDB.createDriver();
               conn = HotelDB.getDBConnection();
               String sql = "SELECT ProductID,ProductName,Price,ImageLocation FROM products";
               stmt = conn.createStatement();
               
               rs = stmt.executeQuery(sql);
               
               items.clear();
               
               while(rs.next()){
                   id = rs.getInt(1);
                   name = rs.getString(2);
                   price = rs.getFloat(3);
                   location = rs.getString(4);
                   
                   Image img = null;
                   
                   if(location != null && !("".equals(location))){
                      img =  new ImageIcon(location).getImage();
                   }else{
                      img = new ImageIcon(getClass().getResource("/hotel/Images/defsale100.png")).getImage();
                   }
                   items.add(new RowItem(id,name,price,img));
               }
               
               
           }catch(Exception ex){
              HError.logError(ex.getMessage());
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
        
    }
    
    private void getItems( String itemName){
        
        Driver driver = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        items = new ArrayList<>();
        
        int id;
        String name,location;
        float price;
        
        try{
               driver = HotelDB.createDriver();
               conn = HotelDB.getDBConnection();
               String sql = "SELECT ProductID,ProductName,Price,ImageLocation FROM products WHERE "
                       + "ProductName LIKE '%" + itemName + "%'";
               stmt = conn.createStatement();
               
               rs = stmt.executeQuery(sql);
               
               items.clear();
               
               while(rs.next()){
                   id = rs.getInt(1);
                   name = rs.getString(2);
                   price = rs.getFloat(3);
                   location = rs.getString(4);
                   Image img = null;
                   
                   if(location != null && !("".equals(location))){
                      img =  new ImageIcon(location).getImage();
                   }else{
                      img = new ImageIcon(getClass().getResource("/hotel/Images/defsale100.png")).getImage();
                   }

                   items.add(new RowItem(id,name,price,img));
               }
               
               
           }catch(Exception ex){
               HError.logError(ex.getMessage());
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
        
    }
    
    /**
     * Adds a single row item to the table.
     * @param itemName the name of the item.
     * @param qty the quantity of the item.
     * @param price the price of the item.
     */
    public void addToTable(String itemName,String qty,String price){
       

        Vector<String> rowData = new Vector<>();
        rowData.add(itemName);
        rowData.add(qty);
        rowData.add(price);
        
        tableModel.addRow(rowData);
    }
    
    private ArrayList getTableList(){
        ArrayList<String> items = new ArrayList<>();
        for(int i=0;i<table.getRowCount();i++){
            items.add(table.getValueAt(i, 0)+" x " + table.getValueAt(i, 1));
        }
        
        return items;
    }
    
    private void reset(){
        
        tableModel.setRowCount(0);

        amountPaid.setText("");
        totalLabel.setText("Total: Ksh:0.00");
    }
       
    private void saveData(){       
        
        try{
               
           HashMap<String,Object> sale = new HashMap<>();

           sale.put("SalesID",HotelDB.generateKey(HotelDB.SALES_TABLE,"SalesID"));
           sale.put("ItemList", String.join(",",getTableList()));
           sale.put("PaymentMode", "cash");
           sale.put("TotalCost", totalPrice);
           
           float paid = Float.parseFloat(amountPaid.getText());
           if(paid > totalPrice){
               paid = totalPrice;
           }
           
           sale.put("AmountPaid", paid);
           sale.put("ReceiptNo", HotelDB.generateKey(HotelDB.SALES_TABLE,"ReceiptNo"));
           sale.put("OrderNo", HotelDB.generateReducedKey(HotelDB.SALES_TABLE,"OrderNo"));
           sale.put("TimeOfSale", new SimpleDateFormat(HotelDB.getSQLiteDateTimeFormat()).format(HotelDB.getCurrentDateTime()));

           HotelDB.insertTODB(HotelDB.SALES_TABLE, sale);
           
           HashMap<String,Object> sbf = new HashMap<>();
           
            for(int i=0;i<table.getRowCount();i++){
                
                sbf.put("Product",table.getValueAt(i, 0).toString());
                sbf.put("DateSold",new SimpleDateFormat(HotelDB.getSQLiteDateFormat()).format(HotelDB.getCurrentDateTime()));
                sbf.put("Quantity",table.getValueAt(i, 1));
                
                int qty = Integer.parseInt(table.getValueAt(i, 1).toString());
                float total = Float.parseFloat(table.getValueAt(i, 2).toString());
                float ppu = total/qty;
                
                sbf.put("PPU",ppu);
                sbf.put("TotalPrice", total);
                
                HotelDB.insertTODB(HotelDB.SBF_TABLE, sbf);
            }

           JOptionPane.showMessageDialog(this,"Item(s) sold.","Sold",JOptionPane.INFORMATION_MESSAGE);

           reset();
               
           }catch(Exception ex){
               HError.showErrorMessage("SA001: Sorry an error occurred while saving your data.", null, ex.getMessage());
           }
    }
    
}
