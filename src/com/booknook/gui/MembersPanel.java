package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.User;
import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Registers the students who are allowed to borrow.
 * Only a registered Student ID can take out a book.
 */
public class MembersPanel extends JPanel {

    private final MainFrame mainFrame;
    private final LibraryManager libraryManager;

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final DefaultTableModel tableModel;
    private final JTable memberTable;

    public MembersPanel(MainFrame mainFrame, LibraryManager libraryManager) {
        this.mainFrame = mainFrame;
        this.libraryManager = libraryManager;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.CREAM);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel header = new JLabel("Member Registration", SwingConstants.LEFT);
        header.setFont(Theme.titleFont(24));
        header.setForeground(Theme.DARK_ESPRESSO);
        add(header, BorderLayout.NORTH);

        add(buildForm(), BorderLayout.WEST);

        String[] columns = {"Student ID", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setRowHeight(26);
        memberTable.setFillsViewportHeight(true);
        memberTable.getTableHeader().setBackground(Theme.COFFEE_BROWN);
        memberTable.getTableHeader().setForeground(Theme.WHITE_MOCHA);
        memberTable.getTableHeader().setFont(Theme.bodyFont(java.awt.Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.SOFT_LINE));
        add(scrollPane, BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(6, 1, 6, 6));
        form.setBackground(Theme.CARAMEL_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Theme.COFFEE_BROWN, 1),
                        " Register Student ",
                        0, 0,
                        Theme.bodyFont(java.awt.Font.BOLD, 14),
                        Theme.DARK_ESPRESSO),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel idLabel = new JLabel("Student ID");
        idLabel.setForeground(Theme.DARK_ESPRESSO);
        JLabel nameLabel = new JLabel("Full Name");
        nameLabel.setForeground(Theme.DARK_ESPRESSO);

        JButton registerButton = MainFrame.styledButton("Register Member");
        registerButton.addActionListener(e -> registerMember());

        form.add(idLabel);
        form.add(idField);
        form.add(nameLabel);
        form.add(nameField);
        form.add(new JLabel(""));
        form.add(registerButton);

        form.setPreferredSize(new java.awt.Dimension(240, 100));
        return form;
    }

    private void registerMember() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both the Student ID and the full name.",
                    "Missing Fields",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String result = libraryManager.addUser(id, name);
        if (result.startsWith("Success:")) {
            idField.setText("");
            nameField.setText("");
            refresh();
            mainFrame.notifyDataChanged();
            JOptionPane.showMessageDialog(this, result,
                    "Member Registered", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, result,
                    "Duplicate ID", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (User u : libraryManager.getUserList()) {
            tableModel.addRow(new Object[]{u.getStudentId(), u.getFullName()});
        }
    }
}
