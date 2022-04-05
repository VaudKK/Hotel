/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.Util.ImageViewer;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Vaud Keith
 */
public class MainWindow extends JFrame implements WindowListener{
    
   public MainWindow(){    
       
       initComponents();
       
       //TODO CATCH FILE ERRORS
       Image backgroundImage = new ImageIcon(getClass().getResource("/hotel/Images/delicious11.png")).getImage();
       add(new ImageViewer(backgroundImage));
   }
   
   private  void initComponents(){
       
       setTitle("Hotel");
       setSize(new Dimension(900,600));
       setMinimumSize(new Dimension(500,400));
       setLocationRelativeTo(null);
       setExtendedState(MAXIMIZED_BOTH);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
       
       //Menu
        JMenuBar menuBar = new JMenuBar();
   
        JMenu createMenu = new JMenu();
        JMenu salesMenu = new JMenu();
        JMenu supplyMenu = new JMenu();
        JMenu usageMenu = new JMenu();
        JMenu viewMenu = new JMenu();
        JMenu settingsMenu = new JMenu();
   
        //Create
        JMenuItem productMenuItem = new JMenuItem();
        JMenuItem employeeMenuItem = new JMenuItem();
        JMenuItem createUserMenuItem = new JMenuItem();
        
        //Supply
        JMenuItem newSupplyMenuItem = new JMenuItem();
        JMenuItem viewSupplyMenuItem = new JMenuItem();
        JMenuItem viewStock = new JMenuItem();
        
        //Sales
        JMenuItem salesMenuItem = new JMenuItem();
        JMenuItem salesByFood = new JMenuItem();
        JMenuItem salesViewMenuItem = new JMenuItem();
        
        //View
        JMenuItem viewEmployees = new JMenuItem();
        JMenuItem viewProducts = new JMenuItem();
        JMenuItem logs = new JMenuItem();
        
        //Usage
        JMenuItem usageMenuItem = new JMenuItem();
        JMenuItem viewUsageMenuItem = new JMenuItem();
        
        //Settings
        JMenuItem appSettingsMenuItem = new JMenuItem();
        JMenuItem changePassMenuItem = new JMenuItem();
        
        createMenu.setText("Create");
        
        productMenuItem.setText("Products");
        productMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        productMenuItem.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
              JFProduct product = new JFProduct();
              product.setVisible(true);
           }
        });
        createMenu.add(productMenuItem);
        
        employeeMenuItem.setText("Employees");
        employeeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        employeeMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
               JFEmployees emp = new JFEmployees();
               emp.setVisible(true);
           }
       });
        createMenu.add(employeeMenuItem);
             
        
        createUserMenuItem.setText("User");
        createUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        createUserMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
               JFCreateUsers users = new JFCreateUsers();
               users.setVisible(true);
           }
       });
        
        createMenu.add(createUserMenuItem);
                
        
        menuBar.add(createMenu);
        
        salesMenu.setText("Sales");
        
        //Sales
        salesMenuItem.setText("Make A Sale");
        salesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,InputEvent.ALT_MASK));
        salesMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              Sales sale = new Sales();
              sale.setVisible(true);
              setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
           }
       });
        
        salesMenu.add(salesMenuItem);
        
        salesByFood.setText("View Sales By Product");
        salesByFood.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFSalesByFoodView sale = new JFSalesByFoodView();
              sale.setVisible(true);
           }
       });
        
        salesMenu.add(salesByFood);
        
        salesViewMenuItem.setText("View All Sales");
        salesViewMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFSalesView sale = new JFSalesView();
              sale.setVisible(true);
           }
       });
        
        salesMenu.add(salesViewMenuItem);
        
        menuBar.add(salesMenu);
        
        supplyMenu.setText("Supply");
        
        //Supply
        newSupplyMenuItem.setText("New Supply");
        newSupplyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.ALT_MASK | InputEvent.CTRL_MASK));
        newSupplyMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFSupply supply = new JFSupply();
              supply.setVisible(true);
           }
       });
        
        supplyMenu.add(newSupplyMenuItem);
        
        viewSupplyMenuItem.setText("View Supplies");     
        viewSupplyMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFSupplyView supply = new JFSupplyView();
              supply.setVisible(true);
           }
       });
        
        supplyMenu.add(viewSupplyMenuItem);
        
        viewStock.setText("View Stock");
        viewStock.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFStock stock = new JFStock();
              stock.setVisible(true);
           }
       });
        
        supplyMenu.add(viewStock);
        
        menuBar.add(supplyMenu);
        
        //Usage
        
        usageMenu.setText("Usage");
        
        usageMenuItem.setText("Add New");
        usageMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFUse use = new JFUse();
              use.setVisible(true);
           }
       });
        
        usageMenu.add(usageMenuItem);
        
        
        viewUsageMenuItem.setText("View Uses");
        viewUsageMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFUseView useview = new JFUseView();
              useview.setVisible(true);
           }
       });
        
        usageMenu.add(viewUsageMenuItem);
        
        menuBar.add(usageMenu);
        
        //View
        viewMenu.setText("View");
        
        viewEmployees.setText("View Employees");
        viewEmployees.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFEmployeesView empView = new JFEmployeesView("view");
              empView.setVisible(true);
           }
       });
        
        viewMenu.add(viewEmployees);
        
        viewProducts.setText("View Products");
        viewProducts.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFProductsView view = new JFProductsView("view");
              view.setVisible(true);
           }
       });
        
        viewMenu.add(viewProducts);
        
        logs.setText("View Logs");
        logs.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFLogs log = new JFLogs();
              log.setVisible(true);
           }
       });
        
        viewMenu.add(logs);
        
        menuBar.add(viewMenu);
        
        //Settings
        settingsMenu.setText("Settings");
        
        appSettingsMenuItem.setText("Application Settings");
        appSettingsMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              JFSettings set = new JFSettings();
              set.setVisible(true);
           }
       });
        
        settingsMenu.add(appSettingsMenuItem);
        
        changePassMenuItem.setText("Change Password");
        changePassMenuItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
             
           }
       });
        
        settingsMenu.add(changePassMenuItem);
        
        menuBar.add(settingsMenu);
       
       setJMenuBar(menuBar);
       
       addWindowListener(this);
       setVisible(true);
   } 
   

    @Override
    public void windowOpened(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        /*Window[] windows = JFrame.getWindows();
        
        for(Window win : windows){
            if(win.getClass().toString().contains("hotel.GUI.JFLogin")){
                win.setVisible(true);
            }
        }*/
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
       //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //To change body of generated methods, choose Tools | Templates.
    }
}
