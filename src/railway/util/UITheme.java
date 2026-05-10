package railway.util;

import java.awt.*;

public class UITheme {
    // Colors
    public static final Color PRIMARY = new Color(26, 35, 126);       // Dark blue
    public static final Color PRIMARY_LIGHT = new Color(57, 73, 171);  // Lighter blue
    public static final Color ACCENT = new Color(255, 145, 0);         // Orange
    public static final Color SUCCESS = new Color(46, 125, 50);        // Green
    public static final Color DANGER = new Color(198, 40, 40);         // Red
    public static final Color BG = new Color(245, 247, 250);           // Light grey background
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(33, 33, 33);
    public static final Color TEXT_LIGHT = new Color(117, 117, 117);
    public static final Color BORDER = new Color(200, 200, 210);
    public static final Color ROW_ALT = new Color(232, 234, 246);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 12);

    public static javax.swing.JButton createButton(String text, Color bg, Color fg) {
        javax.swing.JButton btn = new javax.swing.JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    public static javax.swing.JTextField createTextField() {
        javax.swing.JTextField tf = new javax.swing.JTextField();
        tf.setFont(FONT_BODY);
        tf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    public static javax.swing.JPasswordField createPasswordField() {
        javax.swing.JPasswordField pf = new javax.swing.JPasswordField();
        pf.setFont(FONT_BODY);
        pf.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    public static javax.swing.JLabel createLabel(String text, Font font, Color color) {
        javax.swing.JLabel lbl = new javax.swing.JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }
}
