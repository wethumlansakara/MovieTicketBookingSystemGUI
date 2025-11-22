import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JComboBox<String> movieBox, timeBox, seatBox;
    private JComboBox<String> seatTypeBox;
    private JSpinner qtySpinner;
    private JLabel priceLabel;
    private JLabel availabilityLabel;

    public MenuPanel(CineVerse parent) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("🎥 Select Movie Details", JLabel.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));

        movieBox = new JComboBox<>(new String[]{"Avengers", "Superman", "Batman"});
        timeBox = new JComboBox<>(new String[]{"10:00 AM", "1:00 PM", "6:00 PM"});

        // Seat type (affects price) - show this first
        seatTypeBox = new JComboBox<>(new String[]{"Regular", "VIP"});

        // Seat selector will be populated based on seat type
        seatBox = new JComboBox<>();

        // when seat type changes, update available seat list
        seatTypeBox.addActionListener(ae -> {
            updateSeatList(parent);
            updateAvailabilityLabel(parent);
            updatePrice(parent);
        });

        // when seat selection changes, update availability and price
        seatBox.addActionListener(ae -> {
            updateAvailabilityLabel(parent);
            updatePrice(parent);
        });

        // when quantity changes, update availability check and price
        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        qtySpinner.addChangeListener(e -> {
            updateAvailabilityLabel(parent);
            updatePrice(parent);
        });

        // initialize seats based on default selection
        updateSeatList(parent);

        priceLabel = new JLabel("Ticket Price: -");
        availabilityLabel = new JLabel("Available: -");

        JButton calcBtn = new JButton("Calculate Price");
        calcBtn.addActionListener(e -> updatePrice(parent));

        form.add(new JLabel("Movie:"));
        form.add(movieBox);
        form.add(new JLabel("Show Time:"));
        form.add(timeBox);
        form.add(new JLabel("Seat Type:"));
        form.add(seatTypeBox);
        form.add(new JLabel("Seat:"));
        form.add(seatBox);
        form.add(new JLabel("Quantity:"));
        form.add(qtySpinner);
        form.add(calcBtn);
        form.add(priceLabel);
        form.add(new JLabel("Availability:"));
        form.add(availabilityLabel);

        JPanel bottom = new JPanel();
        JButton btnConfirm = new JButton("Confirm Booking");
        JButton btnLogout = new JButton("Logout");

        btnConfirm.addActionListener(e -> {
            String movie = (String) movieBox.getSelectedItem();
            String time = (String) timeBox.getSelectedItem();
            String seat = (String) seatBox.getSelectedItem();
            String type = (String) seatTypeBox.getSelectedItem();
            int qty = (Integer) qtySpinner.getValue();
            double perSeat = parent.getTicketPrice();
            double total = perSeat * qty;

            // Check seat availability before booking
            if (!parent.isSeatAvailable(seat, qty)) {
                JOptionPane.showMessageDialog(this,
                        "Not enough seats available!\n" +
                                "Requested: " + qty + "\n" +
                                "Available: " + parent.getAvailableSeats(seat));
                return;
            }

            // Book the seats
            parent.bookSeats(seat, qty);

            String ticket = String.format("""
                ------------------------------
                   🎬 CINEVERSE TICKET
                ------------------------------
                User: %s
                Movie: %s
                Time: %s
                Seat: %s
                Type: %s
                Quantity: %d
                Per Seat: $%.2f
                Total: $%.2f
                ------------------------------
                Enjoy your movie! 🍿
                """, parent.getCurrentUser(), movie, time, seat, type, qty, perSeat, total);

            parent.displayTicket(ticket);
        });

        btnLogout.addActionListener(e -> parent.showCard("login"));

        bottom.add(btnLogout);
        bottom.add(btnConfirm);

        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Initialize price and availability labels
        updatePrice(parent);
        updateAvailabilityLabel(parent);
    }

    private void updatePrice(CineVerse parent) {
        String movie = (String) movieBox.getSelectedItem();
        String type = (String) seatTypeBox.getSelectedItem();
        int qty = (Integer) qtySpinner.getValue();

        double basePrice;
        if ("Avengers".equals(movie)) basePrice = 15.0;
        else if ("Superman".equals(movie)) basePrice = 12.0;
        else basePrice = 9.0;

        double typeMultiplier = "VIP".equals(type) ? 1.5 : 1.0;
        double perSeat = basePrice * typeMultiplier;
        double total = perSeat * qty;

        // store per-seat price for reference
        parent.setTicketPrice(perSeat);
        priceLabel.setText(String.format("Per seat: $%.2f  |  Total: $%.2f", perSeat, total));
    }

    private void updateSeatList(CineVerse parent) {
        String type = (String) seatTypeBox.getSelectedItem();
        if ("VIP".equals(type)) {
            seatBox.setModel(new DefaultComboBoxModel<>(new String[]{"A1", "A2"}));
        } else {
            seatBox.setModel(new DefaultComboBoxModel<>(new String[]{"B1", "B2", "C1", "C2"}));
        }
    }

    private void updateAvailabilityLabel(CineVerse parent) {
        String seat = (String) seatBox.getSelectedItem();
        int qty = (Integer) qtySpinner.getValue();

        if (seat != null) {
            int available = parent.getAvailableSeats(seat);
            boolean canBook = parent.isSeatAvailable(seat, qty);

            availabilityLabel.setText(available + " seats available");
            availabilityLabel.setForeground(canBook ? Color.GREEN.darker() : Color.RED);
        }
    }
}