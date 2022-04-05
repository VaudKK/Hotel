/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.DB;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Vaud Keith
 */
public class SQLScriptRunner {
    
    private static final String DEFAULT_DELIMETER = ";";   
    
    private Connection connection;
    private String delimeter = DEFAULT_DELIMETER;
    private Reader reader;
    
    public SQLScriptRunner(Connection connection,Reader reader){
        this.connection = connection;
        this.reader = reader;
    }
    
    public void runScript() throws IOException, SQLException{

        StringBuffer command = null;
        String line = null;
        
        LineNumberReader lineReader = new LineNumberReader(reader);
        
        while((line = lineReader.readLine()) != null){
            if(command == null){
                command = new StringBuffer();
            }
            
            String trimmedLine = line.trim();
            
            if(trimmedLine.endsWith(getDelimeter())){
                command.append(line.substring(0,line.lastIndexOf(getDelimeter())));
                command.append(" ");
                
                Statement stmt = connection.createStatement();              
                
                boolean hasResults = false;
                
                hasResults = stmt.execute(command.toString());
                
                if(hasResults){
                    
                }
                
                command = null;
                stmt.close();
            }else{
                command.append(line);
                command.append(" ");
            }
            
        }
        
    }
    
    
    private String getDelimeter(){
        return delimeter;
    }
    
}
