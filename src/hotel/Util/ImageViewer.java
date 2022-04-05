/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

/**
 *
 * @author Vaud Keith
 */
public class ImageViewer extends JPanel{
    
    private Image image;
    private int width = 0;
    private int height = 0;
    
    /**
     * Default constructor
     * @param image the image to be placed into the viewer
     */
    public ImageViewer(Image image){
        this.image = image;
    } 
    
    public ImageViewer(Image image,int width,int height){
        this.image = image;
        this.width = width;
        this.height = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(image != null){
            if(width != 0 && height != 0){
                 g.drawImage(image,0,0,width,height, this);
            }else{
                g.drawImage(image,0,0,getWidth(),getHeight(), this);
            }
        }
    }
    
}
