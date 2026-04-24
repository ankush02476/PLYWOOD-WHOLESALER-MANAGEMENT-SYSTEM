import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class StockPage extends JPanel {

    function fn = new function(); 

    // Updated Fields
    JTextField stockid,productID, stockin, stockout,
            availableQuantity, minStockLevel, lastUpdated, txtsearch;

    JComboBox<String> productName, productThickness, cmbsearch;

    JButton search, btnprint,btnUpdate;

    JTable table;
    DefaultTableModel model;

    public StockPage() {

        setLayout(new BorderLayout());
        setBackground(Color.white);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.setBackground(new Color(242,230,213));

        fn.createLabel(mainPanel, "STOCK MODULE", 65, 30, 16);
        fn.createLabel(mainPanel, "STOCK LIST", 400, 30, 16);
        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);

        cmbsearch = new JComboBox<>(new String[]{
            
                 "STOCK ID","PRODUCT ID","PRODUCT NAME","PRODUCT THICKNESS",
                "AVAILABLE QUANTITY","MINIMUM STOCK LEVEL"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);
       
        txtsearch = fn.createTextField(mainPanel,"",1000,30);
        txtsearch.setBounds(970,30,160,28);

        search = fn.createRoundButton(mainPanel, "SEARCH",1140,30);
        fn.addHoverEffect(search, new Color(10, 10, 10), new Color(230, 160, 160));
        search.setBounds(1140,30,160,28);

        mainPanel.add(search);

        search.addActionListener(e -> searchStock());

        // LEFT FORM PANEL
        
        JPanel formPanel = new JPanel(null);
        formPanel.setPreferredSize(new Dimension(320, 0));
        formPanel.setBackground(new Color(250, 249, 246));

        int y = 20;

        stockid = fn.createTextField(formPanel, "", 20, 30);
        fn.createLabel(formPanel, "Stock ID:", 20, y - 20, 14);
        stockid.setEditable(false);
        stockid.setFocusable(false);

        y += 70;
        productID = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Product ID:", 20, y - 30, 14);
        
        
        y += 70;
        productName = new JComboBox<>(new String[]{
                " ", "MR WATERPROOF", "COMMERCIAL DPING", "COMMERCIAL LOW"
        });
        productName.setBounds(20, y, 280, 30);
        formPanel.add(productName);
        fn.createLabel(formPanel, "Product Name:", 20, y - 30, 14);
        
        
        y += 60;
        productThickness = new JComboBox<>(new String[]{
                " ", "19mm", "18mm", "16mm", "12mm", "10mm", "6mm", "4mm", "2mm"
        });
        productThickness.setBounds(20, y, 280, 30);
        formPanel.add(productThickness);
        fn.createLabel(formPanel, "Product Thickness:", 20, y - 30, 14);
        

        y += 60;
        stockin = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Stock in:", 20, y - 30, 14);
        

        y += 60;
        stockout= fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Stock out:", 20, y - 30, 14);
       

        y += 60;
        availableQuantity = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Available Quantity:", 20, y - 30, 14);

        y += 60;
        minStockLevel = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Minimum Stock Level:", 20, y - 30, 14);

        y += 60;
        lastUpdated = fn.createTextField(formPanel, "", 20, y);
        fn.createLabel(formPanel, "Last Updated:", 20, y - 30, 14);

        btnUpdate = fn.createRoundButton(formPanel, "UPDATE", 20, y + 60);
        fn.addHoverEffect(btnUpdate, new Color(10,10,10), new Color(230,160,160));
        btnUpdate.addActionListener(e -> updateStock());
        

        // RIGHT TABLE PANEL
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel(
                new String[]{
                        "Stock ID",
                        "Product ID",
                        "Product Name",
                        "Product Thickness",
                        "Stock In",
                        "Stock Out",
                        "Available Quantity",
                        "Minimum Stock ",
                        "Last Updated"
                }, 0
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

        // Table Row Click Event//
        table.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
        int row = table.getSelectedRow();
        if (row >= 0) {

            stockid.setText(model.getValueAt(row, 0).toString());
            productID.setText(model.getValueAt(row, 1).toString());
            productName.setSelectedItem(model.getValueAt(row, 2).toString());
            productThickness.setSelectedItem(model.getValueAt(row, 3).toString());
            stockin.setText(model.getValueAt(row, 4).toString());
            stockout.setText(model.getValueAt(row, 5).toString());
            availableQuantity.setText(model.getValueAt(row, 6).toString());
            minStockLevel.setText(model.getValueAt(row, 7).toString());
            lastUpdated.setText(model.getValueAt(row, 8).toString());
        }
    }
});
        mainPanel.add(formPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        loadStockData();
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
                    "STOCK LIST"   // ✅ FIX ADDED
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
                    StockPage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}

private void updateStock() {

    if (stockid.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please select a row first!");
        return;
    }

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "UPDATE STOCK SET AVAILABLE_QUANTITY=?, MIN_STOCK_LEVEL=?, LAST_UPDATED=SYSDATE WHERE STOCK_ID=?")) {

        ps.setDouble(1, Double.parseDouble(availableQuantity.getText()));
        ps.setDouble(2, Double.parseDouble(minStockLevel.getText()));
        ps.setString(3, stockid.getText());

        ps.executeUpdate();

        JOptionPane.showMessageDialog(this, "Stock Updated Successfully!");

        loadStockData();
        clearFields();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
    }
}

private void clearFields() {

    stockid.setText("");
    productID.setText("");
    stockin.setText("");
    stockout.setText("");
    availableQuantity.setText("");
    minStockLevel.setText("");
    lastUpdated.setText("");

    productName.setSelectedIndex(0);
    productThickness.setSelectedIndex(0);

    availableQuantity.setEditable(false);
    minStockLevel.setEditable(false);

    table.clearSelection();
}

private void searchStock() {

    String column = cmbsearch.getSelectedItem().toString();
    String value = txtsearch.getText().trim();

    if (value.isEmpty()) {
        loadStockData();
        return;
    }

    String dbColumn;

    switch (column) {
        case "STOCK ID" -> dbColumn = "STOCK_ID";
        case "PRODUCT ID" -> dbColumn = "PRODUCT_ID";
        case "PRODUCT NAME" -> dbColumn = "PRODUCT_NAME";
        case "PRODUCT THICKNESS" -> dbColumn = "PRODUCT_THICKNESS";
        case "AVAILABLE QUANTITY" -> dbColumn = "AVAILABLE_QUANTITY";
        case "MINIMUM STOCK LEVEL" -> dbColumn = "MIN_STOCK_LEVEL";
        default -> dbColumn = "STOCK_ID";
    }

    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM STOCK WHERE " + dbColumn + " LIKE ? ORDER BY LAST_UPDATED DESC")) {

        ps.setString(1, "%" + value + "%");

        ResultSet rs = ps.executeQuery();
        model.setRowCount(0);

        while (rs.next()) {
            model.addRow(new Object[]{
                    rs.getString("STOCK_ID"),
                    rs.getString("PRODUCT_ID"),
                    rs.getString("PRODUCT_NAME"),
                    rs.getString("PRODUCT_THICKNESS"),
                    rs.getDouble("STOCK_IN"),
                    rs.getDouble("STOCK_OUT"),
                    rs.getDouble("AVAILABLE_QUANTITY"),
                    rs.getDouble("MIN_STOCK_LEVEL"),
                    rs.getDate("LAST_UPDATED")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Search Error: " + e.getMessage());
    }
}





public void loadStockData() {
    model.setRowCount(0);
    try (Connection con = DB.getConnection();
         PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM STOCK ORDER BY LAST_UPDATED DESC");
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            model.addRow(new Object[]{
                    rs.getString("STOCK_ID"),
                    rs.getString("PRODUCT_ID"),
                    rs.getString("PRODUCT_NAME"),
                    rs.getString("PRODUCT_THICKNESS"),
                    rs.getDouble("STOCK_IN"),
                    rs.getDouble("STOCK_OUT"),
                    rs.getDouble("AVAILABLE_QUANTITY"),
                    rs.getDouble("MIN_STOCK_LEVEL"),
                    rs.getDate("LAST_UPDATED")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                "Load Error: " + e.getMessage());
    }
}


}