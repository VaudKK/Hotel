/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.DB;


import hotel.Util.HError;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 *
 * @author Vaud Keith
 */

public class HotelDB {
    
    //private static final String JDBC_DRIVER = "org:sqlite:JDBC";
    //private static final String DB_URL = "jdbc:sqlite:" + DirectoryManager.getDbDirectory() + "\\hotel.db";
    private static final String DB_URL = "jdbc:sqlite:hotel.db";
    //private static final String URL = "jdbc:mysql://159.89.105.247:3306/hotel_db";
    
    public static final String PRODUCTS_TABLE = "products";
    public static final String EMPLOYEES_TABLE = "employees";
    public static final String SALES_TABLE = "sales";
    public static final String USERS_TABLE = "users";
    public static final String PURCHASES_TABLE = "purchases";
    public static final String SUPPLIERS_TABLE = "suppliers";
    public static final String DAILYUSE_TABLE = "dailyuse";
    public static final String SBF_TABLE = "sbf";
    public static final String LOGS_TABLE = "logs";
    public static final String STOCK_TABLE = "stock";
    
    
    public static void main(String[] args){
        try{
          //new HotelDB().createTables();
        }catch(Exception e){
           System.out.println(e.getMessage());
        } 
    }
    
    //<editor-fold defaultstate="collapsed" desc="Creates database tables">
    public void createTables() throws SQLException, FileNotFoundException, IOException{
        Driver sqliteDriver;
        Connection sqliteConn = null;
        
        sqliteDriver = new org.sqlite.JDBC();
        DriverManager.registerDriver(sqliteDriver);
        
        sqliteConn = getDBConnection();
        
        Reader reader = new FileReader(getClass().getResource("/hotel/DB/tables.sql").getPath());
        
        SQLScriptRunner runner = new SQLScriptRunner(sqliteConn, reader);
        
        runner.runScript();
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Deletes tables">
     private static void dropTable(String tableName) throws SQLException{
        Driver sqliteDriver;
        Connection sqliteConn;
        Statement stmt;
        
        //Register Driver;
        sqliteDriver = new org.sqlite.JDBC();
        DriverManager.registerDriver(sqliteDriver);
        
        sqliteConn = DriverManager.getConnection(DB_URL);
        stmt = sqliteConn.createStatement();
        
        
        String sql;

        sql = "DROP TABLE " + tableName;
        stmt.execute(sql);           
        
        stmt.close();
        sqliteConn.close();
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Inserts values into the database">
    /**
     * Inserts values to the database
     * @param tableName the name of the table
     * @param values a HashMap containing the Column names and the values to be inserted
     * @throws SQLException 
     */
    public static void insertTODB(String tableName,HashMap<String,Object> values) throws SQLException{
        
        Driver sqliteDriver = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        

            StringBuffer query = new StringBuffer();
            query.append("INSERT");
            query.append(" INTO ");
            query.append(tableName);
            query.append("(");
            
            sqliteDriver = new org.sqlite.JDBC();
            DriverManager.registerDriver(sqliteDriver);
            
            
            int size = (values != null && !values.isEmpty()) ? values.size() : 0;
            
            int i =0;
            if(size > 0){                
                
              for(String colName : values.keySet()){
                  query.append((i > 0) ? "," : "");
                  query.append(colName);
                  i++;
              }
            
            query.append(")");
            query.append(" VALUES (");
            
            for(i =0;i<size;i++){
              
                query.append((i>0) ? ",?" : "?");
                
            }
            
            query.append(");");
            
            conn = getDBConnection();
            
            pstmt = conn.prepareStatement(query.toString());
            
            int n = 1;
            
            for(Object value : values.values()){
                pstmt.setObject(n, value);
                n++;
            }
            
            pstmt.execute();
            
            pstmt.close();
            conn.close();
            
         }  
      
    }
    // </editor-fold>   
    
    //<editor-fold defaultstate="collapsed" desc="Updates records in the database">
    /**
     * Updates a row in a table.The last value added to the LinkedHashMap should be the
     * key value that helps determine a unique row in a database.
     * @param tableName the name of the table.
     * @param values the LinkedHashMap containing the data.
     * @throws SQLException 
     */
    public static void updateDB(String tableName,LinkedHashMap<String,Object> values) throws SQLException{
        
        Driver sqliteDriver = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        sqliteDriver = new org.sqlite.JDBC();
        DriverManager.registerDriver(sqliteDriver);
        
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE ");
        builder.append(tableName);
        builder.append(" SET ");
        
        int i = 0;
        int maxLength = values.entrySet().size();
        
        String keyField="";
        String keyValue="";
        
        
        for(String colName : values.keySet()){
            if(!(i == (maxLength - 1))){   
                builder.append((i > 0)? "," : "");
                builder.append(colName);
                builder.append(" = ?");
                i++;
            }else{
                keyField = colName;
                keyValue = values.get(colName).toString();
            }
        }
        
        builder.append(" WHERE ");
        builder.append(keyField);
        builder.append(" = '");
        builder.append(keyValue);
        builder.append("'");
        
        conn = getDBConnection();
            
        pstmt = conn.prepareStatement(builder.toString());

        i = 0;
        int n = 1;

        for(Object value : values.values()){
            if(!(i == (maxLength - 1))){
                pstmt.setObject(n, value);
                n++;
                i++;
            }
        }

        pstmt.execute();

        pstmt.close();
        conn.close();
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Logs application activities">
    public static void logActivity(String user,String activity) throws SQLException{
        
        HashMap<String,Object> log = new HashMap<>();
        log.put("Activity", activity);
        log.put("User", user);
        log.put("TimeLogged", new SimpleDateFormat(getSQLiteDateTimeFormat()).format(getCurrentDateTime()));
        
        insertTODB(LOGS_TABLE, log);
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="For testing and debugging purposes">
    public static void dbTests(){
        Driver sqliteDriver;
        Connection sqliteConn = null;
        Statement stmt = null;
        
        try{
            //Register Driver
            //sqliteDriver = new com.mysql.cj.jdbc.Driver();
            //DriverManager.registerDriver(sqliteDriver);
            
            //Connected to database
            //sqliteConn = DriverManager.getConnection(URL,"vaud","Vaud120,");
            
            
            //stmt = sqliteConn.createStatement();
            //java.sql.ResultSet rs = 
            //stmt.execute("UPDATE tbl_sales SET ModeOfPayment = 'MPESA' WHERE SalesID = 1");
             
            //while(rs.next()){
                //System.out.println(rs.getString(1));//+ " " + rs.getString(2) + " " +rs.getString(3) + " " + rs.getFloat(4)
                //+" " +rs.getFloat(5) + " " + rs.getInt(6)+" "+rs.getInt(7) +" "+rs.getString(8));// + " " 
                        //+ rs.getFloat(9) + " " + rs.getString(10));
            //}
            
            //stmt.close();
            //sqliteConn.close();
            
        }catch(Exception e){
            //For Debugging purposes
            System.out.println("Error :" +e.getMessage());
        }finally{
            try{
                if(stmt != null){
                    stmt.close();
                }
                if(sqliteConn != null){
                    sqliteConn.close();
                }
            }catch(Exception e){
                //DO NOTHING
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Helper Functions">
    
    /**
     * Gets the connection to the database
     * @return an SQLConnection
     * @throws SQLException 
     */
    public static Connection getDBConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }
    
   /**
    * Creates a JDBC driver.
    * @return an SQL Driver.
    * @throws SQLException 
    */
    public static Driver createDriver()throws SQLException{
        Driver driver = new org.sqlite.JDBC();        
        DriverManager.registerDriver(driver);
        return driver;
    }
    
    /**
     * Gets the current system date and time
     * @return date and time
     */
    public static Date getCurrentDateTime(){       
        return Calendar.getInstance().getTime();
    }
    
    /**
     * Gets the specified datetime format.
     * @return a String 
     * @deprecated  replaced by {@link  #getSQLiteDateTimeFormat() }
     */
    public static String getDateTimeFormat(){
        return "dd-MM-yyyy HH:mm:ss";
    }
    
    /**
     * Gets the specified datetime format as specified by the SQLite 
     * database format
     * @return a String 
     */
    public static String getSQLiteDateTimeFormat(){
        return "yyyy-MM-dd HH:mm:ss";
    }
    
    /**
     * Gets the specified date format.
     * @return a String 
     * @deprecated  replaced by {@link #getSQLiteDateFormat() }
     */
    public static String getDateFormat(){
        return "dd-MM-yyyy";
    }
    
    /**
     * Gets the specified date format as specified by the SQLite 
     * database format
     * @return a String 
     */
    public static String getSQLiteDateFormat(){
        return "yyyy-MM-dd";
    }
    
    
    /**
     * Checks if a specific row is available based on a unique value.
     * @param tableName the name of the table
     * @param columnName the name of the unique column.
     * @param value the unique value.
     * @param caseSensitive should the search be case sensitive or case insensitive.
     * @return true if row exists,false otherwise.
     * @throws SQLException 
     */
    public static boolean isRowAvailable(String tableName,String columnName,Object value,boolean caseSensitive) throws SQLException{
        
        Driver driver;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        boolean hasRow = false;
        
       driver = HotelDB.createDriver();
       conn = HotelDB.getDBConnection();
       
       String sql;
       
       if(caseSensitive){
            sql = "SELECT * FROM " + tableName + " WHERE " +columnName+ " = '"+value + "' LIMIT 1";
       }else{
             sql = "SELECT * FROM " + tableName + " WHERE " +columnName+ " = '"+value+"' COLLATE NOCASE LIMIT 1";
       }
       pstmt = conn.prepareStatement(sql);

       rs = pstmt.executeQuery();

       if(rs.next()){
           hasRow = true;
       }

       rs.close();
       pstmt.close();
       conn.close();
       
        return hasRow;
    }
    

    public static Object getValue(String tableName,String columnName,boolean caseSensitive) throws SQLException {
        
        Driver driver;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;        
        
       driver = HotelDB.createDriver();
       conn = HotelDB.getDBConnection();
       
       String sql;
       
       if(caseSensitive){   
            sql = "SELECT " + columnName +" FROM '" + tableName + "' LIMIT 1";
       }else{
             sql = "SELECT " + columnName + " COLLATE NOCASE  FROM '" + tableName + "' LIMIT 1";
       }
       pstmt = conn.prepareStatement(sql);

       rs = pstmt.executeQuery();

       Object val = null;
       
       if(rs.next()){
           val =  rs.getObject(1);
       }

       rs.close();
       pstmt.close();
       conn.close(); 
       
        return val;
    }
    
    /**
     * Generates a unique key for a row in a table.
     * @param tableName the table name
     * @param keyField the unique field name
     * @return an Integer
     * @throws SQLException 
     */
    public static int generateKey(String tableName,String keyField) throws SQLException{
        Random rand = new Random();
        
        int key = rand.nextInt(Integer.MAX_VALUE)+1;
        
        Driver sqliteDriver;
        Connection sqliteConn;
        Statement stmt;
        
        sqliteDriver = new org.sqlite.JDBC();
        DriverManager.registerDriver(sqliteDriver);

        sqliteConn = DriverManager.getConnection(DB_URL);

        String sql = "SELECT * FROM " + tableName + " WHERE " + keyField + " = '" + key + "' LIMIT 1";
        stmt = sqliteConn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);

        if(rs.next()){
            key = rand.nextInt(Integer.MAX_VALUE )+1;
            generateKey(tableName, keyField);
        }

        stmt.close();
        sqliteConn.close();          
        
        return key;
    }
    
    /**
     * Generates a unique key for a row in a table.
     * The key is of a shorter length.
     * @param tableName the table name
     * @param keyField the unique field name
     * @return an Integer
     * @throws SQLException 
     */
    public static int generateReducedKey(String tableName,String keyField) throws SQLException{
        Random rand = new Random();
        
        int key = rand.nextInt(9999)+1;
        
        Driver sqliteDriver;
        Connection sqliteConn;
        Statement stmt;
        
        sqliteDriver = new org.sqlite.JDBC();
        DriverManager.registerDriver(sqliteDriver);

        sqliteConn = DriverManager.getConnection(DB_URL);

        String sql = "SELECT * FROM " + tableName + " WHERE " + keyField + " = '" + key + "' AND "
                + "STRFTIME('%Y-%m-%d',TimeOfSale) = '"+ new SimpleDateFormat(HotelDB.getSQLiteDateFormat())
                        .format(Calendar.getInstance().getTime()) +"' LIMIT 1";
        stmt = sqliteConn.createStatement();
        java.sql.ResultSet rs = stmt.executeQuery(sql);

        if(rs.next()){
            key = rand.nextInt(Integer.MAX_VALUE )+1;
            generateReducedKey(tableName, keyField);
        }

        stmt.close();
        sqliteConn.close();          
        
        return key;
    }
    
    //</editor-fold>
    
}
