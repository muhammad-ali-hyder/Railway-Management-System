package railway.ui;

import railway.db.DatabaseConnection;
import railway.model.*;
import railway.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserFrame extends JFrame {

    private User user;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public UserFrame(User user) {
        this.user = user;
        setTitle("Railway Management System - User: " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BG);

        contentPanel.add(createSearchTrainPanel(), "searchTrain");
        contentPanel.add(createBookTicketPanel(), "bookTicket");
        contentPanel.add(createMyBookingsPanel(), "myBookings");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        showCard("searchTrain");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(27, 94, 32), 0, getHeight(), new Color(46, 125, 50));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 680));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        header.setMaximumSize(new Dimension(220, 120));

        JLabel logo = new JLabel("🚂 RailwayMS");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel userLabel = new JLabel("👤 " + user.getUsername());
        userLabel.setFont(UITheme.FONT_SMALL);
        userLabel.setForeground(new Color(200, 255, 200));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(5));
        header.add(userLabel);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 50));
        sep.setMaximumSize(new Dimension(220, 2));

        sidebar.add(header);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createNavBtn("🔍  Search Train", "searchTrain"));
        sidebar.add(createNavBtn("🎫  Book Ticket", "bookTicket"));
        sidebar.add(createNavBtn("📋  My Bookings", "myBookings"));
        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("⬅  Logout");
        logoutBtn.setFont(UITheme.FONT_BUTTON);
        logoutBtn.setForeground(new Color(255, 200, 200));
        logoutBtn.setOpaque(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setMaximumSize(new Dimension(220, 45));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JButton createNavBtn(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY);
        btn.setForeground(new Color(200, 255, 200));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(Color.WHITE);
                btn.setContentAreaFilled(true);
                btn.setBackground(new Color(255, 255, 255, 30));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(new Color(200, 255, 200));
                btn.setContentAreaFilled(false);
            }
        });
        btn.addActionListener(e -> showCard(card));
        return btn;
    }

    private void showCard(String card) {
        cardLayout.show(contentPanel, card);
        if (card.equals("myBookings")) refreshMyBookings();
        if (card.equals("bookTicket")) refreshBookTicketTrains();
    }

    // ==================== SEARCH TRAIN ====================
    private JTextField srcSearchField, destSearchField;
    private JTable searchResultTable;
    private DefaultTableModel searchResultModel;

    private JPanel createSearchTrainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("Search Trains", UITheme.FONT_TITLE, new Color(27, 94, 32));
        panel.add(title, BorderLayout.NORTH);

        // Search form
        JPanel searchForm = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchForm.setBackground(UITheme.WHITE);
        searchForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        srcSearchField = UITheme.createTextField();
        srcSearchField.setPreferredSize(new Dimension(180, 36));
        srcSearchField.setToolTipText("From (leave blank for all)");

        destSearchField = UITheme.createTextField();
        destSearchField.setPreferredSize(new Dimension(180, 36));
        destSearchField.setToolTipText("To (leave blank for all)");

        JButton searchBtn = UITheme.createButton("🔍 Search", UITheme.SUCCESS, Color.WHITE);
        JButton showAllBtn = UITheme.createButton("Show All", UITheme.PRIMARY_LIGHT, Color.WHITE);

        searchBtn.addActionListener(e -> searchTrains());
        showAllBtn.addActionListener(e -> { srcSearchField.setText(""); destSearchField.setText(""); searchTrains(); });

        searchForm.add(UITheme.createLabel("From:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK));
        searchForm.add(srcSearchField);
        searchForm.add(UITheme.createLabel("To:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK));
        searchForm.add(destSearchField);
        searchForm.add(searchBtn);
        searchForm.add(showAllBtn);

        String[] cols = {"ID", "Train No.", "Train Name", "From", "To", "Departure", "Arrival", "Available Seats", "Fare (PKR)"};
        searchResultModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        searchResultTable = new JTable(searchResultModel);
        styleTable(searchResultTable);

        JScrollPane scroll = new JScrollPane(searchResultTable);

        panel.add(searchForm, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // Load all trains by default
        SwingUtilities.invokeLater(this::searchTrains);
        return panel;
    }

    private void searchTrains() {
        searchResultModel.setRowCount(0);
        String src = srcSearchField.getText().trim();
        String dest = destSearchField.getText().trim();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM trains WHERE source LIKE ? AND destination LIKE ? ORDER BY train_number";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + src + "%");
            ps.setString(2, "%" + dest + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                searchResultModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("train_number"), rs.getString("train_name"),
                    rs.getString("source"), rs.getString("destination"),
                    rs.getString("departure_time"), rs.getString("arrival_time"),
                    rs.getInt("available_seats"), String.format("%.2f", rs.getDouble("fare"))
                });
            }
            if (searchResultModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No trains found for the given route.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== BOOK TICKET ====================
    private JComboBox<String> trainCombo;
    private List<Train> availableTrains = new ArrayList<>();
    private JTextField passengerNameField, passengerAgeField, idProofField, travelDateField;
    private JComboBox<String> genderCombo;

    private JPanel createBookTicketPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("Book a Ticket", UITheme.FONT_TITLE, new Color(27, 94, 32));
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1;

        // Train selection
        trainCombo = new JComboBox<>();
        trainCombo.setFont(UITheme.FONT_BODY);
        trainCombo.setPreferredSize(new Dimension(300, 36));

        // Passenger details
        passengerNameField = UITheme.createTextField();
        passengerAgeField = UITheme.createTextField();
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderCombo.setFont(UITheme.FONT_BODY);
        idProofField = UITheme.createTextField();
        idProofField.setToolTipText("e.g., CNIC 12345-1234567-1");
        travelDateField = UITheme.createTextField();
        travelDateField.setText(LocalDate.now().plusDays(1).toString());

        // Section: Train Selection
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        form.add(UITheme.createLabel("── Select Train ──────────────────────", UITheme.FONT_SUBTITLE, UITheme.TEXT_LIGHT), gbc);

        gbc.gridy = 1; gbc.gridwidth = 1; gbc.gridx = 0; gbc.weightx = 0;
        form.add(UITheme.createLabel("Train:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        form.add(trainCombo, gbc);

        // Section: Passenger Details
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.weightx = 1;
        form.add(UITheme.createLabel("── Passenger Details ─────────────────", UITheme.FONT_SUBTITLE, UITheme.TEXT_LIGHT), gbc);

        gbc.gridwidth = 1;
        // Row: Name & Age
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0;
        form.add(UITheme.createLabel("Full Name:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 1; gbc.weightx = 1; form.add(passengerNameField, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        form.add(UITheme.createLabel("Age:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 3; gbc.weightx = 1; form.add(passengerAgeField, gbc);

        // Row: Gender & ID Proof
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0;
        form.add(UITheme.createLabel("Gender:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 1; gbc.weightx = 1; form.add(genderCombo, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        form.add(UITheme.createLabel("ID Proof:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 3; gbc.weightx = 1; form.add(idProofField, gbc);

        // Row: Travel Date
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0;
        form.add(UITheme.createLabel("Travel Date:", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 1; gbc.weightx = 1; form.add(travelDateField, gbc);
        gbc.gridx = 2; gbc.gridwidth = 2;
        form.add(UITheme.createLabel("(Format: YYYY-MM-DD)", UITheme.FONT_SMALL, UITheme.TEXT_LIGHT), gbc);

        // Book button
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1;
        gbc.insets = new Insets(20, 8, 8, 8);
        JButton bookBtn = UITheme.createButton("🎫  BOOK TICKET", UITheme.SUCCESS, Color.WHITE);
        bookBtn.setPreferredSize(new Dimension(250, 44));
        bookBtn.addActionListener(e -> handleBookTicket());
        form.add(bookBtn, gbc);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private void refreshBookTicketTrains() {
        trainCombo.removeAllItems();
        availableTrains.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM trains WHERE available_seats > 0 ORDER BY train_name");
            while (rs.next()) {
                Train t = new Train();
                t.setId(rs.getInt("id"));
                t.setTrainNumber(rs.getString("train_number"));
                t.setTrainName(rs.getString("train_name"));
                t.setSource(rs.getString("source"));
                t.setDestination(rs.getString("destination"));
                t.setAvailableSeats(rs.getInt("available_seats"));
                t.setFare(rs.getDouble("fare"));
                availableTrains.add(t);
                trainCombo.addItem(t.getTrainNumber() + " - " + t.getTrainName() +
                    " (" + t.getSource() + " → " + t.getDestination() + ") | PKR " + t.getFare() + " | Seats: " + t.getAvailableSeats());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void handleBookTicket() {
        int idx = trainCombo.getSelectedIndex();
        if (idx < 0 || availableTrains.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No train selected or no trains available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = passengerNameField.getText().trim();
        String ageStr = passengerAgeField.getText().trim();
        String gender = (String) genderCombo.getSelectedItem();
        String idProof = idProofField.getText().trim();
        String travelDate = travelDateField.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty() || travelDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 1 || age > 120) throw new NumberFormatException();

            Train train = availableTrains.get(idx);

            // Confirm booking
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm booking?\n\nTrain: " + train.getTrainName() +
                "\nPassenger: " + name + " (Age " + age + ")" +
                "\nTravel Date: " + travelDate +
                "\nFare: PKR " + train.getFare(),
                "Confirm Booking", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            try {
                // Insert passenger
                String pSql = "INSERT INTO passengers (user_id, name, age, gender, id_proof) VALUES (?,?,?,?,?)";
                PreparedStatement pPs = conn.prepareStatement(pSql, Statement.RETURN_GENERATED_KEYS);
                pPs.setInt(1, user.getId()); pPs.setString(2, name);
                pPs.setInt(3, age); pPs.setString(4, gender); pPs.setString(5, idProof);
                pPs.executeUpdate();
                ResultSet pKeys = pPs.getGeneratedKeys();
                int passengerId = pKeys.next() ? pKeys.getInt(1) : -1;

                // Generate seat number
                int seatNo = train.getTotalSeats() - train.getAvailableSeats() + 1;
                String seat = "S" + seatNo;

                // Insert booking
                String bSql = "INSERT INTO bookings (user_id, train_id, passenger_id, booking_date, travel_date, seat_number, status, total_fare) VALUES (?,?,?,CURDATE(),?,?,'Confirmed',?)";
                PreparedStatement bPs = conn.prepareStatement(bSql);
                bPs.setInt(1, user.getId()); bPs.setInt(2, train.getId()); bPs.setInt(3, passengerId);
                bPs.setString(4, travelDate); bPs.setString(5, seat); bPs.setDouble(6, train.getFare());
                bPs.executeUpdate();

                // Update available seats
                conn.createStatement().executeUpdate("UPDATE trains SET available_seats = available_seats - 1 WHERE id = " + train.getId());

                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "✅ Booking Confirmed!\n\nTrain: " + train.getTrainName() +
                    "\nPassenger: " + name + "\nSeat: " + seat +
                    "\nTravel Date: " + travelDate + "\nFare: PKR " + train.getFare(),
                    "Booking Successful", JOptionPane.INFORMATION_MESSAGE);

                passengerNameField.setText("");
                passengerAgeField.setText("");
                idProofField.setText("");
                refreshBookTicketTrains();

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Booking failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== MY BOOKINGS ====================
    private JTable myBookingsTable;
    private DefaultTableModel myBookingsModel;

    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("My Bookings", UITheme.FONT_TITLE, new Color(27, 94, 32));
        JButton refreshBtn = UITheme.createButton("🔄 Refresh", UITheme.SUCCESS, Color.WHITE);
        JButton cancelBtn = UITheme.createButton("❌ Cancel Booking", UITheme.DANGER, Color.WHITE);
        refreshBtn.addActionListener(e -> refreshMyBookings());
        cancelBtn.addActionListener(e -> handleCancelBooking());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);
        btnPanel.add(cancelBtn);
        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Booking ID", "Train No.", "Train Name", "From", "To", "Travel Date", "Seat", "Fare (PKR)", "Status"};
        myBookingsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        myBookingsTable = new JTable(myBookingsModel);
        styleTable(myBookingsTable);

        myBookingsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) table.getModel().getValueAt(row, 8);
                    if ("Cancelled".equals(status)) {
                        c.setBackground(new Color(255, 235, 238));
                        c.setForeground(UITheme.DANGER);
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : UITheme.ROW_ALT);
                        c.setForeground(UITheme.TEXT_DARK);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(myBookingsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshMyBookings() {
        myBookingsModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.id, t.train_number, t.train_name, t.source, t.destination, " +
                         "b.travel_date, b.seat_number, b.total_fare, b.status " +
                         "FROM bookings b JOIN trains t ON b.train_id = t.id " +
                         "WHERE b.user_id = ? ORDER BY b.id DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                myBookingsModel.addRow(new Object[]{
                    rs.getInt("b.id"), rs.getString("t.train_number"), rs.getString("t.train_name"),
                    rs.getString("t.source"), rs.getString("t.destination"),
                    rs.getString("b.travel_date"), rs.getString("b.seat_number"),
                    String.format("%.2f", rs.getDouble("b.total_fare")), rs.getString("b.status")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCancelBooking() {
        int row = myBookingsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String status = (String) myBookingsModel.getValueAt(row, 8);
        if ("Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, "This booking is already cancelled.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int bookingId = (int) myBookingsModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking #" + bookingId + "?",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                // Get train_id
                ResultSet rs = conn.createStatement().executeQuery("SELECT train_id FROM bookings WHERE id = " + bookingId);
                if (rs.next()) {
                    int trainId = rs.getInt("train_id");
                    conn.createStatement().executeUpdate("UPDATE bookings SET status='Cancelled' WHERE id = " + bookingId);
                    conn.createStatement().executeUpdate("UPDATE trains SET available_seats = available_seats + 1 WHERE id = " + trainId);
                    JOptionPane.showMessageDialog(this, "Booking cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshMyBookings();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void styleTable(JTable table) {
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(200, 230, 201));
        table.setSelectionForeground(UITheme.TEXT_DARK);
        table.getTableHeader().setFont(UITheme.FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(new Color(27, 94, 32));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) comp.setBackground(r % 2 == 0 ? Color.WHITE : UITheme.ROW_ALT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return comp;
            }
        });
    }
}
