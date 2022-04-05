/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.awt.Toolkit;

/**
 *
 * @author Vaud Keith
 */
public class WindowLocation {
    
    /**
     * gets the center X position on the screen based on the window
     * width
     * @param width of the  Window
     * @return 
     */
    public static int getCenterX(int width){
        return (Toolkit.getDefaultToolkit().getScreenSize().width)/2 - (width/2);
    } 
    
    /**
     * gets the center Y position on the screen based on the window
     * height
     * @param height of the Window
     * @return 
     */
    public static int getCenterY(int height){
        return (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - (height/2);
    }
    
}
