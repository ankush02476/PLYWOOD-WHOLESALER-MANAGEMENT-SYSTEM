import javax.swing.*;
import java.awt.*;

public class module extends JFrame {

    JPanel sidebar, mainPanel;
    CardLayout card;
    function fn = new function(); 
    
    
    public module() {

        setTitle("JAI MAA JAGDAMBA PLY");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar //
        sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(10, 1, 10, 25));
        sidebar.setBackground(Color.white);
        sidebar.setPreferredSize(new Dimension(170, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        JPanel item = createImageLabelLine("image/landscape.png", "JMJP", 40);
        sidebar.add(item);


        // Main Panel //
        card = new CardLayout();
        mainPanel = new JPanel(card);

        // Pages //
        mainPanel.add(createPage("WELCOME","image/plwood logo.jpg") ,"");
        mainPanel.add(new ProductPage(), "PRODUCT");
        StockPage stockPage = new StockPage();
        AccountsPage accountpage = new AccountsPage();
        SellPage sellPage = new SellPage(stockPage,accountpage);
        EmployeePage employeePage= new EmployeePage(accountpage);
        PurchasePage purchasePage = new PurchasePage(stockPage,accountpage);
        SupplierPage supplierpage = new SupplierPage(purchasePage);
        ReportPage reportpage=new ReportPage();
        mainPanel.add(purchasePage, "PURCHASE");
        mainPanel.add(stockPage, "STOCK");
        mainPanel.add(sellPage, "SELL");
        mainPanel.add(employeePage, "EMPLOYEE");
        mainPanel.add(accountpage, "ACCOUNT");
        mainPanel.add(supplierpage, "SUPPLIER");
        mainPanel.add(reportpage, "REPORT");

        // Sidebar Buttons //
        
        addSidebarButton("PRODUCT", "PRODUCT", "image/product.png");
        addSidebarButton("PURCHASE", "PURCHASE", "image/purchase.png");
        addSidebarButton("STOCK", "STOCK", "image/stock.png");
        addSidebarButton("SELL", "SELL", "image/payment.png");
        addSidebarButton("EMPLOYEE", "EMPLOYEE", "image/employee.png");
        addSidebarButton("ACCOUNT", "ACCOUNT", "image/accounting.png");
        addSidebarButton("SUPPLIER", "SUPPLIER", "image/supplier.png");
        addSidebarButton("REPORT", "REPORT", "image/report.png");
       
       
        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        card.show(mainPanel, "WELCOME");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // Sidebar Button Method //
    
    void addSidebarButton(String text, String page, String iconPath) {

        JButton btn = fn.createRoundButton(sidebar, text, 0, 0);
        
        //  for icon //
        ImageIcon icon = new ImageIcon(iconPath);
        if (icon.getIconWidth() > 0) {
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
        }

        fn.addHoverEffect(btn, Color.BLACK, new Color(230, 160, 160));
        btn.addActionListener(e -> card.show(mainPanel, page));
    }


    // function for page //

   JPanel createPage(String text, String imagePath)
    {
    JPanel panel = new JPanel(new BorderLayout()) {
        Image img = new ImageIcon(imagePath).getImage();

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    };

    // label for page //
    JLabel textLabel = new JLabel(text);
    textLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
    textLabel.setHorizontalAlignment(SwingConstants.CENTER);
    textLabel.setForeground(Color.red); 
    textLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(textLabel, BorderLayout.NORTH);

    return panel;
}
// Function to create a horizontal panel with an image and label //
JPanel createImageLabelLine(String imagePath, String labelText, int imageSize) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    panel.setBackground(Color.WHITE);

    // Load image safely//
    Image img = null;
    java.net.URL url = getClass().getResource(imagePath);
    if (url != null) {
        img = new ImageIcon(url).getImage().getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
    }

    // Image label//
    JLabel imgLabel = new JLabel();
    if (img != null) {
        imgLabel.setIcon(new ImageIcon(img));
    }
    panel.add(imgLabel);

    // Text label//
    JLabel textLabel = new JLabel(labelText);
    textLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
    panel.add(textLabel);

    return panel;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new module()); 
    }
}