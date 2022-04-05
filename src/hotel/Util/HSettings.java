/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 *
 * @author Vaud Keith
 */
public class HSettings {
    
    public static enum Key{
        HotelName,HotelPhone,HotelLocation,CounterNumber
    }
   
    private LinkedHashMap<Key,Object> map = null;
    
    public void setPrefrence(LinkedHashMap<Key,Object> map){
        this.map = map;
    }
    
    
    public void saveSettings() throws NullPointerException{
         try{
            Element settings  = new Element("Settings");
            Document doc = new Document(settings);

            for(Key key : map.keySet()){
                Element settingsType = new Element("Type");
                settingsType.setAttribute(new Attribute("Name",key.name()));

                Element settingsValue = new Element("Value");
                settingsValue.setText(map.get(key).toString());

                settingsType.addContent(settingsValue);

                doc.getRootElement().addContent(settingsType);
            
            }
            FileOutputStream fos = new FileOutputStream(DirectoryManager.getUserDirectory()+"\\pref.xml");
            
            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            
            xmlOutput.output(doc, fos);
            
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
}
