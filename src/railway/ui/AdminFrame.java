package railway.ui;

import railway.db.DatabaseConnection;
import railway.model.User;
import railway.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class AdminFrame extends JFrame {

    private User admin;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public AdminFrame(User admin) {
        this.admin = admin;
        setTitle("Railway Management System - Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UITheme.BG);

        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createAddTrainPanel(), "addTrain");
        contentPanel.add(createManageTrainsPanel(), "manageTrains");
        contentPanel.add(createViewBookingsPanel(), "viewBookings");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        showCard("dashboard");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY, 0, getHeight(), new Color(13, 71, 161));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(220, 680));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Header
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 20, 20));
        header.setMaximumSize(new Dimension(220, 120));

        JLabel logo = new JLabel("🚂 RailwayMS");
        logo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel adminLabel = new JLabel("Admin: " + admin.getUsername());
        adminLabel.setFont(UITheme.FONT_SMALL);
        adminLabel.setForeground(new Color(180, 210, 255));
        adminLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(5));
        header.add(adminLabel);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 50));
        sep.setMaximumSize(new Dimension(220, 2));

        sidebar.add(header);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(10));

        // Nav buttons
        sidebar.add(createNavButton("📊  Dashboard", "dashboard"));
        sidebar.add(createNavButton("➕  Add Train", "addTrain"));
        sidebar.add(createNavButton("🚆  Manage Trains", "manageTrains"));
        sidebar.add(createNavButton("📋  All Bookings", "viewBookings"));

        sidebar.add(Box.createVerticalGlue());

        // Logout
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

    private JButton createNavButton(String text, String card) {
        JButton btn = new JButton(text);
        btn.setFont(UITheme.FONT_BODY);
        btn.setForeground(new Color(200, 220, 255));
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
                btn.setForeground(new Color(200, 220, 255));
                btn.setContentAreaFilled(false);
            }
        });
        btn.addActionListener(e -> showCard(card));
        return btn;
    }

    private void showCard(String card) {
        cardLayout.show(contentPanel, card);
        if (card.equals("manageTrains")) refreshManageTrains();
        if (card.equals("viewBookings")) refreshAllBookings();
        if (card.equals("dashboard")) refreshDashboard();
    }

    // ==================== DASHBOARD ====================
    private JPanel dashboardPanel;
    private JLabel totalTrainsLbl, totalBookingsLbl, totalUsersLbl, confirmedLbl;

    private JPanel createDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(UITheme.BG);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("Dashboard", UITheme.FONT_TITLE, UITheme.PRIMARY);
        JLabel sub = UITheme.createLabel("Overview of Railway Management System", UITheme.FONT_BODY, UITheme.TEXT_LIGHT);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(sub);

        dashboardPanel.add(titlePanel, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        totalTrainsLbl = new JLabel("0");
        totalBookingsLbl = new JLabel("0");
        totalUsersLbl = new JLabel("0");
        confirmedLbl = new JLabel("0");

        statsPanel.add(createStatCard("🚆 Total Trains", totalTrainsLbl, UITheme.PRIMARY));
        statsPanel.add(createStatCard("📋 Total Bookings", totalBookingsLbl, UITheme.SUCCESS));
        statsPanel.add(createStatCard("👤 Registered Users", totalUsersLbl, new Color(106, 27, 154)));
        statsPanel.add(createStatCard("✅ Confirmed Bookings", confirmedLbl, UITheme.ACCENT));

        dashboardPanel.add(statsPanel, BorderLayout.CENTER);

        return dashboardPanel;
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lbl = UITheme.createLabel(label, UITheme.FONT_SUBTITLE, UITheme.TEXT_LIGHT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color);

        card.add(lbl, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    private void refreshDashboard() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs;

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM trains");
            if (rs.next()) totalTrainsLbl.setText(String.valueOf(rs.getInt(1)));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM bookings");
            if (rs.next()) totalBookingsLbl.setText(String.valueOf(rs.getInt(1)));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM users WHERE role='user'");
            if (rs.next()) totalUsersLbl.setText(String.valueOf(rs.getInt(1)));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM bookings WHERE status='Confirmed'");
            if (rs.next()) confirmedLbl.setText(String.valueOf(rs.getInt(1)));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ==================== ADD TRAIN ====================
    private JTextField tnField, tNameField, srcField, destField, deptField, arrField, seatsField, fareField;

    private JPanel createAddTrainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("Add New Train", UITheme.FONT_TITLE, UITheme.PRIMARY);
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

        tnField = UITheme.createTextField();
        tNameField = UITheme.createTextField();
        srcField = UITheme.createTextField();
        destField = UITheme.createTextField();
        deptField = UITheme.createTextField();
        deptField.setText("08:00 AM");
        arrField = UITheme.createTextField();
        arrField.setText("06:00 PM");
        seatsField = UITheme.createTextField();
        seatsField.setText("100");
        fareField = UITheme.createTextField();

        addFormRow(form, gbc, 0, "Train Number:", tnField, "Train Name:", tNameField);
        addFormRow(form, gbc, 1, "Source:", srcField, "Destination:", destField);
        addFormRow(form, gbc, 2, "Departure Time:", deptField, "Arrival Time:", arrField);
        addFormRow(form, gbc, 3, "Total Seats:", seatsField, "Fare (PKR):", fareField);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 8, 8, 8);
        JButton addBtn = UITheme.createButton("ADD TRAIN", UITheme.PRIMARY, Color.WHITE);
        addBtn.setPreferredSize(new Dimension(200, 42));
        addBtn.addActionListener(e -> handleAddTrain());
        form.add(addBtn, gbc);

        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setOpaque(false);
        centerWrapper.add(form);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String lbl1, JTextField f1, String lbl2, JTextField f2) {
        gbc.gridy = row; gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.weightx = 0;
        panel.add(UITheme.createLabel(lbl1, UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(f1, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(UITheme.createLabel(lbl2, UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        panel.add(f2, gbc);
    }

    private void handleAddTrain() {
        String tn = tnField.getText().trim();
        String tName = tNameField.getText().trim();
        String src = srcField.getText().trim();
        String dest = destField.getText().trim();
        String dept = deptField.getText().trim();
        String arr = arrField.getText().trim();
        String seatsStr = seatsField.getText().trim();
        String fareStr = fareField.getText().trim();

        if (tn.isEmpty() || tName.isEmpty() || src.isEmpty() || dest.isEmpty() || seatsStr.isEmpty() || fareStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int seats = Integer.parseInt(seatsStr);
            double fare = Double.parseDouble(fareStr);

            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO trains (train_number, train_name, source, destination, departure_time, arrival_time, total_seats, available_seats, fare) VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tn); ps.setString(2, tName); ps.setString(3, src);
            ps.setString(4, dest); ps.setString(5, dept); ps.setString(6, arr);
            ps.setInt(7, seats); ps.setInt(8, seats); ps.setDouble(9, fare);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Train added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            tnField.setText(""); tNameField.setText(""); srcField.setText("");
            destField.setText(""); fareField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats and Fare must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Train number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== MANAGE TRAINS ====================
    private JTable trainsTable;
    private DefaultTableModel trainsModel;

    private JPanel createManageTrainsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("Manage Trains", UITheme.FONT_TITLE, UITheme.PRIMARY);
        JButton refreshBtn = UITheme.createButton("🔄 Refresh", UITheme.PRIMARY_LIGHT, Color.WHITE);
        refreshBtn.addActionListener(e -> refreshManageTrains());
        JButton removeBtn = UITheme.createButton("🗑 Remove Selected", UITheme.DANGER, Color.WHITE);
        removeBtn.addActionListener(e -> handleRemoveTrain());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);
        btnPanel.add(removeBtn);
        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"ID", "Train No.", "Train Name", "Source", "Destination", "Departure", "Arrival", "Total Seats", "Available", "Fare (PKR)"};
        trainsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        trainsTable = new JTable(trainsModel);
        styleTable(trainsTable);

        JScrollPane scroll = new JScrollPane(trainsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void refreshManageTrains() {
        trainsModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM trains ORDER BY id DESC");
            while (rs.next()) {
                trainsModel.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("train_number"), rs.getString("train_name"),
                    rs.getString("source"), rs.getString("destination"),
                    rs.getString("departure_time"), rs.getString("arrival_time"),
                    rs.getInt("total_seats"), rs.getInt("available_seats"),
                    String.format("%.2f", rs.getDouble("fare"))
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading trains: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveTrain() {
        int row = trainsTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a train to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = (int) trainsModel.getValueAt(row, 0);
        String name = (String) trainsModel.getValueAt(row, 2);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remove train: " + name + "?\nThis will also remove all associated bookings.",
            "Confirm Removal", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                conn.createStatement().executeUpdate("DELETE FROM bookings WHERE train_id = " + id);
                conn.createStatement().executeUpdate("DELETE FROM trains WHERE id = " + id);
                JOptionPane.showMessageDialog(this, "Train removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshManageTrains();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==================== VIEW BOOKINGS ====================
    private JTable bookingsTable;
    private DefaultTableModel bookingsModel;

    private JPanel createViewBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = UITheme.createLabel("All Bookings", UITheme.FONT_TITLE, UITheme.PRIMARY);
        JButton refreshBtn = UITheme.createButton("🔄 Refresh", UITheme.PRIMARY_LIGHT, Color.WHITE);
        refreshBtn.addActionListener(e -> refreshAllBookings());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(refreshBtn);
        topBar.add(title, BorderLayout.WEST);
        topBar.add(btnPanel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Booking ID", "Username", "Passenger", "Train No.", "Train Name", "From", "To", "Travel Date", "Seat", "Fare", "Status"};
        bookingsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bookingsTable = new JTable(bookingsModel);
        styleTable(bookingsTable);

        // Color rows by status
        bookingsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) table.getModel().getValueAt(row, 10);
                    if ("Cancelled".equals(status)) {
                        c.setBackground(new Color(255, 235, 238));
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : UITheme.ROW_ALT);
                    }
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(bookingsTable);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void refreshAllBookings() {
        bookingsModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT b.id, u.username, p.name, t.train_number, t.train_name, t.source, t.destination, " +
                         "b.travel_date, b.seat_number, b.total_fare, b.status " +
                         "FROM bookings b " +
                         "JOIN users u ON b.user_id = u.id " +
                         "JOIN passengers p ON b.passenger_id = p.id " +
                         "JOIN trains t ON b.train_id = t.id " +
                         "ORDER BY b.id DESC";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                bookingsModel.addRow(new Object[]{
                    rs.getInt("b.id"), rs.getString("u.username"), rs.getString("p.name"),
                    rs.getString("t.train_number"), rs.getString("t.train_name"),
                    rs.getString("t.source"), rs.getString("t.destination"),
                    rs.getString("b.travel_date"), rs.getString("b.seat_number"),
                    String.format("%.2f", rs.getDouble("b.total_fare")), rs.getString("b.status")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleTable(JTable table) {
        table.setFont(UITheme.FONT_BODY);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(197, 202, 233));
        table.setSelectionForeground(UITheme.TEXT_DARK);
        table.getTableHeader().setFont(UITheme.FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
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
