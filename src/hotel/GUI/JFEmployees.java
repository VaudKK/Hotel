/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.GUI;

import hotel.DB.HotelDB;
import hotel.Util.DirectoryManager;
import hotel.Util.HError;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


/**
 *
 * @author Vaud Keith
 */
public class JFEmployees extends javax.swing.JDialog {

    /**
     * Creates new form JFEmployees
     */
    
    String key ="";
    private String lastDirectory = null;
    private static final Pattern VALID_EMAIL_REGEX = 
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    public JFEmployees() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        setTitle("Employees");
    }
    
    private void clearFields(){
        for(Component c : this.getRootPane().getContentPane().getComponents()){
            if(c instanceof JTextField){
                ((JTextField)c).setText("");
            }
        }
        
        txtNationalID.requestFocus();
        txtNationalID.setEditable(true);
        datePicker.setDate(null);
        empDate.setDate(null);
        btnEdit.setText("Edit");
        btnSave.setEnabled(true);
    }
    
        /**
     * Determines if a character is a whole number.
     * @param input the character to be evaluated.
     * @return true if the character is  a number,false otherwise.
     */
    private boolean isNumber(char input){
        
        boolean isNum = false;
        
        char[] numbers = "1234567890".toCharArray();
        for(int i = 0;i<numbers.length;i++){
            if(input == numbers[i]){
                return true;
            }
        }
        
        return isNum;
    }
    
    
    private static boolean isEmailValid(String emailAddr){
        Matcher matcher = VALID_EMAIL_REGEX.matcher(emailAddr);
        return matcher.find();
    }
    
 
     private boolean hasRow(String columnName,String value){       
        boolean hasRow = false;
        try {
            hasRow = HotelDB.isRowAvailable(HotelDB.EMPLOYEES_TABLE, columnName, value, false);
        } catch (SQLException ex) {
            HError.showErrorMessage("Error fetching data", null, ex.getMessage());
        }
        return hasRow;
    }
     
     public void getValuesToEdit(String id,String name,String dob,String empdate,String gender,String phone,
             String mail,String sal){
        key = id;
        txtNationalID.setText(id);
        txtName.setText(name);
        
        try {
            Date date = new SimpleDateFormat(HotelDB.getSQLiteDateFormat()).parse(dob);
            Date emp = new SimpleDateFormat(HotelDB.getSQLiteDateFormat()).parse(empdate);
            datePicker.setDate(date);
            empDate.setDate(emp);
        } catch (ParseException ex) {
            HError.showErrorMessage("Error parsing data", null, ex.getMessage());
        }
        
        cboGender.setSelectedItem(gender);
        txtPhone.setText(phone);
        txtMail.setText(mail);
        txtSalary.setText(sal);
        btnEdit.setText("Update");
        btnSave.setEnabled(false);
        txtNationalID.setEditable(false);
    }
    
    private boolean isTextFieldEmpty(){
        
        boolean isEmpty = false;
        
        for(Component c : this.getRootPane().getContentPane().getComponents()){
            if(c instanceof JTextField){
                if(((JTextField)c).getText().length() == 0){
                    isEmpty = true;
                }
            }
        }
        
        
        return isEmpty;
    }
    
    private void copyFile(String source,String destination){
        
        try{
            //Create an inputStream
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File(source)));
            //Create an outputStream
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(new File(destination)));       
        int r;
        //Write to the file
        while((r = input.read()) != -1){
            output.write((byte)r);
        }
        
        input.close();
        output.close();
        
        }catch(FileNotFoundException ex){
             HError.showErrorMessage("Could not find file", null, ex.getMessage());
        }catch(IOException e){
             HError.showErrorMessage("Error copying file", null, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtNationalID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtMail = new javax.swing.JTextField();
        txtSalary = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        cboGender = new javax.swing.JComboBox();
        datePicker = new org.jdesktop.swingx.JXDatePicker();
        empDate = new org.jdesktop.swingx.JXDatePicker();
        picBox = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnAddImage = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Name:");

        txtNationalID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNationalIDKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("National IDNO:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("DOB:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("EmploymentDate:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Gender");

        txtPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPhoneKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Phone Number:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Email:");

        txtSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSalaryKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Salary:");

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnView.setText("Table View");
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });

        cboGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        picBox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Picture:");

        btnAddImage.setText("Add Image");
        btnAddImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddImageActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtMail, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSalary, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPhone, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboGender, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(empDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(17, 17, 17))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName)
                            .addComponent(txtNationalID)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(picBox, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEdit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnView, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemove, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(47, 47, 47))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnAddImage, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRemove)
                                .addGap(0, 67, Short.MAX_VALUE))
                            .addComponent(picBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNationalID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(empDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnView)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        
        if(!isTextFieldEmpty()){            
            
            String name = txtName.getText();
            String natID = txtNationalID.getText();
            String gender = cboGender.getSelectedItem().toString();
            String phone = txtPhone.getText();
            String mail = txtMail.getText();
            String salary = txtSalary.getText();
            
            if(!isEmailValid(mail)){
                HError.showErrorMessage("Wrong email format.Expected format is: \nexample@gmail.com", null);
                txtMail.requestFocus();
                return;
            }           
            
                if(hasRow("EmployeeName", name)){
                     HError.showErrorMessage("Employee name already exists.", null);
                    txtName.requestFocus();
                    return;
                }else{
                    if(hasRow("NationalID", natID)){
                        HError.showErrorMessage("National ID already exists.", null);
                        txtNationalID.requestFocus();
                        return; 
                    }else{
                        if(hasRow("Phone", phone)){
                             HError.showErrorMessage("Phone number already exists.", null);
                            txtPhone.requestFocus();
                            return; 
                        }else{
                            if(hasRow("Email", mail)){
                                HError.showErrorMessage("Email already exists.", null);
                                txtMail.requestFocus();
                                return;
                            }else{
                                 DateFormat dateFormat = new SimpleDateFormat(HotelDB.getSQLiteDateFormat());
            
                                try{

                                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                                    HashMap<String,Object> employee  = new HashMap<>();
                                    employee.put("NationalID", Integer.parseInt(natID));   
                                    employee.put("EmployeeName", name);                                               
                                    employee.put("DOB", dateFormat.format(datePicker.getDate()));
                                    employee.put("EmploymentDate", dateFormat.format(empDate.getDate()));
                                    employee.put("Gender", gender);
                                    employee.put("Phone", phone);
                                    employee.put("Email", mail);
                                    employee.put("Salary", Float.parseFloat(salary));

                                    HotelDB.insertTODB(HotelDB.EMPLOYEES_TABLE, employee);

                                    JOptionPane.showMessageDialog(this,name + " has been added successfully.","Saved",JOptionPane.INFORMATION_MESSAGE);

                                    clearFields();
                                    txtNationalID.requestFocus();

                                }catch(Exception ex){
                                    HError.showErrorMessage("Error occurred when saving.Data not saved", null, ex.getMessage());
                                }finally{
                                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                                }
                            }
                        }
                    }
                }
        
        }else{
            JOptionPane.showMessageDialog(this,"Please fill all available fields.","Empty Field(s)",JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if(btnEdit.getText().equalsIgnoreCase("edit")){
            JFEmployeesView view = new JFEmployeesView("edit");
            view.setVisible(true);
        }else{
            if(btnEdit.getText().equalsIgnoreCase("update")){
                if(!isTextFieldEmpty()){            
            
            String name = txtName.getText();
            String natID = txtNationalID.getText();
            String gender = cboGender.getSelectedItem().toString();
            String phone = txtPhone.getText();
            String mail = txtMail.getText();
            String salary = txtSalary.getText();
            
           DateFormat dateFormat = new SimpleDateFormat(HotelDB.getSQLiteDateFormat());
            
            try{

                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                LinkedHashMap<String,Object> employee  = new LinkedHashMap<>();
 
                employee.put("EmployeeName", name);                                               
                employee.put("DOB", dateFormat.format(datePicker.getDate()));
                employee.put("EmploymentDate", dateFormat.format(empDate.getDate()));
                employee.put("Gender", gender);
                employee.put("Phone", phone);
                employee.put("Email", mail);
                employee.put("Salary", Float.parseFloat(salary));                                   
                employee.put("NationalID", Integer.parseInt(natID));  

                HotelDB.updateDB(HotelDB.EMPLOYEES_TABLE, employee);

                JOptionPane.showMessageDialog(this,name + " has been updated successfully.","update",JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                txtNationalID.requestFocus();

            }catch(Exception ex){
                 HError.showErrorMessage("Error occurred when saving.Data not saved", null, ex.getMessage());
            }finally{
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        
        }else{
            JOptionPane.showMessageDialog(this,"Please fill all available fields.","Empty Field(s)",JOptionPane.INFORMATION_MESSAGE);
        }
            }
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        JFEmployeesView view = new JFEmployeesView("view");
        view.setVisible(true);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtNationalIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNationalIDKeyTyped
       if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtNationalIDKeyTyped

    private void txtPhoneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPhoneKeyTyped
       if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtPhoneKeyTyped

    private void txtSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalaryKeyTyped
        if(!isNumber(evt.getKeyChar())){
            evt.consume();
        }
    }//GEN-LAST:event_txtSalaryKeyTyped

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
       clearFields();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAddImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddImageActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setDialogTitle("Select Image");

        String[] fileExtension = new String[]{".jpg",".jpeg",".png"};
        fileChooser.getFileSystemView().getDefaultDirectory();
        fileChooser.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                if(f.isDirectory()){return true;}
                
                String fileName = f.getName().toLowerCase();
                
                for(String extension: fileExtension){
                    if(fileName.endsWith(extension)){
                        return true;
                    }
                }                          
                
                return false;
            }

            @Override
            public String getDescription() {
               return "Image Files(*.jpg,*.jpeg.*.png)";
            }
        });

        fileChooser.setAcceptAllFileFilterUsed(false);

        if(lastDirectory != null){
            File lastDir = new File(lastDirectory);
            if(lastDir.isDirectory()){
                fileChooser.setCurrentDirectory(lastDir);
            }
        }

        int option = fileChooser.showDialog(this,"Set Image");

        if(option == JFileChooser.APPROVE_OPTION){
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try{
                String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                lastDirectory = fileChooser.getSelectedFile().getParentFile().toString();
                String selectedFileName = fileChooser.getSelectedFile().getName();

                //Copy the image to the directories folder
                Image empImage = new ImageIcon(fileChooser.getSelectedFile().getAbsolutePath()).getImage();
                //copyFile(selectedFile,DirectoryManager.getImageDirectory()+"\\"+selectedFileName);
                    //ImagePath = DirectoryManager.getImageDirectory()+"\\"+selectedFileName;

                    //Resize the image to fit the dimensions of the pictureBox
                    Image resizedImage = empImage.getScaledInstance(picBox.getWidth(), picBox.getHeight(),Image.SCALE_SMOOTH);
                    picBox.setIcon(new ImageIcon(resizedImage));
                }catch(Exception e){
                     HError.showErrorMessage("Sorry an error occurred when fetching the Image", null, e.getMessage());
                }finally{
                    setCursor(Cursor.getDefaultCursor());
                }
            }
    }//GEN-LAST:event_btnAddImageActionPerformed

    
    
    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        picBox.setIcon(null);
    }//GEN-LAST:event_btnRemoveActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFEmployees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFEmployees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddImage;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox cboGender;
    private org.jdesktop.swingx.JXDatePicker datePicker;
    private org.jdesktop.swingx.JXDatePicker empDate;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel picBox;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtNationalID;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSalary;
    // End of variables declaration//GEN-END:variables
}
