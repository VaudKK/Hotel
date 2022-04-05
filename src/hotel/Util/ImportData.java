/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.Util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Vaud Keith
 */
public class ImportData {
    
    public static DefaultTableModel readFromFile(FileInputStream file,int sheet,Vector<String>columnNames) throws IOException,NullPointerException{
        
        XSSFWorkbook workBook = new XSSFWorkbook(file);               
        
        Sheet workSheet = workBook.getSheetAt(sheet);
        
        Iterator<Row> rowIterator = workSheet.iterator();
        
        Vector<Vector<Object>> data = new Vector<>();
        
        while(rowIterator.hasNext()){
            
            Row row = rowIterator.next();
            Vector<Object> dataObj = new Vector<>();
            
            Iterator<Cell> cellIterator = row.cellIterator();
            
            while(cellIterator.hasNext()){
                
                Cell cell = cellIterator.next();
                
                dataObj.add(cell.getStringCellValue());
            }
            
            data.add(dataObj);
            
        }
        
        file.close();
        
        DefaultTableModel model= new DefaultTableModel(data,columnNames);
        
        return model;
    }
    
}
