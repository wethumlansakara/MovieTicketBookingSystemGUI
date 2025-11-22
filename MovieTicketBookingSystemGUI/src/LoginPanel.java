import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField txtLoginUser;
    private JPasswordField txtLoginPass;

    public LoginPanel(CineVerse parent) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        JLabel title = new JLabel("🎟️ CINEVERSE LOGIN", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        // form area (labels + fields)
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        txtLoginUser = new JTextField();
        txtLoginPass = new JPasswordField();

        form.add(new JLabel("Username:"));
        form.add(txtLoginUser);
        form.add(new JLabel("Password:"));
        form.add(txtLoginPass);

        // bottom button row
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
    JButton btnLogin = new JButton("Login");
    JButton btnReset = new JButton("Reset");
    JButton btnResetAll = new JButton("Reset All Seats");
    JButton btnRegister = new JButton("Register");

        btnLogin.addActionListener(e -> {
            String user = txtLoginUser.getText();
            String pass = new String(txtLoginPass.getPassword());

            if (parent.authenticate(user, pass)) {
                parent.setCurrentUser(user);
                parent.showCard("menu");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        });

        btnReset.addActionListener(e -> {
            txtLoginUser.setText("");
            txtLoginPass.setText("");
        });

        btnRegister.addActionListener(e -> parent.showCard("register"));

        buttonRow.add(btnLogin);
        buttonRow.add(btnReset);
        buttonRow.add(btnResetAll);
        buttonRow.add(btnRegister);

        // Password-protected reset (password = "1234")
        btnResetAll.addActionListener(e -> {
            JPasswordField pwd = new JPasswordField();
            Object[] message = {"Enter admin password to reset all seats:", pwd};
            int option = JOptionPane.showConfirmDialog(this, message, "Admin Reset",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String entered = new String(pwd.getPassword());
                if ("1234".equals(entered)) {
                    parent.resetSeats();
                    JOptionPane.showMessageDialog(this, "All seats have been reset to full availability.");
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect password. Reset aborted.");
                }
            }
        });

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(buttonRow, BorderLayout.SOUTH);
    }
}
