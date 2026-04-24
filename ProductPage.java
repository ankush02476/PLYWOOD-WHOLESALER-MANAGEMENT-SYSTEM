import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.print.PrinterJob;



public class ProductPage extends JPanel {

    function fn = new function(); 

    JTextField ProductID, txtHSN, productRate, GST, QTY, STATUS,txtsearch;
    JComboBox<String> cmbCategory, thickness,cmbsearch;

    JButton search,btnprint;

    JTable table;
    DefaultTableModel model;

    public ProductPage() {
        setLayout(new BorderLayout());
        setBackground(Color.white);

        // MAIN PANEL //
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.setBackground(new Color(242,230,213));
        fn.createLabel(mainPanel, "PRODUCT MODULE", 65, 30, 16);
        fn.createLabel(mainPanel, "PRODUCT LIST", 400, 30, 16);
        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);
        cmbsearch = new JComboBox<>(new String[]{
                "PRODUCT ID","PRODUCT TYPE","PRODUCT THICKNESS","HSN CODE","GST","PRODUCT RATE","STATUS"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);
        txtsearch=fn.createTextField(mainPanel,"",1000,30);
        txtsearch.setBounds(970,30,160,28);
        search=fn.createRoundButton(mainPanel, "SEARCH",1140,30);
        fn.addHoverEffect(search, new Color(10, 10, 10), new Color(230, 160, 160));
        search.setBounds(1140,30,160,28);
        mainPanel.add(search);
        search.addActionListener(e -> searchProduct());



        // LEFT FORM PANEL //
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(320, 0));
        formPanel.setBackground(new Color(250, 249, 246));

        int y = 20;

        ProductID = fn.createTextField(formPanel, "", 20, 30);
        fn.createLabel(formPanel, "Product ID:", 20, y - 20, 14);
        ProductID.setEditable(false);
        ProductID.setFocusable(false);

        y += 70;
        cmbCategory = new JComboBox<>(new String[]{
                " ", "MR WATERPROOF", "COMMERCIAL DPING", "COMMERCIAL LOW"
        });
        cmbCategory.setBounds(20, y, 280, 30);
        formPanel.add(cmbCategory);
        fn.createLabel(formPanel, "Product Type:", 20, y - 30, 14);

        y += 60;
        thickness = new JComboBox<>(new String[]{
                " ", "19mm", "18mm", "16mm", "12mm", "10mm", "6mm", "4mm", "2mm"
        });
        thickness.setBounds(20, y, 280, 30);
        formPanel.add(thickness);
        fn.createLabel(formPanel, "Product Thickness:", 20, y - 30, 14);

        y += 60;
        txtHSN = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "HSN Code:", 20, y - 30, 14);

        y += 60;
        GST = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "GST", 20, y - 30, 14);

        y += 60;
        QTY = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Product Unit", 20, y - 30, 14);

        y += 60;
        productRate = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Product Rate (sqft):", 20, y - 30, 14);

        y += 60;
        STATUS = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Status:", 20, y - 30, 14);
        STATUS.setText("ACTIVE"); 

        // BUTTONS //
        y += 60;
        JButton btnAdd = fn.createRoundButton(formPanel, "ADD", 20, y + 10);
        JButton btnUpdate = fn.createRoundButton(formPanel, "UPDATE", 20, y + 50);
        JButton btnDelete = fn.createRoundButton(formPanel, "DELETE", 20, y + 90);

        fn.addHoverEffect(btnAdd, new Color(10, 10, 10), new Color(230, 160, 160));
        fn.addHoverEffect(btnUpdate, new Color(10, 10, 10), new Color(230, 160, 160));
        fn.addHoverEffect(btnDelete, new Color(10, 10, 10), new Color(230, 160, 160));

        btnAdd.addActionListener(e -> insertProduct());
        btnUpdate.addActionListener(e -> updateProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        // RIGHT TABLE PANEL //
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(
                new String[]{"Product ID", "Product Type", "Product Thickness",
                        "HSN code","GST","Product Unit", "Product Rate (sqft)","Status"}, 0
        );
        table = new JTable(model);
        table.setShowGrid(true);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250, 249, 246));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        btnprint = new JButton("PRINT");
        btnprint.setForeground(Color.WHITE);
        btnprint.setPreferredSize(new Dimension(100, 40));
        fn.addHoverEffect(btnprint, new Color(10,10,10), new Color(230,160,160));

        btnprint.addActionListener(e -> printInvoice());
        

        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printPanel.setBackground(Color.WHITE);
        printPanel.add(btnprint);
        tablePanel.add(printPanel, BorderLayout.SOUTH);

        // Click on table row to fill fields //

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    ProductID.setText(model.getValueAt(row, 0).toString());
                    cmbCategory.setSelectedItem(model.getValueAt(row, 1).toString());
                    thickness.setSelectedItem(model.getValueAt(row, 2).toString());
                    txtHSN.setText(model.getValueAt(row, 3).toString());
                    GST.setText(model.getValueAt(row, 4).toString());
                    QTY.setText(model.getValueAt(row, 5).toString());
                    productRate.setText(model.getValueAt(row, 6).toString());
                    STATUS.setText(model.getValueAt(row, 7).toString());
                }
            }
        });

        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        loadTable(); 
    }
     // PRINT FUNCTION //
 private void printInvoice() {

    JDialog processing = new JDialog((Frame)null, "Printing", true);
    processing.add(new JLabel("Processing... Please wait", JLabel.CENTER));
    processing.setSize(250, 100);
    processing.setLocationRelativeTo(this);

    SwingWorker<Void, Void> worker = new SwingWorker<>() {

        @Override
        protected Void doInBackground() throws Exception {

            PrintableInvoice invoice = new PrintableInvoice(
                    "JAI MAA JAGDAMBA PLY",
                    "KHANDERAN SINGH MARG, NEAR SAHITYA SAMMELAN,KADAMKUAN,PATNA-3",
                    "PHONE NO:9122678054",
                    table,
                    "PRODUCT LIST"   // ✅ FIX ADDED
            );

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(invoice);

            if (job.printDialog()) job.print();

            return null;
        }

        @Override
        protected void done() {
            processing.dispose();
            JOptionPane.showMessageDialog(
                    ProductPage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}


    private void searchProduct() {

    String selectedField = cmbsearch.getSelectedItem().toString();
    String searchText = txtsearch.getText().trim();

    if (searchText.isEmpty()) {
        loadTable(); // show all data if empty search
        return;
    }

    // Map combo box values to DB column names
    String columnName = "";

    switch (selectedField) {
        case "PRODUCT ID":
            columnName = "product_id";
            break;
        case "PRODUCT NAME":
            columnName = "product_type"; // adjust if you have product_name column
            break;
        case "PRODUCT TYPE":
            columnName = "product_type";
            break;
        case "PRODUCT THICKNESS":
            columnName = "product_thickness";
            break;
        case "HSN CODE":
            columnName = "hsn_code";
            break;
        case "GST":
            columnName = "gst";
            break;
        case "PRODUCT RATE":
            columnName = "product_rate";
            break;
        case "STATUS":
            columnName = "status";
            break;
        default:
            JOptionPane.showMessageDialog(this, "INVALID SEARCH FIELD");
            return;
    }

    model.setRowCount(0); // clear table

    try {
        Connection con = DB.getConnection();
        String sql = "SELECT * FROM product WHERE " + columnName + " LIKE ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + searchText + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("product_id"),
                    rs.getString("product_type"),
                    rs.getString("product_thickness"),
                    rs.getString("hsn_code"),
                    rs.getString("gst"),
                    rs.getString("product_unit"),
                    rs.getString("product_rate"),
                    rs.getString("status")
            });
        }

        rs.close();
        ps.close();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "ERROR WHILE SEARCHING");
    }
}


    // INSERT PRODUCT //
    private void insertProduct() {
        if (cmbCategory.getSelectedIndex() == 0 ||
            thickness.getSelectedIndex() == 0 ||
            txtHSN.getText().trim().isEmpty() ||
            GST.getText().trim().isEmpty() ||
            QTY.getText().trim().isEmpty() ||
            productRate.getText().trim().isEmpty() ||
            STATUS.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "PLEASE ENTER ALL DETAILS");
            return;
        }

        double rate;
        try {
            rate = Double.parseDouble(productRate.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "PLEASE ENTER VALID DATA FOR PRODUCT RATE");
            return;
        }

        try {
            Connection con = DB.getConnection();
            String sql =
                    "INSERT INTO PRODUCT " +
                    "(product_type, product_thickness, hsn_code, gst, product_unit, product_rate, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"product_id"});
            ps.setString(1, cmbCategory.getSelectedItem().toString());
            ps.setString(2, thickness.getSelectedItem().toString());
            ps.setString(3, txtHSN.getText().trim());
            ps.setString(4, GST.getText().trim());
            ps.setString(5, QTY.getText().trim());
            ps.setDouble(6, rate);
            ps.setString(7, STATUS.getText().trim());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) ProductID.setText(rs.getString(1));

            ps.close();
            rs.close();
            con.close();

            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "PRODUCT ADDED SUCCESSFULLY");
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROE ADDING PRODUCT");
        }
    }

    // UPDATE PRODUCT //
    private void updateProduct() {
        if(ProductID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "PLEASE SELECT ROW TO UPDATE");
            return;
        }

        if (cmbCategory.getSelectedIndex() == 0 ||
            thickness.getSelectedIndex() == 0 ||
            txtHSN.getText().trim().isEmpty() ||
            GST.getText().trim().isEmpty() ||
            QTY.getText().trim().isEmpty() ||
            productRate.getText().trim().isEmpty() ||
            STATUS.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "PLEASE ENETR ALL DETAILS");
            return;
        }

        double rate;
        try {
            rate = Double.parseDouble(productRate.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "PLEASE ENTER VALID DATA FOR PRODUCT RATE");
            return;
        }

        try {
            Connection con = DB.getConnection();
            String sql = "UPDATE product SET product_type=?, product_thickness=?, hsn_code=?, gst=?, " +
                         "product_unit=?, product_rate=?, status=? WHERE product_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cmbCategory.getSelectedItem().toString());
            ps.setString(2, thickness.getSelectedItem().toString());
            ps.setString(3, txtHSN.getText().trim());
            ps.setString(4, GST.getText().trim());
            ps.setString(5, QTY.getText().trim());
            ps.setDouble(6, rate);
            ps.setString(7, STATUS.getText().trim());
            ps.setString(8, ProductID.getText().trim());

            ps.executeUpdate();
            ps.close();
            con.close();

            loadTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "PRODUCT UPDATED SUCCESSFULLY");
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR UPDATING PRODUCT");
        }
    }

    // DELETE PRODUCT //
     private void deleteProduct() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        try {
            Connection con = DB.getConnection();
            String sql = "UPDATE product SET status='INACTIVE' WHERE product_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ProductID.getText());
            ps.executeUpdate();

            ps.close();
            con.close();

            model.removeRow(row);
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // CLEAR FIELDS //
    private void clearFields() {
        ProductID.setText("");
        cmbCategory.setSelectedIndex(0);
        thickness.setSelectedIndex(0);
        txtHSN.setText("");
        GST.setText("");
        QTY.setText("");
        productRate.setText("");
        STATUS.setText("ACTIVE"); 
    }

    // LOAD DATA INTO TABLE //
    private void loadTable() {
        model.setRowCount(0);
        try {
            Connection con = DB.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM product WHERE status='ACTIVE' ORDER BY product_id");
            while(rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("product_id"),
                        rs.getString("product_type"),
                        rs.getString("product_thickness"),
                        rs.getString("hsn_code"),
                        rs.getString("gst"),
                        rs.getString("product_unit"),
                        rs.getString("product_rate"),
                        rs.getString("status")
                });
            }
            rs.close();
            st.close();
            con.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}