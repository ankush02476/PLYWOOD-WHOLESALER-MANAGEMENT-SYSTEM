import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//function for label//
public class function extends JFrame {

   



    //function for label//

        public JLabel createLabel(JPanel panel, String text, int x, int y, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 300, 30);
        lbl.setFont(new Font("times new roman", Font.BOLD, size));
        lbl.setForeground(Color.black);
        panel.add(lbl);
        return lbl;    
     }
    
    //function for textfield//

        public JTextField createTextField(JPanel panel, String text, int x, int y) {
        JTextField txt = new JTextField(text);
        txt.setBounds(x, y, 280, 30);
        panel.add(txt);
        return txt;
    }
    
    //function for button//

        public JButton createButton(JPanel panel,String text,int x,int y){
        JButton btn=new JButton(text);
        btn.setBounds(x, y, 280, 30);
        panel.add(btn);
        return btn;
        }
    
     //function for rounded buttons//

    public JButton createRoundButton(JPanel panel, String text, int x, int y) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
               
            }

            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
        };

        btn.setBounds(x, y, 280, 35);
        btn.setBackground(new Color(10,10,10)); 
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);


        panel.add(btn);
        return btn;
    }

     public void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
     });
    
}
}
        

