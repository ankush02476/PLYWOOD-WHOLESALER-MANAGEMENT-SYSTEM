import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.print.PrinterJob;
import java.time.LocalDate;


public class EmployeePage extends JPanel {

    function fn = new function();
     private AccountsPage accountsPage;

    JTextField empID, empName, phone, address, joiningDate, salary, status, txtsearch;
    JComboBox<String> paymentType, cmbsearch;

    JButton search, btnprint;

    JTable table;
    DefaultTableModel model;

    public EmployeePage(AccountsPage accountsPage) {
        this.accountsPage = accountsPage;

        setLayout(new BorderLayout());
        setBackground(Color.white);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(15,0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60,60,60,60));
        mainPanel.setBackground(new Color(242,230,213));

        fn.createLabel(mainPanel,"EMPLOYEE MODULE",65,30,16);
        fn.createLabel(mainPanel,"EMPLOYEE LIST",400,30,16);
        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);

        cmbsearch = new JComboBox<>(new String[]{
                "EMPLOYEE ID","EMPLOYEE NAME","PHONE","PAYMENT TYPE","STATUS"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);

        txtsearch = fn.createTextField(mainPanel,"",970,30);
        txtsearch.setBounds(970,30,160,28);

        search = fn.createRoundButton(mainPanel,"SEARCH",1140,30);
        fn.addHoverEffect(search,new Color(10,10,10),new Color(230,160,160));
        search.setBounds(1140,30,160,28);
        mainPanel.add(search);
        search.addActionListener(e -> searchEmployee());

        // FORM PANEL
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(320,0));
        formPanel.setBackground(new Color(250,249,246));

        int y = 20;

        empID = fn.createTextField(formPanel,"",20,30);
        fn.createLabel(formPanel,"Employee ID:",20,y-20,14);
        empID.setEditable(false);
        empID.setFocusable(false);

        y += 70;
        empName = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Employee Name:",20,y-30,14);

        y += 60;
        phone = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Phone No:",20,y-30,14);

        y += 60;
        address = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Address:",20,y-30,14);

        y += 60;
        joiningDate = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Joining Date:",20,y-30,14);
        joiningDate.setText(LocalDate.now().toString());
        joiningDate.setEditable(false);

        y += 60;
        salary = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Salary:",20,y-30,14);

        y += 60;
        paymentType = new JComboBox<>(new String[]{" ","DAILY","MONTHLY"});
        paymentType.setBounds(20,y,280,30);
        formPanel.add(paymentType);
        fn.createLabel(formPanel,"Payment Type:",20,y-30,14);

        y += 60;
        status = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Status:",20,y-30,14);
        status.setText("ACTIVE");

        // BUTTONS
        y += 60;
        JButton btnAdd = fn.createRoundButton(formPanel,"ADD",20,y+10);
        JButton btnUpdate = fn.createRoundButton(formPanel,"UPDATE",20,y+50);
        JButton btnDelete = fn.createRoundButton(formPanel,"DELETE",20,y+90);

        fn.addHoverEffect(btnAdd,new Color(10,10,10),new Color(230,160,160));
        fn.addHoverEffect(btnUpdate,new Color(10,10,10),new Color(230,160,160));
        fn.addHoverEffect(btnDelete,new Color(10,10,10),new Color(230,160,160));

        btnAdd.addActionListener(e -> insertEmployee());
        btnUpdate.addActionListener(e -> updateEmployee());
        btnDelete.addActionListener(e -> deleteEmployee());

        // TABLE PANEL
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(
                new String[]{"Employee ID","Employee Name","Phone","Address",
                        "Joining Date","Salary","Payment Type","Status"},0
        );

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250,249,246));
        header.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,12));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        // PRINT BUTTON
        btnprint = new JButton("PRINT");
        btnprint.setForeground(Color.WHITE);
        btnprint.setPreferredSize(new Dimension(100,40));
        fn.addHoverEffect(btnprint,new Color(10,10,10),new Color(230,160,160));
        btnprint.addActionListener(e -> printEmployee());

        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printPanel.setBackground(Color.WHITE);
        printPanel.add(btnprint);
        tablePanel.add(printPanel,BorderLayout.SOUTH);

        // TABLE CLICK
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = table.getSelectedRow();
                if(row>=0){
                    empID.setText(model.getValueAt(row,0).toString());
                    empName.setText(model.getValueAt(row,1).toString());
                    phone.setText(model.getValueAt(row,2).toString());
                    address.setText(model.getValueAt(row,3).toString());
                    joiningDate.setText(model.getValueAt(row,4).toString());
                    salary.setText(model.getValueAt(row,5).toString());
                    paymentType.setSelectedItem(model.getValueAt(row,6).toString());
                    status.setText(model.getValueAt(row,7).toString());
                }
            }
        });

        mainPanel.add(formPanel,BorderLayout.WEST);
        mainPanel.add(tablePanel,BorderLayout.CENTER);
        add(mainPanel,BorderLayout.CENTER);

        loadTable();
    }

    // PRINT METHOD
   private void printEmployee(){

    JDialog processing = new JDialog((Frame)null,"Printing",true);
    processing.add(new JLabel("Processing... Please wait",JLabel.CENTER));
    processing.setSize(250,100);
    processing.setLocationRelativeTo(this);

    SwingWorker<Void,Void> worker = new SwingWorker<>() {

        protected Void doInBackground() throws Exception {

            PrintableInvoice invoice = new PrintableInvoice(
                    "JAI MAA JAGDAMBA PLY",
                    "KHANDERAN SINGH MARG, NEAR SAHITYA SAMMELAN,KADAMKUAN,PATNA-3",
                    "PHONE NO:9122678054",
                    table,
                    "EMPLOYEE LIST"   // ✅ FIX ADDED
            );

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(invoice);

            if(job.printDialog()) job.print();

            return null;
        }

        protected void done(){
            processing.dispose();
            JOptionPane.showMessageDialog(
                    EmployeePage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}

    // INSERT
    private void insertEmployee() {
    if(empName.getText().trim().isEmpty() || phone.getText().trim().isEmpty() ||
       salary.getText().trim().isEmpty() || paymentType.getSelectedIndex()==0){
        JOptionPane.showMessageDialog(this,"PLEASE ENTER ALL DETAILS");
        return;
    }

    try {
        Connection con = DB.getConnection();

      
        PreparedStatement ps = con.prepareStatement(
                "INSERT INTO EMPLOYEE (EMPLOYEE_NAME,PHONE_NO,ADDRESS,JOINING_DATE,SALARY,PAYMENT_TYPE,STATUS) " +
                "VALUES (?,?,?,?,?,?,?)", new String[]{"EMPLOYEE_ID"} // this tells JDBC to return generated keys
        );

        ps.setString(1, empName.getText());
        ps.setString(2, phone.getText());
        ps.setString(3, address.getText());
        ps.setDate(4, java.sql.Date.valueOf(joiningDate.getText()));
        ps.setDouble(5, Double.parseDouble(salary.getText()));
        ps.setString(6, paymentType.getSelectedItem().toString());
        ps.setString(7, status.getText());

        ps.executeUpdate();

        // GET GENERATED KEY
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            empID.setText(rs.getString(1)); // <-- this gets EMPID00001 style ID
        }

        rs.close();
        ps.close();
        con.close();

        loadTable();
        clearFields();

        if(accountsPage != null){
    accountsPage.loadTableData();
}
        JOptionPane.showMessageDialog(this,"EMPLOYEE ADDED SUCCESSFULLY");

    } catch(Exception e){
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,"ERROR ADDING EMPLOYEE");
    }
}
    // UPDATE
    private void updateEmployee() {
        if(empID.getText().isEmpty()) return;

        try{
            Connection con = DB.getConnection();
            String sql = "UPDATE EMPLOYEE SET EMPLOYEE_NAME=?,PHONE_NO=?,ADDRESS=?,JOINING_DATE=?,SALARY=?,PAYMENT_TYPE=?,STATUS=? WHERE EMPLOYEE_ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empName.getText());
            ps.setString(2, phone.getText());
            ps.setString(3, address.getText());
            ps.setDate(4, java.sql.Date.valueOf(joiningDate.getText()));
            ps.setDouble(5, Double.parseDouble(salary.getText()));
            ps.setString(6, paymentType.getSelectedItem().toString());
            ps.setString(7, status.getText());
            ps.setString(8, empID.getText());

            ps.executeUpdate();
            ps.close();
            con.close();

            loadTable();
            clearFields();

if(accountsPage != null){
    accountsPage.loadTableData();
}

            JOptionPane.showMessageDialog(this,"EMPLOYEE UPDATED SUCCESSFULLY");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // DELETE
    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if(row==-1) return;

        try{
            Connection con = DB.getConnection();
            String sql="UPDATE EMPLOYEE SET STATUS='INACTIVE' WHERE EMPLOYEE_ID=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, empID.getText());
            ps.executeUpdate();
            ps.close();
            con.close();

            model.removeRow(row);
            clearFields();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // CLEAR
    private void clearFields(){
        empID.setText("");
        empName.setText("");
        phone.setText("");
        address.setText("");
        joiningDate.setText(LocalDate.now().toString());
        salary.setText("");
        paymentType.setSelectedIndex(0);
        status.setText("ACTIVE");
    }

    // LOAD TABLE
    private void loadTable(){
        model.setRowCount(0);
        try{
            Connection con = DB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYEE WHERE STATUS='ACTIVE' ORDER BY EMPLOYEE_ID");
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("EMPLOYEE_ID"),
                        rs.getString("EMPLOYEE_NAME"),
                        rs.getString("PHONE_NO"),
                        rs.getString("ADDRESS"),
                        rs.getDate("JOINING_DATE"),
                        rs.getDouble("SALARY"),
                        rs.getString("PAYMENT_TYPE"),
                        rs.getString("STATUS")
                });
            }
            rs.close();
            st.close();
            con.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // SEARCH
    private void searchEmployee(){
        String field = cmbsearch.getSelectedItem().toString();
        String text = txtsearch.getText().trim();

        if(text.isEmpty()){ loadTable(); return; }

        String column = switch(field){
            case "EMPLOYEE ID" -> "EMPLOYEE_ID";
            case "EMPLOYEE NAME" -> "EMPLOYEE_NAME";
            case "PHONE" -> "PHONE_NO";
            case "PAYMENT TYPE" -> "PAYMENT_TYPE";
            case "STATUS" -> "STATUS";
            default -> "";
        };

        model.setRowCount(0);
        try{
            Connection con = DB.getConnection();
            String sql = "SELECT * FROM EMPLOYEE WHERE "+column+" LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1,"%"+text+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("EMPLOYEE_ID"),
                        rs.getString("EMPLOYEE_NAME"),
                        rs.getString("PHONE_NO"),
                        rs.getString("ADDRESS"),
                        rs.getDate("JOINING_DATE"),
                        rs.getDouble("SALARY"),
                        rs.getString("PAYMENT_TYPE"),
                        rs.getString("STATUS")
                });
            }
            rs.close();
            ps.close();
            con.close();
        }catch(Exception e){ e.printStackTrace(); }
    }
}