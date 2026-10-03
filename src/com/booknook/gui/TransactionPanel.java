package com.booknook.gui;

import com.booknook.core.LibraryManager;
import java.awt.*;
import javax.swing.*;

/**
 * BookNook Transaction Terminal
 * Member 4 - Sargento
 *
 * Handles borrowing and returning books using the shared LibraryManager.
 */
public class TransactionPanel extends JPanel {
    private static final Color DARK_ESPRESSO = new Color(0x3E2C22);
    private static final Color COFFEE_BROWN = new Color(0x6F4E37);
    private static final Color LATTE_TAN = new Color(0xA0785A);
    private static final Color CARAMEL_CARD = new Color(0xE9DCC8);
    private static final Color CREAM = new Color(0xF5EFE6);
    private static final Color WHITE_MOCHA = Color.WHITE;

    private final LibraryManager libraryManager;
    private final JTextField studentIdField = new JTextField();
    private final JTextField bookIdField = new JTextField();
    private final JLabel statusLabel = new JLabel("Enter a Student ID and Book ID.");

    public TransactionPanel(LibraryManager libraryManager) {
        if (libraryManager == null) {
            throw new IllegalArgumentException("Library manager cannot be null.");
        }
        this.libraryManager = libraryManager;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(CREAM);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Transaction Terminal");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(DARK_ESPRESSO);

        JLabel subtitle = new JLabel("Borrow or return a book");
        subtitle.setForeground(COFFEE_BROWN);

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARAMEL_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LATTE_TAN),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, gbc, 0, "Student ID:", studentIdField);
        addFormRow(form, gbc, 1, "Book ID:", bookIdField);

        JButton issueButton = createButton("Issue Loan");
        JButton returnButton = createButton("Return Book");
        JButton clearButton = createButton("Clear");

        issueButton.addActionListener(e -> issueLoan());
        returnButton.addActionListener(e -> returnBook());
        clearButton.addActionListener(e -> clearFields());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttons.setOpaque(false);
        buttons.add(issueButton);
        buttons.add(returnButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        add(form, BorderLayout.CENTER);

        statusLabel.setForeground(DARK_ESPRESSO);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));
        add(statusLabel, BorderLayout.SOUTH);

        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "issueLoan");
        getActionMap().put("issueLoan", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                issueLoan();
            }
        });
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JTextField field) {
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.gridx = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(DARK_ESPRESSO);
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setPreferredSize(new Dimension(260, 34));
        field.setBackground(WHITE_MOCHA);
        field.setForeground(DARK_ESPRESSO);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(LATTE_TAN),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        panel.add(field, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COFFEE_BROWN);
        button.setForeground(WHITE_MOCHA);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(LATTE_TAN));
        return button;
    }

    private void issueLoan() {
        try {
            String studentId = studentIdField.getText().trim();
            String bookId = bookIdField.getText().trim();

            validateRequiredFields(studentId, bookId);

            String result = libraryManager.borrowBook(bookId, studentId);
            statusLabel.setText(result);

            if (result.startsWith("Success:")) {
                showInfo(result);
                clearFields();
            } else {
                showWarning(result);
            }
        } catch (IllegalArgumentException ex) {
            statusLabel.setText(ex.getMessage());
            showWarning(ex.getMessage());
        } catch (RuntimeException ex) {
            statusLabel.setText("Unable to process the loan.");
            showError("Something went wrong while processing the loan.");
        }
    }

    private void returnBook() {
        try {
            String bookId = bookIdField.getText().trim();

            if (bookId.isEmpty()) {
                throw new IllegalArgumentException("Please enter a Book ID.");
            }

            String result = libraryManager.returnBook(bookId);
            statusLabel.setText(result);

            if (result.startsWith("Success:")) {
                showInfo(result);
                clearFields();
            } else {
                showWarning(result);
            }
        } catch (IllegalArgumentException ex) {
            statusLabel.setText(ex.getMessage());
            showWarning(ex.getMessage());
        } catch (RuntimeException ex) {
            statusLabel.setText("Unable to process the return.");
            showError("Something went wrong while processing the return.");
        }
    }

    private void validateRequiredFields(String studentId, String bookId) {
        if (studentId.isEmpty() && bookId.isEmpty()) {
            throw new IllegalArgumentException("Please enter a Student ID and Book ID.");
        }
        if (studentId.isEmpty()) {
            throw new IllegalArgumentException("Please enter a Student ID.");
        }
        if (bookId.isEmpty()) {
            throw new IllegalArgumentException("Please enter a Book ID.");
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        bookIdField.setText("");
        statusLabel.setText("Enter a Student ID and Book ID.");
        studentIdField.requestFocusInWindow();
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "BookNook",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Please Check",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
