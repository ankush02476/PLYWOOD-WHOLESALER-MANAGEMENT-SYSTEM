import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;



public class login extends JFrame implements ActionListener
{
  JPanel leftPanel,rightPanel;
  JButton Loginbtn,registerbtn;
  JTextField userf,pass;

  function fn=new function();
  register rg=new register(this);
  public login()
  {
    setTitle("JAI MAA JAGDAMBA PLY");
    setSize(900,450);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new GridLayout(1,2));
    setResizable(false);

     // Left Panel //
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.decode("#4A4A4A"));
        leftPanel.setLayout(null);

        ImagePanel leftPanel = new ImagePanel("/image/plwood logo.jpg");
      

     // Right Panel //
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        //function call//
    
       fn.createLabel(rightPanel, "LOGIN", 80, 30, 24);
       fn. createLabel(rightPanel, "USER NAME", 80, 70, 14);
       userf = fn. createTextField(rightPanel,"",80,100);
       fn. createLabel(rightPanel,"PASSWORD",80 ,140,14);
       pass = fn. createTextField(rightPanel,"",80,170);
       fn.createLabel(rightPanel,"DON'T HAVE AN ACCOUNT",125,270,14);

        
        //rounded buttons//
        Loginbtn=fn.createRoundButton(rightPanel,"LOGIN",80,220);
        registerbtn=fn.createRoundButton(rightPanel,"REGISTER",80,300);
        
        fn.addHoverEffect(Loginbtn, new Color(0,0,0), new Color(230,160,160));
        fn.addHoverEffect(registerbtn,new Color(0,0,0),new Color(230,160,160));
        



        Loginbtn.addActionListener(this);
        registerbtn.addActionListener(this);

        
        

        add(leftPanel);
        add(rightPanel);

    

        setVisible(true);
        
        
  }
   public void showLoginPanel() {
        getContentPane().remove(1);
        add(rightPanel, 1);
        revalidate();
        repaint();
        }

   class ImagePanel extends JPanel {
        private Image image;
        ImagePanel(String path) {
            image = new ImageIcon(getClass().getResource(path)).getImage();
            setLayout(null);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }


public void actionPerformed(ActionEvent e) {

    if (e.getSource() == Loginbtn) {

        String username = userf.getText().trim();
        String password = pass.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all details");
            return;
        }

        try {
            Connection con = DB.getConnection();

            String sql = "SELECT 1 FROM USERS WHERE USER_NAME = ? AND PASSWORD = ?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // BOTH username & password matched
                JOptionPane.showMessageDialog(this, "Login successful");

                new module();   // open module
                dispose();      // close login window

            } else {
                // Either username or password is wrong
                JOptionPane.showMessageDialog(this, "Please enter correct username or password");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error");
        }
    }


    else if(e.getSource()==registerbtn){
        getContentPane().remove(rightPanel);
        register rg=new register(this);
        getContentPane().add(rg,1);
        revalidate();
        repaint();
    }

}

public static void main(String args[]){
    new login();
}
}