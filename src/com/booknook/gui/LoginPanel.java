package com.booknook.gui;

import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * The first screen the user sees.
 * A simple centered card that asks for the librarian
 * credentials before opening the system.
 */
public class LoginPanel extends JPanel {

    private final MainFrame mainFrame;
    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.CREAM);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARAMEL_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("BookNook", SwingConstants.CENTER);
        title.setFont(Theme.titleFont(36));
        title.setForeground(Theme.DARK_ESPRESSO);
        gbc.gridy = 0;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Library Management System", SwingConstants.CENTER);
        subtitle.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 14));
        subtitle.setForeground(Theme.COFFEE_BROWN);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.bodyFont(java.awt.Font.BOLD, 13));
        userLabel.setForeground(Theme.DARK_ESPRESSO);
        gbc.gridy = 2;
        card.add(userLabel, gbc);

        usernameField.setColumns(16);
        usernameField.setBackground(Theme.WHITE_MOCHA);
        usernameField.setBorder(fieldBorder());
        gbc.gridx = 1;
        card.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.bodyFont(java.awt.Font.BOLD, 13));
        passLabel.setForeground(Theme.DARK_ESPRESSO);
        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(passLabel, gbc);

        passwordField.setColumns(16);
        passwordField.setBackground(Theme.WHITE_MOCHA);
        passwordField.setBorder(fieldBorder());
        gbc.gridx = 1;
        card.add(passwordField, gbc);

        JButton loginButton = MainFrame.styledButton("Log In");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 4, 8);
        card.add(loginButton, gbc);

        JLabel hint = new JLabel("Use admin for both fields during the demo", SwingConstants.CENTER);
        hint.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 11));
        hint.setForeground(Theme.LATTE_TAN);
        gbc.gridy = 5;
        gbc.insets = new Insets(4, 8, 8, 8);
        card.add(hint, gbc);

        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());

        add(card);
    }

    private javax.swing.border.Border fieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(6, 8, 6, 8));
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username and password.",
                    "Missing Fields",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (mainFrame.getLibraryManager().authenticate(username, password)) {
            passwordField.setText("");
            mainFrame.showApp();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Incorrect username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
