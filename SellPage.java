import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.awt.print.PrinterJob;

public class SellPage extends JPanel {
    
    StockPage stockPage;
    private AccountsPage accountsPage;
    function fn = new function();

    JTextField orderID, customerName, customerAddress, customerPhone;
    JTextField orderDate, productRate, productID, gst;
    JTextField dueAmount, productQuantity, totalamount, paidAmount, TXTSEARCH;

    JComboBox<String> paymentMode, cmbthickness;
    JComboBox<String> productname, cmbsearch;

    JButton btnAdd, btnUpdate, btnDelete, SEARCH,btnPrint;

    JTable table;
    DefaultTableModel model;

    public SellPage(StockPage stockPage,AccountsPage accountsPage) {
        this.stockPage = stockPage;
        this.accountsPage = accountsPage;

        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(242,230,213));
        mainPanel.setBounds(0,0,1300,700);

        fn.createLabel(mainPanel,"SELL MODULE",65,30,16);

        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);

        cmbsearch = new JComboBox<>(new String[]{
                "ORDER ID","CUSTOMER NAME","PRODUCT NAME","DATE"
        });

        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);

        TXTSEARCH = fn.createTextField(mainPanel,"",970,30);
        TXTSEARCH.setBounds(970,30,160,28);
        SEARCH = fn.createRoundButton(mainPanel,"SEARCH",1140,30);
        fn.addHoverEffect(SEARCH, new Color(10, 10, 10), new Color(230, 160, 160));
        SEARCH.setBounds(1140,30,160,28);
        mainPanel.add(SEARCH);
        SEARCH.addActionListener(e->searchOrder());

        JPanel upperPanel = new JPanel(null);
        upperPanel.setBackground(new Color(250,249,246));
        upperPanel.setBounds(60,60,1240,290);

        int x=20;
        int y=20;
        int gapX=370;
        int gapY=50;

        orderID = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Order ID:",x,y-25,14);
        orderID.setEditable(false);
        orderID.setFocusable(false);


        productID = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"Product ID:",x+gapX,y-25,14);

        productRate = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"Product Rate:",x+2*gapX,y-25,14);

        y+=gapY;

        orderDate = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Order Date:",x,y-25,14);
        orderDate.setEditable(false);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        orderDate.setText(today.format(formatter));
       

        productQuantity = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"Product Quantity:",x+gapX,y-25,14);

        gst = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"GST:",x+2*gapX,y-25,14);

        y+=gapY;

        customerName = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Customer Name:",x,y-25,14);

        productname = new JComboBox<>(new String[]{
                " ","MR WATERPROOF","COMMERCIAL DPING","COMMERCIAL LOW"
        });

        productname.setBounds(x+gapX,y,280,28);
        upperPanel.add(productname);
        fn.createLabel(upperPanel,"Product Name:",x+gapX,y-25,14);

        paidAmount = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"Paid Amount:",x+2*gapX,y-25,14);

        y+=gapY;

        customerAddress = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Customer Address:",x,y-25,14);

        cmbthickness = new JComboBox<>(new String[]{
                " ","19mm","18mm","16mm","12mm","10mm","6mm","4mm","2mm"
        });

        cmbthickness.setBounds(x+gapX,y,280,28);
        upperPanel.add(cmbthickness);
        fn.createLabel(upperPanel,"Product Thickness:",x+gapX,y-25,14);

        dueAmount = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"Due Amount:",x+2*gapX,y-25,14);

        y+=gapY;

        customerPhone = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Customer PhoneNo:",x,y-25,14);

        paymentMode = new JComboBox<>(new String[]{
                " ","CASH","BANK","UPI",
        });

        paymentMode.setBounds(x+gapX,y,280,28);
        upperPanel.add(paymentMode);
        fn.createLabel(upperPanel,"Payment Mode:",x+gapX,y-25,14);

        totalamount = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"Total Amount:",x+2*gapX,y-25,14);

        btnAdd = fn.createRoundButton(upperPanel,"ADD",1100,100);
        btnAdd.setSize(120, 30);
        btnUpdate = fn.createRoundButton(upperPanel,"UPDATE",1100,150);
        btnUpdate.setSize(120, 30);
        btnDelete = fn.createRoundButton(upperPanel,"DELETE",1100,200);
        btnDelete.setSize(120, 30);


        fn.addHoverEffect(btnAdd, new Color(10,10,10), new Color(230,160,160));
        fn.addHoverEffect(btnUpdate, new Color(10,10,10), new Color(230,160,160));
        fn.addHoverEffect(btnDelete, new Color(10,10,10), new Color(230,160,160));



        btnAdd.addActionListener(e->insertOrder());
        btnUpdate.addActionListener(e->updateOrder());
        btnDelete.addActionListener(e->deleteOrder());

        mainPanel.add(upperPanel);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.setBounds(60,390,1240,350);
        lowerPanel.setBackground(Color.WHITE);

         JLabel tableLabel = new JLabel("SELL DETAILS");
        tableLabel.setFont(new Font("times new roman", Font.BOLD, 16));
        tableLabel.setHorizontalAlignment(JLabel.CENTER);
        tableLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        lowerPanel.add(tableLabel, BorderLayout.NORTH);


        lowerPanel.add(tableLabel,BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "Order ID","Date","C Name","C Address","C Phone",
                "Product ID","Product Name","Product Thickness",
                "Product Quantity","Product Rate","GST",
                "Paid Amount","Due Amount","Grand Total","Payment Mode"
        },0);

        table = new JTable(model);
        table.setShowGrid(true);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250, 249, 246));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        lowerPanel.add(scrollPane, BorderLayout.CENTER);


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
        productname.addActionListener(e->fetchProductID());
        cmbthickness.addActionListener(e->fetchProductID());

        productRate.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculateTotal();
            }
        });

        productQuantity.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculateTotal();
            }
        });

        gst.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculateTotal();
            }
        });

        paidAmount.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculateDue();
            }
        });

        table.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting() && table.getSelectedRow()!=-1){
                int row = table.getSelectedRow();

                orderID.setText(model.getValueAt(row,0).toString());
                orderDate.setText(model.getValueAt(row,1).toString());
                customerName.setText(model.getValueAt(row,2).toString());
                customerAddress.setText(model.getValueAt(row,3).toString());
                customerPhone.setText(model.getValueAt(row,4).toString());

                productID.setText(model.getValueAt(row,5).toString());
                productname.setSelectedItem(model.getValueAt(row,6).toString());
                cmbthickness.setSelectedItem(model.getValueAt(row,7).toString());

                productQuantity.setText(model.getValueAt(row,8).toString());
                productRate.setText(model.getValueAt(row,9).toString());
                gst.setText(model.getValueAt(row,10).toString());

                paidAmount.setText(model.getValueAt(row,11).toString());
                dueAmount.setText(model.getValueAt(row,12).toString());
                totalamount.setText(model.getValueAt(row,13).toString());

                paymentMode.setSelectedItem(model.getValueAt(row,14).toString());
            }
        });


        loadTableData();
    }


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
                    "SELL LIST"   // ✅ FIX ADDED
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
                    SellPage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}
private double parseDouble(String v){
    try{
        if(v == null || v.trim().isEmpty())
            return 0;
        return Double.parseDouble(v.trim());
    }catch(Exception e){
        return 0;
    }
}

   private void calculateTotal() {

    double rate = parseDouble(productRate.getText());
    double qty = parseDouble(productQuantity.getText());
    double gstVal = parseDouble(gst.getText());

    double amount = rate * qty;
    double gstAmount = amount * gstVal / 100;
    double total = amount + gstAmount;

    totalamount.setText(String.format("%.2f", total));

    calculateDue(); // auto update due
}

 private void calculateDue() {

    double total = parseDouble(totalamount.getText());
    double paid = parseDouble(paidAmount.getText());

    if (paid > total) {
        JOptionPane.showMessageDialog(this, "Paid amount cannot exceed total!");
        paidAmount.setText("");
        return;
    }

    double due = total - paid;

    dueAmount.setText(String.format("%.2f", due));
}
        private void fetchProductID(){

        String product = productname.getSelectedItem().toString();
        String thickness = cmbthickness.getSelectedItem().toString();

        if(product.equals(" ") || thickness.equals(" "))
            return;

        try(Connection con = DB.getConnection()){

            String sql="SELECT PRODUCT_ID FROM PRODUCT WHERE PRODUCT_TYPE=? AND PRODUCT_THICKNESS=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1,product);
            ps.setString(2,thickness);

            ResultSet rs = ps.executeQuery();

           if(rs.next()){
           productID.setText(rs.getString("PRODUCT_ID"));
           }else{
           productID.setText("");
           }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

private void insertOrder() {

    if(productID.getText().trim().isEmpty()){
        JOptionPane.showMessageDialog(this, "Please select Product & Thickness!");
        return;
    }

    if(customerName.getText().trim().isEmpty()){
        JOptionPane.showMessageDialog(this, "Customer name required!");
        return;
    }

    try (Connection con = DB.getConnection()) {

        String sql = "INSERT INTO SELL (" +
                "CUSTOMER_NAME, CUSTOMER_ADDRESS, CUSTOMER_PHONE, " +
                "PRODUCT_ID, PRODUCT_NAME, PRODUCT_THICKNESS, " +
                "PRODUCT_QUANTITY, PRODUCT_RATE, GST, " +
                "PAID_AMOUNT, DUE_AMOUNT, GRAND_TOTAL, PAYMENT_MODE" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        // Customer
        ps.setString(1, customerName.getText());
        ps.setString(2, customerAddress.getText());
        ps.setString(3, customerPhone.getText());

        // Product
        ps.setString(4, productID.getText());
        ps.setString(5, productname.getSelectedItem().toString());
        ps.setString(6, cmbthickness.getSelectedItem().toString());

        // Quantity & Rate
        ps.setDouble(7, parseDouble(productQuantity.getText()));
        ps.setDouble(8, parseDouble(productRate.getText()));
        ps.setDouble(9, parseDouble(gst.getText()));

        // Payment
        ps.setDouble(10, parseDouble(paidAmount.getText()));
        ps.setDouble(11, parseDouble(dueAmount.getText()));
        ps.setDouble(12, parseDouble(totalamount.getText()));
        ps.setString(13, paymentMode.getSelectedItem().toString());

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Sell entry added successfully!");

        loadTableData();
        clearFields();

        if (accountsPage != null) {
    SwingUtilities.invokeLater(() -> {
        accountsPage.loadTableData();
    });
}
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Sell Insert Error: " + e.getMessage());
    }
}
private void updateOrder() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select row to update");
        return;
    }

   try (Connection con = DB.getConnection()) {

    con.setAutoCommit(false);

    try {

        String productId = model.getValueAt(row, 5).toString();
        double oldQty = Double.parseDouble(model.getValueAt(row, 8).toString());
        double newQty = parseDouble(productQuantity.getText());

        // Restore old stock
        PreparedStatement ps1 = con.prepareStatement(
            "UPDATE STOCK SET AVAILABLE_QUANTITY = AVAILABLE_QUANTITY + ?, STOCK_OUT = STOCK_OUT - ? WHERE PRODUCT_ID=?"
        );
        ps1.setDouble(1, oldQty);
        ps1.setDouble(2, oldQty);
        ps1.setString(3, productId);
        ps1.executeUpdate();

        if (!checkStockAvailable(con)) {
            con.rollback();   // ✅ ADD
            return;
        }

        // UPDATE SELL
        PreparedStatement ps = con.prepareStatement(
            "UPDATE SELL SET CUSTOMER_NAME=?,CUSTOMER_ADDRESS=?,CUSTOMER_PHONE=?,PRODUCT_RATE=?,PRODUCT_QUANTITY=?,GST=?,PAID_AMOUNT=?,DUE_AMOUNT=?,GRAND_TOTAL=?,PAYMENT_MODE=? WHERE ORDER_ID=?"
        );

        ps.setString(1, customerName.getText());
        ps.setString(2, customerAddress.getText());
        ps.setString(3, customerPhone.getText());
        ps.setDouble(4, parseDouble(productRate.getText()));
        ps.setDouble(5, newQty);
        ps.setDouble(6, parseDouble(gst.getText()));
        ps.setDouble(7, parseDouble(paidAmount.getText()));
        ps.setDouble(8, parseDouble(dueAmount.getText()));
        ps.setDouble(9, parseDouble(totalamount.getText()));
        ps.setString(10, paymentMode.getSelectedItem().toString());
        ps.setString(11, orderID.getText());

        ps.executeUpdate();

        updateStockAfterSale(con);

        con.commit();   

        JOptionPane.showMessageDialog(this, "Updated Successfully");

        if (accountsPage != null) {
    SwingUtilities.invokeLater(() -> {
        accountsPage.loadTableData();
    });
}

        loadTableData();
        clearFields();

    } catch(Exception e){
        con.rollback();   
        throw e;
    }

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
}
}

   private void deleteOrder() {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "Select row to delete");
        return;
    }

   try (Connection con = DB.getConnection()) {

    con.setAutoCommit(false);

    try {

        String productId = model.getValueAt(row, 5).toString();
        double qty = Double.parseDouble(model.getValueAt(row, 8).toString());

        // Restore stock
        PreparedStatement ps1 = con.prepareStatement(
            "UPDATE STOCK SET AVAILABLE_QUANTITY = AVAILABLE_QUANTITY + ?, STOCK_OUT = STOCK_OUT - ? WHERE PRODUCT_ID=?"
        );
        ps1.setDouble(1, qty);
        ps1.setDouble(2, qty);
        ps1.setString(3, productId);
        ps1.executeUpdate();

        // Delete
        PreparedStatement ps = con.prepareStatement("DELETE FROM SELL WHERE ORDER_ID=?");
        ps.setString(1, orderID.getText());
        ps.executeUpdate();

        con.commit();   // ✅ COMMIT

        JOptionPane.showMessageDialog(this, "Deleted Successfully");

        loadTableData();
        clearFields();

    } catch(Exception e){
        con.rollback();   // ✅ ADD
        throw e;
    }

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
}
}
    private void loadTableData(){

        model.setRowCount(0);

        try(Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM SELL ORDER BY ORDER_ID DESC");
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                model.addRow(new Object[]{
                        rs.getString("ORDER_ID"),
                        rs.getDate("ORDER_DATE"),
                        rs.getString("CUSTOMER_NAME"),
                        rs.getString("CUSTOMER_ADDRESS"),
                        rs.getString("CUSTOMER_PHONE"),
                        rs.getString("PRODUCT_ID"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("PRODUCT_THICKNESS"),
                        rs.getDouble("PRODUCT_QUANTITY"),
                        rs.getDouble("PRODUCT_RATE"),
                        rs.getDouble("GST"),
                        rs.getDouble("PAID_AMOUNT"),
                        rs.getDouble("DUE_AMOUNT"),
                        rs.getDouble("GRAND_TOTAL"),
                        rs.getString("PAYMENT_MODE")
                });

            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Load Error: "+e.getMessage());
        }

    }

    private void clearFields(){

        orderID.setText("");
        customerName.setText("");
        customerAddress.setText("");
        customerPhone.setText("");
        productID.setText("");
        productQuantity.setText("");
        productRate.setText("");
        gst.setText("");
        paidAmount.setText("");
        dueAmount.setText("");
        totalamount.setText("");

        paymentMode.setSelectedIndex(0);
        productname.setSelectedIndex(0);
        cmbthickness.setSelectedIndex(0);

    }

    private void searchOrder() {

    String column = cmbsearch.getSelectedItem().toString();
    String value = TXTSEARCH.getText().trim();

    if (value.isEmpty()) {
        loadTableData();
        return;
    }

    String dbColumn;

    switch (column) {
        case "ORDER ID" -> dbColumn = "ORDER_ID";
        case "CUSTOMER NAME" -> dbColumn = "CUSTOMER_NAME";
        case "PRODUCT NAME" -> dbColumn = "PRODUCT_NAME";
        case "DATE" -> dbColumn = "ORDER_DATE";
        default -> dbColumn = "CUSTOMER_NAME";
    }

    String sql = "SELECT * FROM SELL WHERE " + dbColumn + " LIKE ?";

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, "%" + value + "%");

        try (ResultSet rs = ps.executeQuery()) {

            model.setRowCount(0);

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getString("ORDER_ID"),
                        rs.getDate("ORDER_DATE"),
                        rs.getString("CUSTOMER_NAME"),
                        rs.getString("CUSTOMER_ADDRESS"),
                        rs.getString("CUSTOMER_PHONE"),
                        rs.getString("PRODUCT_ID"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("PRODUCT_THICKNESS"),
                        rs.getDouble("PRODUCT_QUANTITY"),
                        rs.getDouble("PRODUCT_RATE"),
                        rs.getDouble("GST"),
                        rs.getDouble("PAID_AMOUNT"),
                        rs.getDouble("DUE_AMOUNT"),
                        rs.getDouble("GRAND_TOTAL"),
                        rs.getString("PAYMENT_MODE")
                });
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
    }
}
   

private boolean checkStockAvailable(Connection con) {

    String productId = productID.getText().trim();
    double sellQty = parseDouble(productQuantity.getText());

    try {

        String sql = "SELECT AVAILABLE_QUANTITY FROM STOCK WHERE PRODUCT_ID=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, productId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            double available = rs.getDouble("AVAILABLE_QUANTITY");

            if (sellQty > available) {

                JOptionPane.showMessageDialog(
                        this,
                        "Stock not available!\nAvailable Quantity: " + available,
                        "Stock Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return false;
            }

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Product not found in stock!",
                    "Stock Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Stock Check Error: " + e.getMessage());
        return false;
    }

    return true;
}
private void updateStockAfterSale(Connection con) throws SQLException {

    String productId = productID.getText().trim();
    double qty = parseDouble(productQuantity.getText());

    String sql = "UPDATE STOCK SET " +
                 "STOCK_OUT = STOCK_OUT + ?, " +
                 "AVAILABLE_QUANTITY = AVAILABLE_QUANTITY - ?, " +
                 "LAST_UPDATED = SYSDATE " +
                 "WHERE PRODUCT_ID = ?";

    PreparedStatement ps = con.prepareStatement(sql);
    ps.setDouble(1, qty);
    ps.setDouble(2, qty);
    ps.setString(3, productId);

    ps.executeUpdate();
    ps.close();
}



    
}
