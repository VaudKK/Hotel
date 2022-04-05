/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.io.File;

/**
 *
 * @author Vaud Keith
 */
public class DirectoryManager {
    
    /**
     * Creates default directories for the application.
     */
    public static void createDirectories(){    
        createErrorDirectory();
        createDbDirectory();
        createUserDirectory();
        createExportDirectory();
        createImageDirectory();        
    }
    
    /**
     * Creates the default Image directory
     */
    private static void createImageDirectory(){
       if(OS.isWindows()){
           try{
                File imageFolder = new File(System.getenv("APPDATA")+"\\VHotel\\Images");
                if(!imageFolder.exists()){
                    imageFolder.mkdirs();
                }
           }
           catch(NullPointerException | SecurityException e){
               //For Debugging purposes
               System.out.println(e.getMessage());
           }
       }
    } 
    
    /**
     * Creates the directory for error logs.
     */
    private static void createErrorDirectory(){
        if(OS.isWindows()){
           try{
                File imageFolder = new File(System.getenv("APPDATA")+"\\VHotel\\Error");
                if(!imageFolder.exists()){
                    imageFolder.mkdirs();
                }
           }
           catch(NullPointerException | SecurityException e){
               //For Debugging purposes
               System.out.println(e.getMessage());
           }
       }
    }
    
    /**
     * Creates the Data directory
     */
    private static void createDbDirectory(){
        if(OS.isWindows()){
           try{
                File imageFolder = new File(System.getenv("APPDATA")+"\\VHotel\\Data");
                if(!imageFolder.exists()){
                    imageFolder.mkdirs();
                }
           }
           catch(NullPointerException | SecurityException e){
               //For Debugging purposes
               System.out.println(e.getMessage());
           }
       }
    }
    
     /**
     * Creates the user directory
     */
    private static void createUserDirectory(){
        if(OS.isWindows()){
           try{
                File imageFolder = new File(System.getenv("APPDATA")+"\\VHotel\\Preferences");
                if(!imageFolder.exists()){
                    imageFolder.mkdirs();
                }
           }
           catch(NullPointerException | SecurityException e){
               //For Debugging purposes
               System.out.println(e.getMessage());
           }
       }
    }
    
    private static void createExportDirectory(){
        if(OS.isWindows()){
            try{
                File exportFolder = new File(System.getenv("APPDATA")+"\\VHotel\\Exports");
                if(!exportFolder.exists()){
                    exportFolder.mkdirs();
                }
            }
            catch(NullPointerException | SecurityException e){
                //For Debuggin purposes
                System.out.println(e.getMessage());
            }
        }
    }
    

    public static String getImageDirectory(){
        return new File(System.getenv("APPDATA")+"\\VHotel\\Images").getAbsolutePath();
    }
    
    public static String getDbDirectory(){
        return new File(System.getenv("APPDATA")+"\\VHotel\\Data").getAbsolutePath();
    }
    
    public static String getErrorDirectory(){
        return new File(System.getenv("APPDATA")+"\\VHotel\\Error").getAbsolutePath();
    }
    
    public static String getUserDirectory(){
        return new File(System.getenv("APPDATA")+"\\VHotel\\Preferences").getAbsolutePath();
    }
    
    public static String getExportDirectory(){
        return new File(System.getenv("APPDATA")+"\\VHotel\\Exports").getAbsolutePath();
    }
    
    
}
