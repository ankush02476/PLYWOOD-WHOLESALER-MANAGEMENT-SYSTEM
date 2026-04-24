import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.awt.print.PrinterJob;

public class AccountsPage extends JPanel {

    function fn = new function();
    JLabel lblTotalPurchase, lblTotalSell, lblProfit;
    JTextField accountID, date, partyName, contactNo, gstamount,refID;
    JTextField netAmount, paidAmount, pendingAmount, dueDate, TXTSEARCH;

    JComboBox<String> transactionType, partyType, paymentStatus;
    JComboBox<String> paymentMode, cashflowType, cmbsearch;

    JButton btnprint ,btnUpdate, SEARCH;

    JTable table;
    DefaultTableModel model;

    public AccountsPage() {

        setLayout(new BorderLayout());
        setBackground(Color.white);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(242,230,213));

        fn.createLabel(mainPanel,"ACCOUNTS MODULE",65,30,16);

        // SEARCH
        fn.createLabel(mainPanel,"SEARCH BY",700,30,16);

        cmbsearch = new JComboBox<>(new String[]{
                "ACCOUNT ID","PARTY NAME","DATE","PAYMENT STATUS","TRANSACTION TYPE","PAYMENT MODE"
        });
        cmbsearch.setBounds(800,30,160,28);
        mainPanel.add(cmbsearch);

        TXTSEARCH = fn.createTextField(mainPanel,"",970,30);
        TXTSEARCH.setBounds(970,30,160,28);

        SEARCH = fn.createRoundButton(mainPanel,"SEARCH",1140,30);
        SEARCH.setBounds(1140,30,160,28);
        fn.addHoverEffect(SEARCH, new Color(10, 10, 10), new Color(230, 160, 160));
        mainPanel.add(SEARCH);

        SEARCH.addActionListener(e->searchAccount());

        // ================= UPPER PANEL =================
        JPanel upperPanel = new JPanel(null);
        upperPanel.setBackground(new Color(250,249,246));
        upperPanel.setBounds(60,60,1240,290);

        int x=20, y=20, gapX=370, gapY=50;

        accountID = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Account ID:",x,y-25,14);
        accountID.setEditable(false);
        accountID.setFocusable(false);


        transactionType = new JComboBox<>(new String[]{
                " ","SELL","PURCHASE","SALARY"
        });
        transactionType.setBounds(x+gapX,y,280,28);
        upperPanel.add(transactionType);
        fn.createLabel(upperPanel,"Transaction Type:",x+gapX,y-25,14);

        paymentMode = new JComboBox<>(new String[]{
                " ","CASH","BANK","UPI"
        });
        paymentMode.setBounds(x+2*gapX,y,280,28);
        upperPanel.add(paymentMode);
        fn.createLabel(upperPanel,"Payment Mode:",x+2*gapX,y-25,14);

        y+=gapY;

        date = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Date:",x,y-25,14);
        date.setEditable(false);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date.setText(today.format(formatter));

        partyName = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"Party Name:",x+gapX,y-25,14);

        partyType = new JComboBox<>(new String[]{
                " ","CUSTOMER","SUPPLIER","EMPLOYEE"
        });
        partyType.setBounds(x+2*gapX,y,280,28);
        upperPanel.add(partyType);
        fn.createLabel(upperPanel,"Party Type:",x+2*gapX,y-25,14);

        y+=gapY;

        contactNo = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Contact No:",x,y-25,14);

        gstamount = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"GST Amount:",x+gapX,y-25,14);

        netAmount = fn.createTextField(upperPanel,"",x+2*gapX,y);
        fn.createLabel(upperPanel,"Net Amount:",x+2*gapX,y-25,14);

        y+=gapY;

        paidAmount = fn.createTextField(upperPanel,"",x,y);
        fn.createLabel(upperPanel,"Paid Amount:",x,y-25,14);

        pendingAmount = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"Pending Amount:",x+gapX,y-25,14);

        paymentStatus = new JComboBox<>(new String[]{
                " ","PAID","PARTIAL","PENDING"
        });
        paymentStatus.setBounds(x+2*gapX,y,280,28);
        upperPanel.add(paymentStatus);
        fn.createLabel(upperPanel,"Payment Status:",x+2*gapX,y-25,14);
        
        
       int refY = y + 50; // adjust vertical position
       refID=fn.createTextField(upperPanel, "", x + 2*gapX, refY);
       fn.createLabel(upperPanel, "Reference ID:", x + 2*gapX, refY - 25, 14);
       
        y+=gapY;

        cashflowType = new JComboBox<>(new String[]{
                " ","IN","OUT"
        });
        cashflowType.setBounds(x,y,280,28);
        upperPanel.add(cashflowType);
        fn.createLabel(upperPanel,"Cashflow Type:",x,y-25,14);

        dueDate = fn.createTextField(upperPanel,"",x+gapX,y);
        fn.createLabel(upperPanel,"Due Date:",x+gapX,y-25,14);

        

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
    "Account ID","Date","Type","Party Name","Party Type",
    "Contact","GST Amount","Net","Paid","Pending",
    "Status","Payment Mode","Cashflow","Due Date","Reference ID"
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


        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
leftPanel.setBackground(Color.WHITE);

lblTotalPurchase = new JLabel("TOTAL PURCHASE: ₹ 0");
lblTotalPurchase.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 14));
lblTotalPurchase.setForeground(Color.RED);

lblTotalSell = new JLabel("TOTAL SELL: ₹ 0");
lblTotalSell.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 14));
lblTotalSell.setForeground(new Color(0,128,0));

lblProfit = new JLabel("NET PROFIT: ₹ 0");
lblProfit.setFont(new Font("TIMES NEW ROMAN", Font.BOLD, 14));
lblProfit.setForeground(Color.BLUE);

leftPanel.add(lblTotalPurchase);
leftPanel.add(lblTotalSell);
leftPanel.add(lblProfit);


        btnprint = new JButton("PRINT");
        btnprint.setBackground(Color.BLACK);
        btnprint.setForeground(Color.WHITE);

        
       JPanel printPanel = new JPanel(new BorderLayout());
       printPanel.setBackground(Color.WHITE);

       printPanel.add(leftPanel, BorderLayout.WEST);
       printPanel.add(btnprint, BorderLayout.EAST);

       lowerPanel.add(printPanel, BorderLayout.SOUTH);

       

        btnprint.addActionListener(e -> printAccount());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });

        mainPanel.add(lowerPanel);
        add(mainPanel);

        // AUTO CALCULATION
        paidAmount.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculatePending();
            }
        });

        netAmount.addKeyListener(new java.awt.event.KeyAdapter(){
            public void keyReleased(java.awt.event.KeyEvent evt){
                calculatePending();
            }
        });

        loadTableData();
        calculateTotals();
    }

    private double parseDouble(String v){
        try{ return Double.parseDouble(v); }
        catch(Exception e){ return 0; }
    }

    private void calculatePending(){
        double net = parseDouble(netAmount.getText());
        double paid = parseDouble(paidAmount.getText());
        double pending = Math.max(0, net - paid);

        pendingAmount.setText(String.valueOf(pending));

       if(pending==0) paymentStatus.setSelectedItem("PAID");
       else if(paid>0) paymentStatus.setSelectedItem("PARTIAL");
       else paymentStatus.setSelectedItem("PENDING");
    }

   public void loadTableData(){
    model.setRowCount(0);

    try(Connection con = DB.getConnection();
        ResultSet rs = con.createStatement().executeQuery(
    "SELECT *FROM ACCOUNT ORDER BY CREATED_AT DESC"
);){

        int count = 0;

        while(rs.next()){
            count++;

            model.addRow(new Object[]{
                rs.getString("ACCOUNT_ID"),
                rs.getString("TRANSACTION_DATE"),
                rs.getString("TRANSACTION_TYPE"),
                rs.getString("PARTY_NAME"),
                rs.getString("PARTY_TYPE"),
                rs.getString("CONTACT_NO"),
                rs.getDouble("GST_AMOUNT"),
                rs.getDouble("NET_AMOUNT"),
                rs.getDouble("PAID_AMOUNT"),
                rs.getDouble("PENDING_AMOUNT"),
                rs.getString("PAYMENT_STATUS"),
                rs.getString("PAYMENT_MODE"),
                rs.getString("CASHFLOW_TYPE"),
                rs.getString("DUE_DATE"),
                rs.getString("REFERENCE_ID")
            });
        }

        System.out.println("ACCOUNT rows loaded: " + count);

        model.fireTableDataChanged();
        table.repaint();

    }catch(Exception e){
        JOptionPane.showMessageDialog(this,e.getMessage());
    }
}

    private void searchAccount(){
        try(Connection con = DB.getConnection()){
            String sql="SELECT * FROM ACCOUNT WHERE PARTY_NAME LIKE ? ORDER BY CREATED_AT DESC";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,"%"+TXTSEARCH.getText()+"%");
            ResultSet rs=ps.executeQuery();

            model.setRowCount(0);

            while(rs.next()){
                model.addRow(new Object[]{
    rs.getString("ACCOUNT_ID"),
    rs.getString("TRANSACTION_DATE"),   
    rs.getString("TRANSACTION_TYPE"),
    rs.getString("PARTY_NAME"),
    rs.getString("PARTY_TYPE"),
    rs.getString("CONTACT_NO"),
    rs.getDouble("GST_AMOUNT"),         
    rs.getDouble("NET_AMOUNT"),
    rs.getDouble("PAID_AMOUNT"),
    rs.getDouble("PENDING_AMOUNT"),
    rs.getString("PAYMENT_STATUS"),
    rs.getString("PAYMENT_MODE"),
    rs.getString("CASHFLOW_TYPE"),
    rs.getString("DUE_DATE"),
    rs.getString("REFERENCE_ID")
});
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    private void fillForm(){
        int row = table.getSelectedRow();
        if(row==-1) return;

        accountID.setText(model.getValueAt(row,0).toString());
        date.setText(model.getValueAt(row,1).toString());
        partyName.setText(model.getValueAt(row,3).toString());
        netAmount.setText(model.getValueAt(row,7).toString());
        paidAmount.setText(model.getValueAt(row,8).toString());
        pendingAmount.setText(model.getValueAt(row,9).toString());
        refID.setText(model.getValueAt(row,14).toString());
    }

   private void printAccount(){

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
                    "ACCOUNT STATEMENT"   // ✅ THIS IS THE FIX
            );

            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(invoice);

            if(job.printDialog()) job.print();

            return null;
        }

        protected void done(){
            processing.dispose();
            JOptionPane.showMessageDialog(
                    AccountsPage.this,
                    "Printed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    };

    worker.execute();
    processing.setVisible(true);
}
private void calculateTotals(){
    double totalPurchase = 0;
    double totalSell = 0;

    try(Connection con = DB.getConnection()){
        String sql = "SELECT TRANSACTION_TYPE, SUM(NET_AMOUNT) AS TOTAL FROM ACCOUNT GROUP BY TRANSACTION_TYPE";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String type = rs.getString("TRANSACTION_TYPE");
            double amount = rs.getDouble("TOTAL");

            if("PURCHASE".equalsIgnoreCase(type)){
                totalPurchase = amount;
            }
            else if("SELL".equalsIgnoreCase(type)){
                totalSell = amount;
            }
        }

        double profit = totalSell - totalPurchase;

        lblTotalPurchase.setText("TOTAL PURCHASE: ₹ " + totalPurchase);
        lblTotalSell.setText("TOTAL SELL: ₹ " + totalSell);
        lblProfit.setText("NET PROFIT: ₹ " + profit);

    }catch(Exception e){
        e.printStackTrace();
    }
}

   
}