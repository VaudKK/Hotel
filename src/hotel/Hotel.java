/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel;

import hotel.Util.DirectoryManager;
import hotel.GUI.JFLogin;
import hotel.GUI.MainWindow;
import hotel.Util.HError;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Vaud Keith
 */
public class Hotel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try{
           javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        }catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            HError.showErrorMessage("Error loading look and feel", null,ex.getMessage());
        }

        DirectoryManager.createDirectories();
        try {
            new hotel.DB.HotelDB().createTables();
        } catch (SQLException ex) {
            HError.showErrorMessage("Database sql error", null,ex.getMessage());
        } catch (IOException ex) {
            HError.showErrorMessage("Database file error", null,ex.getMessage());
        }
  
        MainWindow main = new MainWindow();
        main.setVisible(true);
        
        
    }
    
}
