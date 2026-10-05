package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.Book;
import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Add and remove books from the catalog.
 * The table reads straight from the shared
 * LibraryManager, so changes appear everywhere.
 */
public class ManageBooksPanel extends JPanel {

    private final MainFrame mainFrame;
    private final LibraryManager libraryManager;

    private final JTextField titleField = new JTextField();
    private final JTextField authorField = new JTextField();
    private JComboBox<String> categoryBox;
    private final DefaultTableModel tableModel;
    private final JTable bookTable;

    public ManageBooksPanel(MainFrame mainFrame, LibraryManager libraryManager) {
        this.mainFrame = mainFrame;
        this.libraryManager = libraryManager;

        setLayout(new BorderLayout(16, 16));
        setBackground(Theme.CREAM);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel header = new JLabel("Book Management", SwingConstants.LEFT);
        header.setFont(Theme.titleFont(24));
        header.setForeground(Theme.DARK_ESPRESSO);
        add(header, BorderLayout.NORTH);

        add(buildForm(), BorderLayout.WEST);

        String[] columns = {"Book ID", "Title", "Author", "Category", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        add(buildTableArea(), BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(8, 1, 6, 6));
        form.setBackground(Theme.CARAMEL_CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Theme.COFFEE_BROWN, 1),
                        " Add New Book ",
                        0, 0,
                        Theme.bodyFont(java.awt.Font.BOLD, 14),
                        Theme.DARK_ESPRESSO),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel titleLabel = new JLabel("Title");
        titleLabel.setForeground(Theme.DARK_ESPRESSO);
        JLabel authorLabel = new JLabel("Author");
        authorLabel.setForeground(Theme.DARK_ESPRESSO);
        JLabel categoryLabel = new JLabel("Category");
        categoryLabel.setForeground(Theme.DARK_ESPRESSO);

        String[] categories = {"Fiction", "Non Fiction", "Science", "History", "Technology", "General"};
        categoryBox = new JComboBox<>(categories);
        categoryBox.setBackground(Theme.WHITE_MOCHA);

        JButton addButton = MainFrame.styledButton("Add Book");
        addButton.addActionListener(e -> addBook());

        form.add(titleLabel);
        form.add(titleField);
        form.add(authorLabel);
        form.add(authorField);
        form.add(categoryLabel);
        form.add(categoryBox);
        form.add(new JLabel(""));
        form.add(addButton);

        form.setPreferredSize(new java.awt.Dimension(240, 100));
        return form;
    }

    private JPanel buildTableArea() {
        JPanel area = new JPanel(new BorderLayout(10, 10));
        area.setOpaque(false);

        bookTable.setRowHeight(26);
        bookTable.setFillsViewportHeight(true);
        bookTable.getTableHeader().setBackground(Theme.COFFEE_BROWN);
        bookTable.getTableHeader().setForeground(Theme.WHITE_MOCHA);
        bookTable.getTableHeader().setFont(Theme.bodyFont(java.awt.Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.SOFT_LINE));

        JButton deleteButton = MainFrame.styledButton("Delete Selected Book");
        deleteButton.addActionListener(e -> deleteBook());

        JPanel bottom = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(deleteButton);

        area.add(scrollPane, BorderLayout.CENTER);
        area.add(bottom, BorderLayout.SOUTH);
        return area;
    }

    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = (String) categoryBox.getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in both the title and the author.",
                    "Missing Fields",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newId = libraryManager.addBook(title, author, category);
        titleField.setText("");
        authorField.setText("");
        refresh();
        mainFrame.notifyDataChanged();

        JOptionPane.showMessageDialog(this,
                "Book " + newId + " was added to the catalog.",
                "Book Added",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBook() {
        int row = bookTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = (String) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 4);

        if (status.equals("Borrowed")) {
            JOptionPane.showMessageDialog(this,
                    "That book is currently borrowed. Return it first.",
                    "Cannot Delete",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete " + bookId + " from the catalog?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            libraryManager.deleteBook(bookId);
            refresh();
            mainFrame.notifyDataChanged();
        }
    }

    public void refresh() {
        tableModel.setRowCount(0);
        for (Book b : libraryManager.getBookList()) {
            tableModel.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }
}
