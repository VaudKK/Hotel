/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import hotel.DB.HotelDB;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Vaud Keith
 */
public class HError{
    
    /**
     * Shows an error message.
     * @param Message the message to be displayed.
     * @param frame the parent frame of the message dialog.
     * @param exMessage the exception that is thrown.
     */
    public static void showErrorMessage(String Message,JFrame frame,String exMessage){
        JOptionPane.showMessageDialog(frame,Message,"Error",JOptionPane.ERROR_MESSAGE);
        logError(exMessage);
    }
    
    /**
     * Shows an error message.
     * @param Message the message to be displayed.
     * @param frame the parent frame of the message dialog.
     */
    public static void showErrorMessage(String Message,JFrame frame){
        JOptionPane.showMessageDialog(frame,Message,"Error",JOptionPane.ERROR_MESSAGE);
    }
    
    public static void logError(String err){           
     try{
        String error = "\r\n\r\n " + HotelDB.getCurrentDateTime().toString() + " : " + err;
        FileOutputStream fos = new FileOutputStream(DirectoryManager.getErrorDirectory()+"\\error.txt",true);
        byte[] errbytes = error.getBytes();
        fos.write(errbytes);
        fos.close();
     }
     catch(FileNotFoundException ex){
         //DO NOTHING
     }catch(IOException ex){
         //DO NOTHING
     }
    }    
}
