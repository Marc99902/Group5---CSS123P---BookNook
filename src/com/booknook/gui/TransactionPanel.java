package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.Transaction;
import com.booknook.utils.Theme;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * The circulation desk of BookNook.
 * Issues loans, processes returns, and shows every
 * transaction in a live history table.
 */
public class TransactionPanel extends JPanel {

    private final MainFrame mainFrame;
    private final LibraryManager libraryManager;

    private final JTextField studentIdField = new JTextField();
    private final JTextField bookIdField = new JTextField();
    private final JLabel statusLabel = new JLabel("Enter a Student ID and a Book ID.");
    private final DefaultTableModel tableModel;
    private final JTable historyTable;

    public TransactionPanel(MainFrame mainFrame, LibraryManager libraryManager) {
        this.mainFrame = mainFrame;
        this.libraryManager = libraryManager;

        setLayout(new BorderLayout(14, 14));
        setBackground(Theme.CREAM);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildHeading(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.WEST);

        String[] columns = {"Txn ID", "Book", "Borrower", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);
        historyTable.setRowHeight(26);
        historyTable.setFillsViewportHeight(true);
        historyTable.getTableHeader().setBackground(Theme.COFFEE_BROWN);
        historyTable.getTableHeader().setForeground(Theme.WHITE_MOCHA);
        historyTable.getTableHeader().setFont(Theme.bodyFont(java.awt.Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.SOFT_LINE));
        add(scrollPane, BorderLayout.CENTER);

        statusLabel.setForeground(Theme.DARK_ESPRESSO);
        statusLabel.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 12));
        statusLabel.setBorder(new EmptyBorder(4, 4, 0, 4));
        add(statusLabel, BorderLayout.SOUTH);

        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ENTER"), "issueLoan");
        getActionMap().put("issueLoan", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                issueLoan();
            }
        });

        refresh();
    }

    private JPanel buildHeading() {
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("Borrow and Return", SwingConstants.LEFT);
        title.setFont(Theme.titleFont(24));
        title.setForeground(Theme.DARK_ESPRESSO);

        JLabel subtitle = new JLabel("Issue a loan or clear an active one");
        subtitle.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
        subtitle.setForeground(Theme.LATTE_TAN);

        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.SOUTH);
        return heading;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.CARAMEL_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(form, gbc, 0, "Student ID", studentIdField);
        addFormRow(form, gbc, 1, "Book ID", bookIdField);

        JButton issueButton = MainFrame.styledButton("Issue Loan");
        JButton returnButton = MainFrame.styledButton("Return Book");
        JButton clearButton = MainFrame.styledButton("Clear");

        issueButton.addActionListener(e -> issueLoan());
        returnButton.addActionListener(e -> returnBook());
        clearButton.addActionListener(e -> clearFields());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        buttons.setOpaque(false);
        buttons.add(issueButton);
        buttons.add(returnButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        return form;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JTextField field) {
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.gridx = 0;

        JLabel label = new JLabel(labelText);
        label.setFont(Theme.bodyFont(java.awt.Font.BOLD, 13));
        label.setForeground(Theme.DARK_ESPRESSO);
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setColumns(14);
        field.setBackground(Theme.WHITE_MOCHA);
        field.setForeground(Theme.DARK_ESPRESSO);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(6, 8, 6, 8)));
        panel.add(field, gbc);
    }

    private void issueLoan() {
        String studentId = studentIdField.getText().trim();
        String bookId = bookIdField.getText().trim();

        if (studentId.isEmpty() || bookId.isEmpty()) {
            showWarning("Please enter both a Student ID and a Book ID.");
            return;
        }

        String result = libraryManager.borrowBook(bookId, studentId);
        statusLabel.setText(result);

        if (result.startsWith("Success:")) {
            showInfo(result);
            clearFields();
            refresh();
            mainFrame.notifyDataChanged();
        } else {
            showWarning(result);
        }
    }

    private void returnBook() {
        String bookId = bookIdField.getText().trim();

        if (bookId.isEmpty()) {
            showWarning("Please enter the Book ID of the book being returned.");
            return;
        }

        String result = libraryManager.returnBook(bookId);
        statusLabel.setText(result);

        if (result.startsWith("Success:")) {
            showInfo(result);
            clearFields();
            refresh();
            mainFrame.notifyDataChanged();
        } else {
            showWarning(result);
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        bookIdField.setText("");
        statusLabel.setText("Enter a Student ID and a Book ID.");
        studentIdField.requestFocusInWindow();
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Transaction t : libraryManager.getTransactionHistory()) {
            tableModel.addRow(new Object[]{
                    t.getTransactionId(),
                    t.getBook().getTitle(),
                    t.getUser().getFullName(),
                    t.getBorrowDate(),
                    t.getStatus()
            });
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "BookNook",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Please Check",
                JOptionPane.WARNING_MESSAGE);
    }
}
