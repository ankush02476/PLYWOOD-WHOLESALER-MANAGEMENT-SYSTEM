import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintableInvoice implements Printable {

    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private JTable table;
    private Image logo;
    private String pageTitle;

    private static String lastDate = "";
    private static int counter = 0;

    public PrintableInvoice(String name, String address, String phone, JTable table, String pageTitle) {
        this.companyName = name;
        this.companyAddress = address;
        this.companyPhone = phone;
        this.table = table;
        this.pageTitle = pageTitle;

        logo = new ImageIcon("image/plwood logo.jpg").getImage();
    }

    private String generateInvoiceNumber() {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());

        if (!today.equals(lastDate)) {
            counter = 1;
            lastDate = today;
        } else {
            counter++;
        }

        return today + "-" + String.format("%03d", counter);
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {

        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2 = (Graphics2D) g;
        g2.translate(pf.getImageableX(), pf.getImageableY());

        double pageWidth = pf.getImageableWidth() - 40;
        double tableWidth = table.getColumnModel().getTotalColumnWidth();

        double scale = (tableWidth > pageWidth) ? (pageWidth / tableWidth) : 1.0;
        g2.scale(scale, scale);

        int margin = (int) (20 / scale);
        int pageW = (int) (pf.getImageableWidth() / scale);
        int pageH = (int) (pf.getImageableHeight() / scale);

        int y = margin;

        // ================= BORDER =================
        g2.drawRect(0, 0, pageW, pageH);

        // ================= LOGO =================
        int logoWidth = 180;
        int logoHeight = 120;
        g2.drawImage(logo, margin, y, logoWidth, logoHeight, null);

        // ================= COMPANY INFO =================
        int textX = margin + logoWidth + 30;
        int textY = y + 30;

        g2.setFont(new Font("Times New Roman", Font.BOLD, 18));
        g2.drawString(companyName, textX, textY);

        g2.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        g2.drawString(companyAddress, textX, textY + 30);
        g2.drawString(companyPhone, textX, textY + 55);

        y += logoHeight + 25;
        g2.drawLine(margin, y, pageW - margin, y);

        // ================= INVOICE INFO =================
        String invoiceNo = generateInvoiceNumber();
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        y += 25;
        g2.drawString("Invoice No: " + invoiceNo, margin, y);
        g2.drawString("Date: " + date, pageW - 180, y);

        y += 15;
        g2.drawLine(margin, y, pageW - margin, y);

        // ================= TITLE =================
        y += 30;
        g2.setFont(new Font("Times New Roman", Font.BOLD, 16));

        int titleWidth = g2.getFontMetrics().stringWidth(pageTitle);
        g2.drawString(pageTitle, (pageW - titleWidth) / 2, y);

        y += 20;
        g2.drawLine(margin, y, pageW - margin, y);
        y += 25;

        // ================= TABLE HEADER =================
        int rowHeight = 25;
        int x;

        g2.setFont(new Font("Times New Roman", Font.BOLD, 12));
        x = margin;

        for (int col = 0; col < table.getColumnCount(); col++) {

            int colWidth = table.getColumnModel().getColumn(col).getWidth();

            g2.drawRect(x, y, colWidth, rowHeight);
            g2.drawString(table.getColumnName(col), x + 5, y + 17);

            x += colWidth;
        }

        y += rowHeight;

        // ================= TABLE ROWS =================
        g2.setFont(new Font("Times New Roman", Font.PLAIN, 11));

        for (int row = 0; row < table.getRowCount(); row++) {

            x = margin;

            for (int col = 0; col < table.getColumnCount(); col++) {

                int colWidth = table.getColumnModel().getColumn(col).getWidth();

                g2.drawRect(x, y, colWidth, rowHeight);

                Object value = table.getValueAt(row, col);
                g2.drawString(String.valueOf(value), x + 5, y + 17);

                x += colWidth;
            }

            y += rowHeight;
        }

        return PAGE_EXISTS;
    }
}