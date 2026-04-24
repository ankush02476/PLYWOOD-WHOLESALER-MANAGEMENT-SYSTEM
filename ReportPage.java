import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.awt.print.PrinterJob;

public class ReportPage extends JPanel {

    function fn = new function();

    JComboBox<String> cmbReport;
    JTextField fromDate, toDate;

    JTable table;
    DefaultTableModel model;

    JButton btnGenerate, btnPrint;

    public ReportPage() {

        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        mainPanel.setBackground(new Color(242,230,213));

        fn.createLabel(mainPanel, "REPORT MODULE", 65, 30, 16);
        fn.createLabel(mainPanel, "REPORT LIST", 400, 30, 16);

        // LEFT PANEL (FILTER)
        JPanel filterPanel = new JPanel(null);
        filterPanel.setPreferredSize(new Dimension(320, 0));
        filterPanel.setBackground(new Color(250, 249, 246));

        int y = 40;

        cmbReport = new JComboBox<>(new String[]{
               "             ", 
               "PRODUCT REPORT",
               "PURCHASE REPORT",
               "STOCK REPORT",
               "SELL REPORT",
               "EMPLOYEE REPORT",
               "ACCOUNT REPORT",
               "SUPPLIER REPORT"

        });
        cmbReport.setBounds(20, y, 280, 30);
        filterPanel.add(cmbReport);
        fn.createLabel(filterPanel, "Select Report:", 20, y - 25, 14);

        y += 70;
        fromDate = fn.createTextField(filterPanel, "", 20, y);
        fn.createLabel(filterPanel, "From Date (YYYY-MM-DD):", 20, y - 25, 14);

        y += 70;
        toDate = fn.createTextField(filterPanel, "", 20, y);
        fn.createLabel(filterPanel, "To Date:", 20, y - 25, 14);

        y += 70;
        btnGenerate = fn.createRoundButton(filterPanel, "GENERATE", 20, y);
        fn.addHoverEffect(btnGenerate, new Color(10,10,10), new Color(230,160,160));

        btnGenerate.addActionListener(e -> loadReport());

        // RIGHT PANEL (TABLE)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        model = new DefaultTableModel();
        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(250, 249, 246));
        header.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // PRINT BUTTON
        btnPrint = new JButton("PRINT");
        btnPrint.setForeground(Color.WHITE);
        btnPrint.setPreferredSize(new Dimension(100, 40));
        fn.addHoverEffect(btnPrint, new Color(10,10,10), new Color(230,160,160));

        btnPrint.addActionListener(e -> printReport());

        JPanel printPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printPanel.setBackground(Color.WHITE);
        printPanel.add(btnPrint);

        tablePanel.add(printPanel, BorderLayout.SOUTH);

        mainPanel.add(filterPanel, BorderLayout.WEST);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // LOAD REPORT DATA
    private void loadReport() {

        String type = cmbReport.getSelectedItem().toString();
        String from = fromDate.getText().trim();
        String to = toDate.getText().trim();

        model.setRowCount(0);
        model.setColumnCount(0);

        try {
            Connection con = DB.getConnection();

            PreparedStatement ps = null;

            switch (type) {

                case "SELL REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "Order ID","Customer","Product","Qty","Total","Date"
                    });

                    ps = con.prepareStatement(
                            "SELECT ORDER_ID, CUSTOMER_NAME, PRODUCT_NAME, PRODUCT_QUANTITY, GRAND_TOTAL, ORDER_DATE " +
"FROM SELL WHERE ORDER_DATE BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') " +
"ORDER BY TO_NUMBER(REGEXP_SUBSTR(ORDER_ID, '[0-9]+'))"
                    );
                    ps.setString(1, from);
                    ps.setString(2, to);
                    break;

                case "PURCHASE REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "Purchase ID","Supplier","Product","Qty","Amount","Date"
                    });

                    ps = con.prepareStatement(
                           "SELECT PURCHASE_ID, SUPPLIER_COMPANY_NAME, PRODUCT_NAME, TOTAL_QUANTITY, NET_PURCHASE_RATE, PURCHASE_DATE " +
"FROM PURCHASE WHERE PURCHASE_DATE BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') " +
"ORDER BY TO_NUMBER(REGEXP_SUBSTR(PURCHASE_ID, '[0-9]+'))"
                    );
                    ps.setString(1, from);
                    ps.setString(2, to);
                    break;

                case "STOCK REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "Product","Stock In","Stock Out","Available"
                    });

                    ps = con.prepareStatement(
                            "SELECT PRODUCT_NAME, STOCK_IN, STOCK_OUT, AVAILABLE_QUANTITY FROM STOCK " +
"ORDER BY PRODUCT_NAME"
                    );
                    break;

                case "PRODUCT REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "ID","Type","Thickness","Rate","Status"
                    });

                    ps = con.prepareStatement(
                            "SELECT PRODUCT_ID, PRODUCT_TYPE, PRODUCT_THICKNESS, PRODUCT_RATE, STATUS FROM PRODUCT " +
"ORDER BY TO_NUMBER(REGEXP_SUBSTR(PRODUCT_ID, '[0-9]+'))"
                    );
                    break;

                case "EMPLOYEE REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "ID","Name","Phone","Salary","Status"
                    });

                    ps = con.prepareStatement(
                            "SELECT EMPLOYEE_ID, EMPLOYEE_NAME, PHONE_NO, SALARY, STATUS FROM EMPLOYEE " +
"ORDER BY TO_NUMBER(REGEXP_SUBSTR(EMPLOYEE_ID, '[0-9]+'))"
                    );
                    break;

                case "SUPPLIER REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "ID","Company","Phone","GST","Status"
                    });

                    ps = con.prepareStatement(
                            "SELECT SUPPLIER_ID, SUPPLIER_COMPANY_NAME, PHONE, GST_NO, STATUS FROM SUPPLIER " +
"ORDER BY TO_NUMBER(REGEXP_SUBSTR(SUPPLIER_ID, '[0-9]+'))"
                    );
                    break;

                case "ACCOUNT REPORT":
                    model.setColumnIdentifiers(new String[]{
                            "Date","Party","Type","Cashflow","Amount","Status"
                    });

                    ps = con.prepareStatement(
                          "SELECT TRANSACTION_DATE, PARTY_NAME, TRANSACTION_TYPE, CASHFLOW_TYPE, NET_AMOUNT, PAYMENT_STATUS " +
"FROM ACCOUNT WHERE TRANSACTION_DATE BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD') " +
"ORDER BY TRANSACTION_DATE"
                    );
                    ps.setString(1, from);
                    ps.setString(2, to);
                    break;
            }

            ResultSet rs = ps.executeQuery();
            int cols = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR LOADING REPORT");
        }
    }

    // PRINT FUNCTION (SAME STYLE)
    private void printReport() {

        JDialog processing = new JDialog((Frame)null, "Printing", true);
        processing.add(new JLabel("Processing... Please wait", JLabel.CENTER));
        processing.setSize(250, 100);
        processing.setLocationRelativeTo(this);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {

            protected Void doInBackground() throws Exception {

                PrintableInvoice invoice = new PrintableInvoice(
                        "JAI MAA JAGDAMBA PLY",
                        "PATNA",
                        "PHONE NO:9122678054",
                        table,
                        cmbReport.getSelectedItem().toString()
                );

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(invoice);

                if (job.printDialog()) job.print();

                return null;
            }

            protected void done() {
                processing.dispose();
                JOptionPane.showMessageDialog(
                        ReportPage.this,
                        "Printed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        };

        worker.execute();
        processing.setVisible(true);
    }
}