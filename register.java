import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class register extends JPanel implements ActionListener {

    function fn = new function();
    JButton reg, back;
    JTextField userField, passField;
    login parent; 

    public register(login parentFrame) {
        this.parent = parentFrame;

        setLayout(null);
        setBackground(Color.WHITE);
        setBounds(0, 0, 450, 450);

        fn.createLabel(this, "REGISTER", 80, 30, 20);
        fn.createLabel(this, "USERNAME", 80, 80, 14);
        userField = fn.createTextField(this, "", 80, 110);

        fn.createLabel(this, "PASSWORD", 80, 150, 14);
        passField = fn.createTextField(this, "", 80, 180);

        reg = fn.createRoundButton(this, "REGISTER", 80, 230);
        back = fn.createRoundButton(this, "BACK TO LOGIN", 80, 300);

        fn.addHoverEffect(reg, new Color(0, 0, 0), new Color(230, 160, 160));
        fn.addHoverEffect(back, new Color(0, 0, 0), new Color(230, 160, 160));

        reg.addActionListener(this);
        back.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == back) {
            Container parentContainer = parent.getContentPane();
            parentContainer.remove(this);          
            parentContainer.add(parent.rightPanel, 1);
            parent.revalidate();
            parent.repaint();
            return; 
        }

        if (e.getSource() == reg) {
            String username = userField.getText();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all details");
                return;
            }

            try {
                Connection con = DB.getConnection();
                String sql = "INSERT INTO USERS VALUES (?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Registration Successful");

                ps.close();
                con.close();
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(this, "Username already exists");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
