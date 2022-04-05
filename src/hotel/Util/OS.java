/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

/**
 *
 * @author Vaud Keith
 */
public class OS {
    
    private static final String OS_TYPE = System.getProperty("os.name").toLowerCase();
    
    /**
     * Determines if the current operating system is windows.
     * @return a boolean 
     */
    public static boolean isWindows(){
        return (OS_TYPE.contains("win"));
    } 
    
    /**
     * Determines if the current operating system is Macintosh
     * @return a boolean 
     */
    public static boolean isMac(){
        return (OS_TYPE.contains("mac"));
    }
    
    /**
     * Determines if the current operating system is Linux.
     * @return a boolean
     */
    public static boolean isLinux(){
        return (OS_TYPE.contains("inx")) || (OS_TYPE.contains("nux")) || (OS_TYPE.contains("aix"));
    }
}
