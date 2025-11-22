import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private JTextField txtRegUser;
    private JPasswordField txtRegPass;

    public RegisterPanel(CineVerse parent) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("📝 REGISTER ACCOUNT", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Center form (labels + fields)
        JPanel form = new JPanel(new GridLayout(4, 1, 8, 8));

        txtRegUser = new JTextField();
        txtRegPass = new JPasswordField();

        form.add(new JLabel("Choose Username:"));
        form.add(txtRegUser);
        form.add(new JLabel("Choose Password:"));
        form.add(txtRegPass);

        add(form, BorderLayout.CENTER);

        // Bottom button row (centered)
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 8));
        JButton btnCreate = new JButton("Register");
        JButton btnBack = new JButton("Back to Login");

        btnCreate.addActionListener(e -> {
            String user = txtRegUser.getText().trim();
            String pass = new String(txtRegPass.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            } else if (parent.userExists(user)) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
            } else {
                parent.registerUser(user, pass);
                JOptionPane.showMessageDialog(this, "Registration successful!");
                parent.showCard("login");
            }
        });

        btnBack.addActionListener(e -> parent.showCard("login"));

        buttons.add(btnCreate);
        buttons.add(btnBack);

        add(buttons, BorderLayout.SOUTH);
    }
}
