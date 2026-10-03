package com.booknook.gui;

import javax.swing.*;
import java.awt.*;
import com.booknook.utils.Theme;

public class LoginPanel extends JPanel{
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private MainFrame mainFrame;

    public LoginPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBackground(Theme.CREAM);

        JPanel loginCard = new JPanel();
        loginCard.setPreferredSize(new Dimension(420, 400));
        loginCard.setBackground(Theme.CARAMEL_CARD);
        loginCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 20, 8, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("BookNook");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Theme.DARK_ESPRESSO);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginCard.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Library Management System");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.DARK_ESPRESSO);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 1;
        loginCard.add(subtitleLabel, gbc);

        // Username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Theme.DARK_ESPRESSO);
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        loginCard.add(usernameLabel, gbc);

        // Username field
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(250, 35));
        usernameField.setBackground(Theme.WHITE_MOCHA);

        gbc.gridx = 1;
        loginCard.add(usernameField, gbc);

        // Password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Theme.DARK_ESPRESSO);
        passwordLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 3;
        loginCard.add(passwordLabel, gbc);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBackground(Theme.WHITE_MOCHA);

        gbc.gridx = 1;
        loginCard.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Theme.COFFEE_BROWN);
        loginButton.setForeground(Theme.WHITE_MOCHA);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 20, 8, 20);

        loginCard.add(loginButton, gbc);

        // Login button action
        loginButton.addActionListener(e -> login());

        add(loginCard);
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your username and password.",
                    "Login Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Temporary login for testing
        if (username.equals("admin") && password.equals("admin")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainFrame.showDashboard();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Incorrect username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
