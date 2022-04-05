CREATE TABLE IF NOT EXISTS hotelprofile(
            HotelID VARCHAR PRIMARY KEY NOT NULL,
            HotelName VARCHAR(255) UNIQUE NOT NULL,
            HotelLocation VARCHAR(255) NOT NULL);

CREATE TABLE IF NOT EXISTS products(
            ProductID INT PRIMARY KEY NOT NULL,
            ProductName VARCHAR(50) UNIQUE NOT NULL,
            Price FLOAT NOT NULL,
            Allergens VARCHAR(200) NOT NULL,
            ImageLocation VARCHAR(300) NULL);

    
CREATE TABLE IF NOT EXISTS employees(
            NationalID INT PRIMARY KEY NOT NULL,
            EmployeeName VARCHAR(50) UNIQUE NOT NULL,
            DOB VARCHAR(30) NOT NULL,
            EmploymentDate VARCHAR(30) NOT NULL,
            Gender VARCHAR(10) NOT NULL,
            Phone VARCHAR(15) UNIQUE NOT NULL,
            Email VARCHAR(50) NOT NULL,
            Salary FLOAT NOT NULL,
            TerminationDate VARCHAR(30) NULL);
    
CREATE TABLE IF NOT EXISTS sales( 
               SalesID INT PRIMARY KEY NOT NULL, 
               ItemList VARCHAR(300) NOT NULL, 
               PaymentMode VARCHAR(20) NOT NULL, 
               TotalCost FLOAT NOT NULL, 
               AmountPaid FLOAT NOT NULL, 
               ReceiptNo INT UNIQUE NOT NULL, 
               OrderNo INT UNIQUE NOT NULL, 
               TimeOfSale VARCHAR(100) NOT NULL); 
    
 CREATE TABLE IF NOT EXISTS users( 
               Username VARCHAR(100) PRIMARY KEY NOT NULL, 
               Email VARCHAR(100)UNIQUE NOT NULL, 
               Password VARCHAR(200) NOT NULL, 
               AccessLevel VARCHAR(20) NOT NULL, 
               Status VARCHAR(50) NOT NULL DEFAULT 'Active', 
               DateCreated VARCHAR(100) NOT NULL, 
               DateTerminated VARCHAR(100) NULL); 
    
 CREATE TABLE IF NOT EXISTS purchases( 
               PID INT PRIMARY KEY NOT NULL, 
               Item VARCHAR(100) NOT NULL, 
               Supplier VARCHAR(100) NOT NULL, 
               Quantity INT NOT NULL, 
               Measurement VARCHAR(30) NOT NULL, 
               PPU FLOAT NOT NULL, 
               TotalPrice FLOAT NOT NULL, 
               DateOfPurchase VARCHAR(100) NOT NULL);
    
 CREATE TABLE IF NOT EXISTS suppliers( 
               SupplierID INT PRIMARY KEY NOT NULL, 
               SupplierName VARCHAR(100) UNIQUE NOT NULL, 
               PhoneNo VARCHAR(200) NOT NULL, 
               Company VARCHAR(20) NOT NULL); 
    
 CREATE TABLE IF NOT EXISTS dailyuse( 
               DateOfUse VARCHAR(100) NOT NULL, 
               Item VARCHAR(100) NOT NULL, 
               Quantity INT NOT NULL, 
               Measurement VARCHAR(50) NOT NULL); 
    
 CREATE TABLE IF NOT EXISTS sbf( 
               Product VARCHAR(100) NOT NULL, 
               DateSold VARCHAR(100) NOT NULL, 
               Quantity INT NOT NULL, 
               PPU FLOAT NOT NULL, 
               TotalPrice FLOAT NOT NULL); 
    
 CREATE TABLE IF NOT EXISTS stock( 
               Item VARCHAR(100) UNIQUE NOT NULL, 
               Quantity INTEGER NOT NULL, 
               Measurement VARCHAR(100) NOT NULL); 
    
 CREATE TABLE IF NOT EXISTS logs( 
               LogNo INTEGER PRIMARY KEY, 
               Activity VARCHAR(200) NOT NULL, 
               User VARCHAR(30) NOT NULL, 
               TimeLogged VARCHAR(30) NOT NULL); 
