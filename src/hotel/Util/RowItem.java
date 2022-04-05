/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import hotel.GUI.JFQty;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Vaud Keith
 */
public class RowItem{
    
    //Color values
    final Color HRED = new Color(139,0,0);
    final Color HBROWN = new Color(102,51,0);
    final Color HBLUE = new Color(90,150,220);
    final Color HGRAY = new Color(96,96,96);
    final Color HGREEN =new Color(0,102,0);
    final Color HPURPLE = new Color(102,0,51);
    final Color HDARK_PINK = new Color(204,0,204);
    final Color HSEA_GREEN = new Color(0,153,153);
    
    final Image DEFAULT_IMAGE = new ImageIcon(getClass().getResource("/hotel/Images/defsale100.png")).getImage();
    
    String itemName;
    float price;
    int itemID;
    Image image;
   
    
    public RowItem(int itemID,String itemName,float price,Image image){
        this.itemID = itemID;
        this.itemName = itemName;
        this.price = price;
        if(image != null){
            this.image = image;
        }else{
            this.image = DEFAULT_IMAGE;
        }
    }
    
    public String getItemName(){
        return itemName;
    }
    
    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    
   public JPanel createRowItem(){
             
       Color[] colors = new Color[]{HBLUE,HBROWN,HRED,HGRAY,HGREEN,HPURPLE,HDARK_PINK,HSEA_GREEN};
       Font font = new Font(Font.SANS_SERIF,Font.PLAIN, 12);
       
       int colorValue = (int)(Math.random() * colors.length);
       
       JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout());
       panel.setBackground(colors[colorValue]);
       
       JLabel imageHolder = new JLabel();
       JLabel details = new JLabel();
       details.setFont(font);
       details.setForeground(new Color(255,255,255));
       
       
       if(itemName.length() > 10){
           String itemNameSub = itemName.substring(0, 8)+"...";
           String content = String.format(" %s  Ksh: %.0f",itemNameSub,price);
           details.setText(content); 
       }else{
          String content = String.format(" %s  Ksh: %.0f",itemName,price);
          details.setText(content); 
       }
       
       Image img = new ImageIcon(image).getImage();
       Image resizedImage = img.getScaledInstance(130, 100, Image.SCALE_SMOOTH);
       
       imageHolder.setIcon(new ImageIcon(resizedImage));
       
       panel.addMouseListener(new MouseListener() {

           @Override
           public void mouseClicked(MouseEvent e) {
              JFQty qty = new JFQty(getItemName(),getPrice());
              qty.setVisible(true);
           }

           @Override
           public void mousePressed(MouseEvent e) {
               
           }

           @Override
           public void mouseReleased(MouseEvent e) {
              
           }

           @Override
           public void mouseEntered(MouseEvent e) {

           }

           @Override
           public void mouseExited(MouseEvent e) {
               
           }
       });
       
       panel.add(imageHolder,BorderLayout.CENTER);
       panel.add(details,BorderLayout.SOUTH);
       
       return panel;
   }
    
}
