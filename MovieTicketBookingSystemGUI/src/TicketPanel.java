import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicketPanel extends JPanel {
    private JTextArea ticketArea;

    public TicketPanel(CineVerse parent) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🎫 Your Ticket", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));

        ticketArea = new JTextArea();
        ticketArea.setEditable(false);
        ticketArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        JButton btnBack = new JButton("Back to Menu");
        JButton btnCancelBooking = new JButton("Cancel Booking");
        JButton btnLogout = new JButton("Logout");

        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showCard("menu");
            }
        });

        // Cancel booking: only allow the current user to cancel their own bookings
        btnCancelBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = parent.getCurrentUser();
                java.util.Map<String, Integer> bookings = parent.getUserBookings(user);
                if (bookings == null || bookings.isEmpty()) {
                    JOptionPane.showMessageDialog(TicketPanel.this, "You have no bookings to cancel.");
                    return;
                }

                // let user pick which seat and quantity to cancel
                String[] seats = bookings.keySet().toArray(new String[0]);
                JComboBox<String> seatChooser = new JComboBox<>(seats);
                int maxQty = bookings.get(seats[0]);
                SpinnerNumberModel model = new SpinnerNumberModel(1, 1, maxQty, 1);
                JSpinner qtySpinner = new JSpinner(model);

                // update spinner max when seat selection changes
                seatChooser.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        String s = (String) seatChooser.getSelectedItem();
                        int available = bookings.getOrDefault(s, 0);
                        qtySpinner.setModel(new SpinnerNumberModel(1, 1, Math.max(1, available), 1));
                    }
                });

                JPanel panel = new JPanel(new GridLayout(2, 2, 8, 8));
                panel.add(new JLabel("Seat:"));
                panel.add(seatChooser);
                panel.add(new JLabel("Quantity to cancel:"));
                panel.add(qtySpinner);

                int res = JOptionPane.showConfirmDialog(TicketPanel.this, panel, "Cancel Your Booking",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String seat = (String) seatChooser.getSelectedItem();
                    int qty = (Integer) qtySpinner.getValue();
                    boolean ok = parent.cancelBooking(user, seat, qty);
                    if (ok) {
                        JOptionPane.showMessageDialog(TicketPanel.this, "Cancelled " + qty + " x " + seat + " successfully.");
                    } else {
                        JOptionPane.showMessageDialog(TicketPanel.this, "Cancellation failed. Check your bookings and try again.");
                    }
                }
            }
        });

        btnLogout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.showCard("login");
            }
        });

        bottom.add(btnBack);
        bottom.add(btnCancelBooking);
        bottom.add(btnLogout);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(ticketArea), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void setTicketText(String text) {
        ticketArea.setText(text);
    }
}