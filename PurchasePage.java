import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.print.PrinterJob;
import java.sql.*;



public class PurchasePage extends JPanel {

    function fn = new function();
    

    // Fields
    JTextField purchaseID, purchaseBillNo, purchaseDate,supplierID,TXTSEARCH;
    JTextField dueDate, productRate, discount, productID, totalGST;
    JTextField Dueamount, totalQuantity, gstAmount, netPurchaseRate, paidAmount;

    JComboBox<String> paymentMode,cmbthickness,suppliercompanyName;
    JComboBox<String> productname,cmbsearch;

    JButton btnAdd, btnUpdate, btnDelete, btnPrint,SEARCH;

    JTable table;
    DefaultTableModel model;
    private StockPage stockPage;
    private AccountsPage accountsPage;
    public PurchasePage(StockPage stockPage,AccountsPage accountsPage) {
    this.stockPage = stockPage;
    this.accountsPage = accountsPage;

    
        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(242, 230, 213));
        mainPanel.setBounds(0, 0, 1300, 700);

        fn.createLabel(mainPanel, "PURCHASE MODULE", 65, 30, 16);

        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);
        cmbsearch = new JComboBox<>(new String[]{
                "PURCHASE ID","PURCHASE BILL NO","SUPPLIER COMPANY NAME","PAYMENT MODE","PRODUCT NAME","PRODUCT ID","PURCHASE DATE","DUE DATE"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);
        TXTSEARCH=fn.createTextField(mainPanel,"",1000,30);
        TXTSEARCH.setBounds(970,30,160,28);
        SEARCH=fn.createRoundButton(mainPanel, "SEARCH",1140,30);
        fn.addHoverEffect(SEARCH, new Color(10, 10, 10), new Color(230, 160, 160));
        SEARCH.setBounds(1140,30,160,28);
        mainPanel.add(SEARCH);
        SEARCH.addActionListener(e -> searchPurchase());


        JPanel upperPanel = new JPanel(null);
        upperPanel.setBackground(new Color(250, 249, 246));
        upperPanel.setBounds(60, 60, 1240, 360);

        int x = 20, y = 20, fieldWidth = 280, fieldHeight = 28, gapX = 370, gapY = 50;

// -------- ROW 1 --------
purchaseID = fn.createTextField(upperPanel, "", x, y);
fn.createLabel(upperPanel, "Purchase ID:", x, y - 25, 14);
purchaseID.setEditable(false);
purchaseID.setFocusable(false);


productID = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Product ID:", x + gapX, y - 25, 14);

// ✅ NEW FIELD (Supplier ID)
supplierID = fn.createTextField(upperPanel, "", x + 2 * gapX, y);
fn.createLabel(upperPanel, "Supplier ID:", x + 2 * gapX, y - 25, 14);

y += gapY;

// -------- ROW 2 --------
purchaseDate = fn.createTextField(upperPanel, "", x, y);
fn.createLabel(upperPanel, "Purchase Date:", x, y - 25, 14);
purchaseDate.setEditable(false);

LocalDate today = LocalDate.now();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
purchaseDate.setText(today.format(formatter));

// Middle column
productRate = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Product Rate(sqft)", x + gapX, y - 25, 14);

// Last column
paidAmount = fn.createTextField(upperPanel, "", x + 2 * gapX, y);
fn.createLabel(upperPanel, "Paid Amount:", x + 2 * gapX, y - 25, 14);



y += gapY;
 
Dueamount = fn.createTextField(upperPanel, "", x + 2 * gapX, y);
fn.createLabel(upperPanel, "Due Amount:", x + 2 * gapX, y - 25, 14);

// -------- ROW 3 --------
purchaseBillNo = fn.createTextField(upperPanel, "", x, y);
fn.createLabel(upperPanel, "Purchase BillNo:", x, y - 25, 14);
purchaseBillNo.setEditable(true);

// Middle column
totalQuantity = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Total Quantity/unit", x + gapX, y - 25, 14);



y += gapY;

// -------- ROW 4 --------
// First column
productname = new JComboBox<>(new String[]{
    " ", "MR WATERPROOF", "COMMERCIAL DPING", "COMMERCIAL LOW"
});
productname.setBounds(x, y, fieldWidth, fieldHeight);
upperPanel.add(productname);
fn.createLabel(upperPanel, "Product Name:", x, y - 25, 14);

// Auto fetch Product ID
productname.addActionListener(e -> {
    if (productname.getSelectedIndex() > 0 && cmbthickness.getSelectedIndex() > 0) {
        fetchProductID();
    } else {
        productID.setText("");
    }
});

// Middle column
totalGST = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Total GST:", x + gapX, y - 25, 14);

// Last column
paymentMode = new JComboBox<>(new String[]{" ","Cash", "Bank","UPI"});
paymentMode.setBounds(x + 2 * gapX, y, fieldWidth, fieldHeight);
upperPanel.add(paymentMode);
fn.createLabel(upperPanel, "Payment Mode:", x + 2 * gapX, y - 25, 14);

y += gapY;

// -------- ROW 5 --------
// First column
cmbthickness = new JComboBox<>(new String[]{
    " ", "19mm", "18mm", "16mm", "12mm", "10mm", "6mm", "4mm", "2mm"
});
cmbthickness.setBounds(x, y, fieldWidth, fieldHeight);
upperPanel.add(cmbthickness);
fn.createLabel(upperPanel, "Product Thickness:", x, y - 25, 14);

// Auto fetch Product ID
cmbthickness.addActionListener(e -> {
    if (productname.getSelectedIndex() > 0 && cmbthickness.getSelectedIndex() > 0) {
        fetchProductID();
    } else {
        productID.setText("");
    }
});


discount = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Discount:", x + gapX, y - 25, 14);

dueDate = fn.createTextField(upperPanel, "", x + 2 * gapX, y);
fn.createLabel(upperPanel, "Due Date:", x + 2 * gapX, y - 25, 14);

y += gapY;



suppliercompanyName = new JComboBox<>();
suppliercompanyName.setBounds(x, y, fieldWidth, fieldHeight);
upperPanel.add(suppliercompanyName);
fn.createLabel(upperPanel, "suppliercompanyName:", x, y - 25, 14);
suppliercompanyName.addActionListener(e -> {
    if (suppliercompanyName.getSelectedIndex() > 0) {
        fetchSupplierID();
    } else {
        supplierID.setText("");
    }
});
// Middle column
netPurchaseRate = fn.createTextField(upperPanel, "", x + gapX, y);
fn.createLabel(upperPanel, "Net Purchase Rate:", x + gapX, y - 25, 14);



        btnAdd = fn.createRoundButton(upperPanel, "ADD", 1100, 100);
        btnAdd.setSize(120, 30);

        btnUpdate = fn.createRoundButton(upperPanel, "UPDATE", 1100, 150);
        btnUpdate.setSize(120, 30);

        btnDelete = fn.createRoundButton(upperPanel, "DELETE", 1100, 200);
        btnDelete.setSize(120, 30);

        fn.addHoverEffect(btnAdd, new Color(10,10,10), new Color(230,160,160));
        fn.addHoverEffect(btnUpdate, new Color(10,10,10), new Color(230,160,160));
        fn.addHoverEffect(btnDelete, new Color(10,10,10), new Color(230,160,160));

        
        btnAdd.addActionListener(e -> insertPurchase());
        btnUpdate.addActionListener(e -> updatePurchase());
        btnDelete.addActionListener(e -> deletePurchase());

        mainPanel.add(upperPanel);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBounds(60, 440, 1240, 320);
        lowerPanel.setBackground(Color.WHITE);

        JLabel tableLabel = new JLabel("PURCHASE DETAILS");
        tableLabel.setFont(new Font("times new roman", Font.BOLD, 16));
        tableLabel.setHorizontalAlignment(JLabel.CENTER);
        tableLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lowerPanel.add(tableLabel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "Purchase ID","Date","Total GST","Billno","Net Purchase Rate","Paid Amount",
                "Payment Mode","Due Date","Product ID","Product Name","Product Thickness",
                "Product rate(sqft)","Total Quantity/unit","Discount",
                "supplier company Name","SupplierID"
        }, 0);

        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFont(new Font("Segoe UI",Font.PLAIN,13));
        table.setRowHeight(30);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);
        TableColumnModel columnModel = table.getColumnModel();
        for(int i=0;i<columnModel.getColumnCount();i++){
            columnModel.getColumn(i).setPreferredWidth(150);
        }
        

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250, 249, 246));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 12));

        lowerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        btnPrint = new JButton("PRINT");
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setPreferredSize(new Dimension(100, 30));
        fn.addHoverEffect(btnPrint, new Color(10,10,10), new Color(230,160,160));

        btnPrint.addActionListener(e -> printInvoice());

        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printPanel.add(btnPrint);
        lowerPanel.add(printPanel, BorderLayout.SOUTH);

        mainPanel.add(lowerPanel);
        add(mainPanel);

              // SELECTION LISTENER
        table.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting() && table.getSelectedRow()!=-1){
                int row = table.getSelectedRow();
                purchaseID.setText(model.getValueAt(row,0).toString());
                purchaseDate.setText(model.getValueAt(row,1).toString());
                totalGST.setText(model.getValueAt(row,2).toString());
                purchaseBillNo.setText(model.getValueAt(row,3).toString());
                netPurchaseRate.setText(model.getValueAt(row,4).toString());
                paidAmount.setText(model.getValueAt(row,5).toString());
                paymentMode.setSelectedItem(model.getValueAt(row,6).toString());
                dueDate.setText(model.getValueAt(row,7).toString());
                productID.setText(model.getValueAt(row,8).toString());
                productname.setSelectedItem(model.getValueAt(row,9).toString());
                cmbthickness.setSelectedItem(model.getValueAt(row,10).toString());
                productRate.setText(model.getValueAt(row,11).toString());
                totalQuantity.setText(model.getValueAt(row,12).toString());
                discount.setText(model.getValueAt(row,13).toString());
                suppliercompanyName.setSelectedItem(model.getValueAt(row,14).toString());
                supplierID.setText(model.getValueAt(row,15).toString());
                calculateAmounts();
            }
        });
        

        // LOAD TABLE INITIALLY
       loadTableData();
       addAutoCalculation(productRate);
       addAutoCalculation(totalQuantity);
       addAutoCalculation(discount);
       addAutoCalculation(paidAmount);

       loadSuppliers();


}

public void refreshSupplierCombo() {
    loadSuppliers();
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
                    "PURCHASE LIST"   // ✅ FIX ADDED
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
                    PurchasePage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}

private void fetchProductID() {
    String product = productname.getSelectedItem().toString();
    String thickness = cmbthickness.getSelectedItem().toString();

    if (product.equals(" ") || thickness.equals(" ")) {
        productID.setText("");
        return;
    }

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        con = DB.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            return;
        }

        // Oracle DB: PRODUCT_TYPE maps to productname, PRODUCT_THICKNESS maps to thickness
        String sql = "SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_TYPE = ? AND PRODUCT_THICKNESS = ?";
        ps = con.prepareStatement(sql);
        ps.setString(1, product);
        ps.setString(2, thickness);

        rs = ps.executeQuery();

        if (rs.next()) {
            productID.setText(rs.getString("PRODUCT_ID"));
        } else {
            productID.setText("");
            JOptionPane.showMessageDialog(this, "Product not found for selected type & thickness.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "SQL Error: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Product ID: " + e.getMessage());
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception e) {}
        try { if (ps != null) ps.close(); } catch (Exception e) {}
        try { if (con != null) con.close(); } catch (Exception e) {}
    }
}

private double parseDouble(String value) {
    try {
        if (value == null || value.trim().isEmpty())
            return 0.0;
        return Double.parseDouble(value.trim());
    } catch (Exception e) {
        return 0.0;
    }
}

private void insertPurchase() {

    // ===== STEP 0: BASIC UI VALIDATION =====
    if (purchaseBillNo.getText().trim().isEmpty() ||
        productID.getText().trim().isEmpty() ||
        suppliercompanyName.getSelectedIndex() == 0 ||
        productname.getSelectedIndex() == 0 ||
        cmbthickness.getSelectedIndex() == 0 ||
        paymentMode.getSelectedIndex() == 0) {

        JOptionPane.showMessageDialog(this, "Please fill all required fields!");
        return;
    }

    try (Connection con = DB.getConnection()) {

        // ===== STEP 1: PARSE AND VALIDATE NUMERIC FIELDS =====
        double rate = parseDouble(productRate.getText());
        double qty = parseDouble(totalQuantity.getText());
        double gst = parseDouble(totalGST.getText());
        double discountVal = parseDouble(discount.getText());
        double net = parseDouble(netPurchaseRate.getText());
        double paid = parseDouble(paidAmount.getText());
        double due = parseDouble(Dueamount.getText());

        if (rate <= 0) { JOptionPane.showMessageDialog(this, "Product Rate must be > 0"); return; }
        if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be > 0"); return; }
        if (net < 0) { JOptionPane.showMessageDialog(this, "Net Amount cannot be negative"); return; }
        if (paid < 0 || due < 0) { JOptionPane.showMessageDialog(this, "Paid/Due cannot be negative"); return; }

        // ===== STEP 2: VALIDATE PAYMENT MODE =====
        String mode = paymentMode.getSelectedItem().toString().trim().toUpperCase();
        if (!(mode.equals("CASH") || mode.equals("BANK") || mode.equals("UPI"))) {
            JOptionPane.showMessageDialog(this, "Please select valid Payment Mode (CASH/BANK/UPI)");
            return;
        }

        // ===== STEP 3: PREPARE SQL =====
        String sql = "INSERT INTO PURCHASE (" +
                "PURCHASE_DATE, DUE_DATE, PURCHASE_BILLNO, PRODUCT_ID, PRODUCT_NAME, " +
                "PRODUCT_THICKNESS, PRODUCT_RATE, TOTAL_QUANTITY, TOTAL_GST, DISCOUNT, " +
                "NET_PURCHASE_RATE, PAID_AMOUNT, DUE_AMOUNT, PAYMENT_MODE, " +
                "SUPPLIER_COMPANY_NAME, SUPPLIER_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = con.prepareStatement(sql);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // PURCHASE_DATE
        ps.setDate(1, java.sql.Date.valueOf(
                LocalDate.parse(purchaseDate.getText().trim(), formatter)));

        // DUE_DATE (nullable)
        if (dueDate.getText().trim().isEmpty()) {
            ps.setNull(2, Types.DATE);
        } else {
            ps.setDate(2, java.sql.Date.valueOf(
                    LocalDate.parse(dueDate.getText().trim(), formatter)));
        }

        // STRING & NUMERIC VALUES
        ps.setString(3, purchaseBillNo.getText().trim());
        ps.setString(4, productID.getText().trim());
        ps.setString(5, productname.getSelectedItem().toString());
        ps.setString(6, cmbthickness.getSelectedItem().toString());
        ps.setDouble(7, rate);
        ps.setDouble(8, qty);
        ps.setDouble(9, gst);
        ps.setDouble(10, discountVal);
        ps.setDouble(11, net);
        ps.setDouble(12, paid);
        ps.setDouble(13, due);
        ps.setString(14, mode);
        ps.setString(15, suppliercompanyName.getSelectedItem().toString().trim());
        ps.setString(16, supplierID.getText().trim());

        // ===== STEP 4: EXECUTE INSERT =====
        ps.executeUpdate();
        ps.close();

        // ===== STEP 5: CALL EXISTING STOCK UPDATE =====
        updateStockFromPurchase(con);  // Use your existing working method

        // ===== STEP 6: REFRESH UI =====
        if (stockPage != null) stockPage.loadStockData();
        if (accountsPage != null) accountsPage.loadTableData();   // 🔥 ADD THIS

        JOptionPane.showMessageDialog(this, "Purchase Added Successfully!");
        clearFields();
        loadTableData();

    } catch (SQLIntegrityConstraintViolationException ex) {
        ex.printStackTrace(); // Keep this for debugging
        JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Insert Error: " + e.getMessage());
    }
}

private void updateStockFromPurchase(Connection con) throws SQLException {

    String productId = productID.getText().trim();
    String productNameValue = productname.getSelectedItem().toString();
    String thickness = cmbthickness.getSelectedItem().toString();
    double quantity = parseDouble(totalQuantity.getText());

    // Check if product already exists in STOCK
    String checkSql = "SELECT STOCK_IN, AVAILABLE_QUANTITY FROM STOCK WHERE PRODUCT_ID = ?";
    PreparedStatement checkPs = con.prepareStatement(checkSql);
    checkPs.setString(1, productId);
    ResultSet rs = checkPs.executeQuery();

    if (rs.next()) {
        // Product exists → update STOCK_IN and AVAILABLE_QUANTITY
        double currentStockIn = rs.getDouble("STOCK_IN");
        double currentAvailable = rs.getDouble("AVAILABLE_QUANTITY");

        String updateSql = "UPDATE STOCK SET STOCK_IN = ?, AVAILABLE_QUANTITY = ?, LAST_UPDATED = SYSDATE WHERE PRODUCT_ID = ?";
        PreparedStatement updatePs = con.prepareStatement(updateSql);

        updatePs.setDouble(1, currentStockIn + quantity);
        updatePs.setDouble(2, currentAvailable + quantity);
        updatePs.setString(3, productId);

        updatePs.executeUpdate();       
        updatePs.close();

    } else {
        // Product does not exist → insert new row
        String insertSql = "INSERT INTO STOCK (PRODUCT_ID, PRODUCT_NAME, PRODUCT_THICKNESS, STOCK_IN, STOCK_OUT, AVAILABLE_QUANTITY, MIN_STOCK_LEVEL, LAST_UPDATED) VALUES (?, ?, ?, ?, 0, ?, 10, SYSDATE)";
        PreparedStatement insertPs = con.prepareStatement(insertSql);

        insertPs.setString(1, productId);
        insertPs.setString(2, productNameValue);
        insertPs.setString(3, thickness);
        insertPs.setDouble(4, quantity); // STOCK_IN
        insertPs.setDouble(5, quantity); // AVAILABLE_QUANTITY

        insertPs.executeUpdate();
        insertPs.close();
    }

    rs.close();
    checkPs.close();
}

private void updatePurchase() {

    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to update!");
        return;
    }

    try (Connection con = DB.getConnection()) {

        // ===== STEP 1: PARSE AND VALIDATE NUMERIC FIELDS =====
        double rate = parseDouble(productRate.getText());
        double qty = parseDouble(totalQuantity.getText());
        double gst = parseDouble(totalGST.getText());
        double discountVal = parseDouble(discount.getText());
        double net = parseDouble(netPurchaseRate.getText());
        double paid = parseDouble(paidAmount.getText());
        double due = parseDouble(Dueamount.getText());

        if (rate <= 0) { JOptionPane.showMessageDialog(this, "Product Rate must be > 0"); return; }
        if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be > 0"); return; }
        if (net < 0) { JOptionPane.showMessageDialog(this, "Net Amount cannot be negative"); return; }
        if (paid < 0 || due < 0) { JOptionPane.showMessageDialog(this, "Paid/Due cannot be negative"); return; }

        // ===== STEP 2: VALIDATE PAYMENT MODE =====
        String mode = paymentMode.getSelectedItem().toString().trim().toUpperCase();
        if (!(mode.equals("CASH") || mode.equals("BANK") || mode.equals("UPI"))) {
            JOptionPane.showMessageDialog(this, "Please select valid Payment Mode (CASH/BANK/UPI)");
            return;
        }

        // ===== STEP 3: GET OLD QUANTITY =====
        double oldQty = 0;
        PreparedStatement psOld = con.prepareStatement(
                "SELECT TOTAL_QUANTITY FROM PURCHASE WHERE PURCHASE_ID=?"
        );
        psOld.setString(1, purchaseID.getText().trim());
        ResultSet rsOld = psOld.executeQuery();
        if (rsOld.next()) {
            oldQty = rsOld.getDouble("TOTAL_QUANTITY");
        }
        rsOld.close();
        psOld.close();

        // ===== STEP 4: UPDATE PURCHASE =====
        String sql = "UPDATE PURCHASE SET " +
                "DUE_DATE=?, PRODUCT_RATE=?, TOTAL_QUANTITY=?, TOTAL_GST=?, " +
                "DISCOUNT=?, NET_PURCHASE_RATE=?, PAID_AMOUNT=?, DUE_AMOUNT=?, " +
                "PAYMENT_MODE=?, SUPPLIER_COMPANY_NAME=?, UPDATED_AT=SYSDATE " +
                "WHERE PURCHASE_ID=?";

        PreparedStatement ps = con.prepareStatement(sql);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Nullable DUE_DATE
        if (dueDate.getText().trim().isEmpty())
            ps.setNull(1, Types.DATE);
        else
            ps.setDate(1, java.sql.Date.valueOf(
                    LocalDate.parse(dueDate.getText().trim(), formatter)));

        // Set other values
        ps.setDouble(2, rate);
        ps.setDouble(3, qty);
        ps.setDouble(4, gst);
        ps.setDouble(5, discountVal);
        ps.setDouble(6, net);
        ps.setDouble(7, paid);
        ps.setDouble(8, due);
        ps.setString(9, mode);
        ps.setString(10, suppliercompanyName.getSelectedItem().toString().trim());
        ps.setString(11, purchaseID.getText().trim());

        ps.executeUpdate();
        ps.close();

        // ===== STEP 5: UPDATE STOCK =====
        double diffQty = qty - oldQty; // Adjust stock based on difference

        if (diffQty != 0) {  // Only update if quantity changed
            PreparedStatement psStock = con.prepareStatement(
                    "UPDATE STOCK SET STOCK_IN = STOCK_IN + ?, AVAILABLE_QUANTITY = AVAILABLE_QUANTITY + ? WHERE PRODUCT_ID=?"
            );
            psStock.setDouble(1, diffQty);
            psStock.setDouble(2, diffQty);
            psStock.setString(3, productID.getText().trim());
            psStock.executeUpdate();
            psStock.close();
        }

        // ===== STEP 6: REFRESH UI =====
        if (stockPage != null) stockPage.loadStockData();
        if (accountsPage != null) accountsPage.loadTableData();

        JOptionPane.showMessageDialog(this, "Purchase Updated Successfully!");
        clearFields();
        loadTableData();

    } catch (SQLIntegrityConstraintViolationException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database Error:\n" + ex.getMessage());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
    }
}

private void deletePurchase() {

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row to delete!");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this record?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);

    if (confirm != JOptionPane.YES_OPTION)
        return;

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM PURCHASE WHERE PURCHASE_ID=?")) {

double oldQty = 0;

PreparedStatement psOld = con.prepareStatement(
        "SELECT TOTAL_QUANTITY FROM PURCHASE WHERE PURCHASE_ID=?"
);
psOld.setString(1, purchaseID.getText().trim());

ResultSet rsOld = psOld.executeQuery();
if (rsOld.next()) {
    oldQty = rsOld.getDouble("TOTAL_QUANTITY");
}

rsOld.close();
psOld.close();


        ps.setString(1, purchaseID.getText());

        ps.executeUpdate();


PreparedStatement psStock = con.prepareStatement(
        "UPDATE STOCK SET STOCK_IN = STOCK_IN - ?, AVAILABLE_QUANTITY = AVAILABLE_QUANTITY - ? WHERE PRODUCT_ID=?"
);

psStock.setDouble(1, oldQty);
psStock.setDouble(2, oldQty);
psStock.setString(3, productID.getText().trim());

psStock.executeUpdate();
psStock.close();

if (stockPage != null) {
    stockPage.loadStockData();
}



        JOptionPane.showMessageDialog(this, "Deleted Successfully!");

        loadTableData();
        clearFields();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
    }
}

private void loadTableData() {

    model.setRowCount(0);

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM PURCHASE ORDER BY CREATED_AT DESC");
         ResultSet rs = ps.executeQuery()) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (rs.next()) {

            String pDate = rs.getDate("PURCHASE_DATE") == null ? "" :
                    rs.getDate("PURCHASE_DATE").toLocalDate().format(formatter);

            String dDate = rs.getDate("DUE_DATE") == null ? "" :
                    rs.getDate("DUE_DATE").toLocalDate().format(formatter);

            model.addRow(new Object[]{
                    rs.getString("PURCHASE_ID"),
                    pDate,
                    rs.getDouble("TOTAL_GST"),
                    rs.getString("PURCHASE_BILLNO"),
                    rs.getDouble("NET_PURCHASE_RATE"),
                    rs.getDouble("PAID_AMOUNT"),
                    rs.getString("PAYMENT_MODE"),
                    dDate,
                    rs.getString("PRODUCT_ID"),
                    rs.getString("PRODUCT_NAME"),
                    rs.getString("PRODUCT_THICKNESS"),
                    rs.getDouble("PRODUCT_RATE"),
                    rs.getDouble("TOTAL_QUANTITY"),
                    rs.getDouble("DISCOUNT"),
                    rs.getString("SUPPLIER_COMPANY_NAME"),
                    rs.getString("SUPPLIER_ID")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Load Error: " + e.getMessage());
    }
}

private void clearFields() {

    purchaseID.setText("");
    purchaseBillNo.setText("");
    productID.setText("");
    productRate.setText("");
    totalQuantity.setText("");
    totalGST.setText("");
    discount.setText("");
    netPurchaseRate.setText("");
    paidAmount.setText("");
    Dueamount.setText("");
    suppliercompanyName.setSelectedIndex(0);
    dueDate.setText("");
    supplierID.setText("");

    paymentMode.setSelectedIndex(0);
    productname.setSelectedIndex(0);
    cmbthickness.setSelectedIndex(0);
}
private void searchPurchase() {

    String column = cmbsearch.getSelectedItem().toString();
    String value = TXTSEARCH.getText().trim();

    // If search box empty → reload full table
    if (value.isEmpty()) {
        loadTableData();
        return;
    }

    String dbColumn;
    boolean isDate = false;

    // Map UI selection → DB column
    switch (column) {
        case "PURCHASE ID":
            dbColumn = "PURCHASE_ID";
            break;
        case "PURCHASE BILL NO":
            dbColumn = "PURCHASE_BILLNO";
            break;
        case "SUPPLIER COMPANY NAME":
            dbColumn = "SUPPLIER_COMPANY_NAME";
            break;
        case "PAYMENT MODE":
            dbColumn = "PAYMENT_MODE";
            break;
        case "PRODUCT NAME":
            dbColumn = "PRODUCT_NAME";
            break;
        case "PRODUCT ID":
            dbColumn = "PRODUCT_ID";
            break;
        case "PURCHASE DATE":
            dbColumn = "PURCHASE_DATE";
            isDate = true;
            break;
        case "DUE DATE":
            dbColumn = "DUE_DATE";
            isDate = true;
            break;
        default:
            dbColumn = "PURCHASE_ID";
    }

    try (Connection con = DB.getConnection()) {

        model.setRowCount(0);

        String sql;
        PreparedStatement ps;

        if (isDate) {
            // Convert input string → SQL Date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate localDate = LocalDate.parse(value, formatter);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            sql = "SELECT * FROM PURCHASE WHERE " + dbColumn + " = ?";
            ps = con.prepareStatement(sql);
            ps.setDate(1, sqlDate);

        } else {
            sql = "SELECT * FROM PURCHASE WHERE " + dbColumn + " LIKE ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + value + "%");
        }

        ResultSet rs = ps.executeQuery();

        // Format dates same as table
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        while (rs.next()) {

            String pDate = rs.getDate("PURCHASE_DATE") == null ? "" :
                    rs.getDate("PURCHASE_DATE").toLocalDate().format(formatter);

            String dDate = rs.getDate("DUE_DATE") == null ? "" :
                    rs.getDate("DUE_DATE").toLocalDate().format(formatter);

            model.addRow(new Object[]{
                    rs.getString("PURCHASE_ID"),
                    pDate,
                    rs.getDouble("TOTAL_GST"),
                    rs.getString("PURCHASE_BILLNO"),
                    rs.getDouble("NET_PURCHASE_RATE"),
                    rs.getDouble("PAID_AMOUNT"),
                    rs.getString("PAYMENT_MODE"),
                    dDate,
                    rs.getString("PRODUCT_ID"),
                    rs.getString("PRODUCT_NAME"),
                    rs.getString("PRODUCT_THICKNESS"),
                    rs.getDouble("PRODUCT_RATE"),
                    rs.getDouble("TOTAL_QUANTITY"),
                    rs.getDouble("DISCOUNT"),
                    rs.getString("SUPPLIER_COMPANY_NAME"),
                    rs.getString("SUPPLIER_ID")
                 
            });
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
    }
}
 private void calculateAmounts() {

    double rate = parseDouble(productRate.getText());
    double qty = parseDouble(totalQuantity.getText());
    double discountVal = parseDouble(discount.getText());
    double paid = parseDouble(paidAmount.getText());

    double gstPercent = 18.0;

    // If no input → reset fields
    if (rate <= 0 || qty <= 0) {
        totalGST.setText("0.00");
        netPurchaseRate.setText("0.00");
        Dueamount.setText("0.00");
        return;
    }

    double basicAmount = rate * qty;
    double gst = (basicAmount * gstPercent) / 100;
    double total = basicAmount + gst;
    double net = total - discountVal;
    double due = net - paid;

    // Safety checks
    if (net < 0) net = 0;
    if (due < 0) due = 0;

    totalGST.setText(String.format("%.2f", gst));
    netPurchaseRate.setText(String.format("%.2f", net));
    Dueamount.setText(String.format("%.2f", due));
}

private void addAutoCalculation(JTextField field) {
    field.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            calculateAmounts();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            calculateAmounts();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            calculateAmounts();
        }
    });
}

    private void loadSuppliers() {
    try {
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(
            "SELECT SUPPLIER_COMPANY_NAME FROM SUPPLIER"
        );
        ResultSet rs = ps.executeQuery();

        suppliercompanyName.removeAllItems();
        suppliercompanyName.addItem(" ");

        while (rs.next()) {
            suppliercompanyName.addItem(rs.getString("SUPPLIER_COMPANY_NAME"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}
public void fetchSupplierID() {
    try {
        Connection con = DB.getConnection();

        PreparedStatement ps = con.prepareStatement(
            "SELECT SUPPLIER_ID FROM SUPPLIER WHERE SUPPLIER_COMPANY_NAME = ?"
        );

        ps.setString(1, suppliercompanyName.getSelectedItem().toString());

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            supplierID.setText(rs.getString("SUPPLIER_ID"));
        } else {
            supplierID.setText("");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

}