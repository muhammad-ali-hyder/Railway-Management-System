package railway.ui;

import railway.db.DatabaseConnection;
import railway.model.User;
import railway.util.UITheme;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JTextField signupEmailField;
    private JTabbedPane tabbedPane;

    public LoginFrame() {
        setTitle("Railway Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BG);

        // Left side - Banner
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right side - Forms
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, UITheme.PRIMARY, 0, getHeight(), UITheme.PRIMARY_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(300, 580));
        panel.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // Train icon using text
        JLabel icon = new JLabel("🚂");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Railway");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title2 = new JLabel("Management");
        title2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title2.setForeground(Color.WHITE);
        title2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title3 = new JLabel("System");
        title3.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title3.setForeground(UITheme.ACCENT);
        title3.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 80));
        sep.setMaximumSize(new Dimension(200, 2));

        JLabel sub = new JLabel("<html><center>Fast. Reliable.<br>Efficient.</center></html>");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        sub.setForeground(new Color(200, 220, 255));
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(Box.createVerticalStrut(20));
        inner.add(icon);
        inner.add(Box.createVerticalStrut(20));
        inner.add(title);
        inner.add(title2);
        inner.add(title3);
        inner.add(Box.createVerticalStrut(15));
        inner.add(sep);
        inner.add(Box.createVerticalStrut(15));
        inner.add(sub);

        panel.add(inner);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UITheme.FONT_SUBTITLE);
        tabbedPane.setBackground(UITheme.WHITE);

        tabbedPane.addTab("  Login  ", createLoginTab());
        tabbedPane.addTab("  Sign Up  ", createSignupTab());

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoginTab() {
        JPanel panel = new JPanel();
        panel.setBackground(UITheme.WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel heading = UITheme.createLabel("Welcome Back!", UITheme.FONT_TITLE, UITheme.PRIMARY);
        JLabel subheading = UITheme.createLabel("Login to your account", UITheme.FONT_BODY, UITheme.TEXT_LIGHT);

        JLabel userLbl = UITheme.createLabel("Username", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK);
        usernameField = UITheme.createTextField();
        usernameField.setPreferredSize(new Dimension(300, 38));

        JLabel passLbl = UITheme.createLabel("Password", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK);
        passwordField = UITheme.createPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 38));

        JButton loginBtn = UITheme.createButton("LOGIN", UITheme.PRIMARY, Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(300, 42));

        JLabel adminNote = UITheme.createLabel("Admin: username=admin, password=admin123", UITheme.FONT_SMALL, UITheme.TEXT_LIGHT);
        adminNote.setHorizontalAlignment(SwingConstants.CENTER);

        loginBtn.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(heading, gbc);
        gbc.gridy = 1;
        panel.add(subheading, gbc);
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 3;
        panel.add(userLbl, gbc);
        gbc.gridy = 4;
        panel.add(usernameField, gbc);
        gbc.gridy = 5;
        panel.add(passLbl, gbc);
        gbc.gridy = 6;
        panel.add(passwordField, gbc);
        gbc.gridy = 7;
        panel.add(Box.createVerticalStrut(10), gbc);
        gbc.gridy = 8;
        panel.add(loginBtn, gbc);
        gbc.gridy = 9;
        panel.add(adminNote, gbc);

        return panel;
    }

    private JPanel createSignupTab() {
        JPanel panel = new JPanel();
        panel.setBackground(UITheme.WHITE);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 0, 7, 0);

        JLabel heading = UITheme.createLabel("Create Account", UITheme.FONT_TITLE, UITheme.PRIMARY);
        JLabel subheading = UITheme.createLabel("Register as a new user", UITheme.FONT_BODY, UITheme.TEXT_LIGHT);

        JLabel userLbl = UITheme.createLabel("Username *", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK);
        signupUsernameField = UITheme.createTextField();
        signupUsernameField.setPreferredSize(new Dimension(300, 38));

        JLabel emailLbl = UITheme.createLabel("Email", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK);
        signupEmailField = UITheme.createTextField();
        signupEmailField.setPreferredSize(new Dimension(300, 38));

        JLabel passLbl = UITheme.createLabel("Password *", UITheme.FONT_SUBTITLE, UITheme.TEXT_DARK);
        signupPasswordField = UITheme.createPasswordField();
        signupPasswordField.setPreferredSize(new Dimension(300, 38));

        JButton signupBtn = UITheme.createButton("CREATE ACCOUNT", UITheme.SUCCESS, Color.WHITE);
        signupBtn.setPreferredSize(new Dimension(300, 42));
        signupBtn.addActionListener(e -> handleSignup());

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(heading, gbc);
        gbc.gridy = 1; panel.add(subheading, gbc);
        gbc.gridy = 2; panel.add(Box.createVerticalStrut(5), gbc);
        gbc.gridy = 3; panel.add(userLbl, gbc);
        gbc.gridy = 4; panel.add(signupUsernameField, gbc);
        gbc.gridy = 5; panel.add(emailLbl, gbc);
        gbc.gridy = 6; panel.add(signupEmailField, gbc);
        gbc.gridy = 7; panel.add(passLbl, gbc);
        gbc.gridy = 8; panel.add(signupPasswordField, gbc);
        gbc.gridy = 9; panel.add(Box.createVerticalStrut(5), gbc);
        gbc.gridy = 10; panel.add(signupBtn, gbc);

        return panel;
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));

                this.dispose();
                if (user.isAdmin()) {
                    new AdminFrame(user).setVisible(true);
                } else {
                    new UserFrame(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSignup() {
        String username = signupUsernameField.getText().trim();
        String email = signupEmailField.getText().trim();
        String password = new String(signupPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'user')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            signupUsernameField.setText("");
            signupEmailField.setText("");
            signupPasswordField.setText("");
            tabbedPane.setSelectedIndex(0);
        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "Username already exists. Choose another.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
