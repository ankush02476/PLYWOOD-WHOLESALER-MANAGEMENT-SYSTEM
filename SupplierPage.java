import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.print.PrinterJob;


public class SupplierPage extends JPanel {

    function fn = new function();
    private PurchasePage purchasePage;

    JTextField supplierID, suppliercompanyName, phone, address, gst, accountno, status, txtsearch;
    JComboBox<String> cmbsearch;
    
    JButton btnprint;
    JTable table;
    DefaultTableModel model;

    public SupplierPage(PurchasePage purchasePage) {
    this.purchasePage = purchasePage; 

        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(new BorderLayout(15,0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60,60,60,60));
        mainPanel.setBackground(new Color(242,230,213));

        fn.createLabel(mainPanel,"SUPPLIER MODULE",65,30,16);
        fn.createLabel(mainPanel,"SUPPLIER LIST",400,30,16);
        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);

        // SEARCH
        cmbsearch = new JComboBox<>(new String[]{
                "SUPPLIER ID","SUPPLIER NAME","PHONE","STATUS"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);

        txtsearch = fn.createTextField(mainPanel,"",970,30);
        txtsearch.setBounds(970,30,160,28);

        JButton search = fn.createRoundButton(mainPanel,"SEARCH",1140,30);
        search.setBounds(1140,30,160,28);
        fn.addHoverEffect(search,new Color(10,10,10),new Color(230,160,160));
        mainPanel.add(search);

        search.addActionListener(e -> searchSupplier());

        // FORM PANEL
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(320,0));
        formPanel.setBackground(new Color(250,249,246));

        int y = 20;

        supplierID = fn.createTextField(formPanel,"",20,30);
        fn.createLabel(formPanel,"Supplier ID:",20,y-20,14);
        supplierID.setEditable(false);
        supplierID.setFocusable(false);


        y+=70;
        suppliercompanyName = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"SuppliercompanyName:",20,y-30,14);

        y+=60;
        phone = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Phone No:",20,y-30,14);

        y+=60;
        address = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Address:",20,y-30,14);

        y+=60;
        gst = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"GST No:",20,y-30,14);

        y+=60;
        accountno = fn.createTextField(formPanel,"",20,y);
        fn.createLabel(formPanel,"Account No:",20,y-30,14);

        y+=60;
        status = fn.createTextField(formPanel,"ACTIVE",20,y);
        fn.createLabel(formPanel,"Status:",20,y-30,14);

        // BUTTONS
        y+=60;
        JButton btnAdd = fn.createRoundButton(formPanel,"ADD",20,y+10);
        JButton btnUpdate = fn.createRoundButton(formPanel,"UPDATE",20,y+50);
        JButton btnDelete = fn.createRoundButton(formPanel,"DELETE",20,y+90);

        fn.addHoverEffect(btnAdd,new Color(10,10,10),new Color(230,160,160));
        fn.addHoverEffect(btnUpdate,new Color(10,10,10),new Color(230,160,160));
        fn.addHoverEffect(btnDelete,new Color(10,10,10),new Color(230,160,160));

        btnAdd.addActionListener(e -> insertSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());

        // TABLE
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE); 

        model = new DefaultTableModel(
                new String[]{"Supplier ID","SuppliercompanyName","Phone","Address","GST","Account No","Status"},0
        );

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250,249,246));
        header.setFont(new Font("TIMES NEW ROMAN",Font.BOLD,12));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane,BorderLayout.CENTER);

        btnprint = new JButton("PRINT");
        btnprint.setForeground(Color.WHITE);
        btnprint.setPreferredSize(new Dimension(100,40));
        fn.addHoverEffect(btnprint,new Color(10,10,10),new Color(230,160,160));
        btnprint.addActionListener(e -> printSupplier());

        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printPanel.setBackground(Color.WHITE);
        printPanel.add(btnprint);
        tablePanel.add(printPanel,BorderLayout.SOUTH);

        // CLICK EVENT
        table.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = table.getSelectedRow();

                supplierID.setText(model.getValueAt(row,0).toString());
                suppliercompanyName.setText(model.getValueAt(row,1).toString());
                phone.setText(model.getValueAt(row,2).toString());
                address.setText(model.getValueAt(row,3).toString());
                gst.setText(model.getValueAt(row,4).toString());
                accountno.setText(model.getValueAt(row,5).toString());
                status.setText(model.getValueAt(row,6).toString());
            }
        });

        mainPanel.add(formPanel,BorderLayout.WEST);
        mainPanel.add(tablePanel,BorderLayout.CENTER);

        add(mainPanel);

        loadTable();
    }

   private void printSupplier(){

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
                    "SUPPLIER LIST"   // ✅ FIX ADDED
            );

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(invoice);

            if(job.printDialog()) job.print();

            return null;
        }

        protected void done(){
            processing.dispose();
            JOptionPane.showMessageDialog(
                    SupplierPage.this,
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
    private void insertSupplier(){
        try{
            Connection con = DB.getConnection();

            String sql = "INSERT INTO SUPPLIER (SUPPLIER_COMPANY_NAME,PHONE,ADDRESS,GST_NO,ACCOUNT_NO,STATUS) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

ps.setString(1, suppliercompanyName.getText().trim());
ps.setString(2, phone.getText().trim());
ps.setString(3, address.getText().trim());
ps.setString(4, gst.getText().trim());
ps.setString(5, accountno.getText().trim());
ps.setString(6, status.getText().trim());
           

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Supplier Added");
            if (purchasePage != null) {
    purchasePage.refreshSupplierCombo();
}

            loadTable();
            clearFields();

        }catch(Exception e){ e.printStackTrace(); }
    }

    // UPDATE
    private void updateSupplier(){
        try{
            Connection con = DB.getConnection();

            String sql = "UPDATE SUPPLIER SET SUPPLIER_COMPANY_NAME=?,PHONE=?,ADDRESS=?,GST_NO=?,ACCOUNT_NO=?,STATUS=? WHERE SUPPLIER_ID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, suppliercompanyName.getText());
            ps.setString(2, phone.getText());
            ps.setString(3, address.getText());
            ps.setString(4, gst.getText());
            ps.setString(5, accountno.getText());
            ps.setString(6, status.getText());
            ps.setString(7, supplierID.getText());

            ps.executeUpdate();

            loadTable();
            clearFields();

        }catch(Exception e){ e.printStackTrace(); }
    }

    // DELETE (Soft Delete)
    private void deleteSupplier(){
        try{
            Connection con = DB.getConnection();

            String sql="UPDATE SUPPLIER SET STATUS='INACTIVE' WHERE SUPPLIER_ID=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, supplierID.getText());
            ps.executeUpdate();

            loadTable();
            clearFields();

        }catch(Exception e){ e.printStackTrace(); }
    }

    // CLEAR
    private void clearFields(){
        supplierID.setText("");
        suppliercompanyName.setText("");
        phone.setText("");
        address.setText("");
        gst.setText("");
        accountno.setText("");
        status.setText("ACTIVE");
    }

    // LOAD TABLE
    private void loadTable(){
        model.setRowCount(0);

        try{
            Connection con = DB.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM SUPPLIER"
            );

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("SUPPLIER_ID"),
                        rs.getString("SUPPLIER_COMPANY_NAME"),
                        rs.getString("PHONE"),
                        rs.getString("ADDRESS"),
                        rs.getString("GST_NO"),
                        rs.getString("ACCOUNT_NO"),
                        rs.getString("STATUS")
                });
            }

        }catch(Exception e){ e.printStackTrace(); }
    }

    // SEARCH
    private void searchSupplier(){
        String field = cmbsearch.getSelectedItem().toString();

        String column = switch(field){
            case "SUPPLIER ID" -> "SUPPLIER_ID";
            case "SUPPLIER COMPANY NAME" -> "SUPPLIER_COMPANY_NAME";
            case "PHONE" -> "PHONE";
            default -> "STATUS";
        };

        try{
            Connection con = DB.getConnection();

            String sql = "SELECT * FROM SUPPLIER WHERE "+column+" LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,"%"+txtsearch.getText()+"%");
            ResultSet rs = ps.executeQuery();

            model.setRowCount(0);

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("SUPPLIER_ID"),
                        rs.getString("SUPPLIER_COMPANY_NAME"),
                        rs.getString("PHONE"),
                        rs.getString("ADDRESS"),
                        rs.getString("GST_NO"),
                        rs.getString("ACCOUNT_NO"),
                        rs.getString("STATUS")
                });
            }

        }catch(Exception e){ e.printStackTrace(); }
    }
}