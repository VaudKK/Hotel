/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author vaud keith
 */
public class HPrint implements Printable{

    private static Font printFont = new Font("Courier", Font.PLAIN, 20);
    static PrinterJob job;
    
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        
         if(pageIndex > 0 )
             return NO_SUCH_PAGE;
         
         
         Graphics2D g2 = (Graphics2D)graphics;
         g2.setFont(printFont);
         g2.setPaint(Color.black);
         g2.drawString("MAIN HEADER", 5, 5);
         return PAGE_EXISTS;
    }
      
      
      private void showPrintPreveiw(){
          
      }
      
    
}


	
