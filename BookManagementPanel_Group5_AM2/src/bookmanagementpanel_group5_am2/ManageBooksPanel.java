/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bookmanagementpanel_group5_am2;

/**
 *
 * @author lai
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageBooksPanel extends JPanel {

    // Theme Colors (Brown Coffee Theme)
    private final Color CREAM = new Color(0xF5, 0xEF, 0xE6);       // #F5EFE6 Background
    private final Color CARAMEL = new Color(0xE9, 0xDC, 0xC8);     // #E9DCC8 Form Panel Background
    private final Color COFFEE_BROWN = new Color(0x6F, 0x4E, 0x37);// #6F4E37 Buttons & Table Header
    private final Color DARK_ESPRESSO = new Color(0x3E, 0x2C, 0x22);// #3E2C22 Text
    private final Color WHITE = Color.WHITE;

    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JComboBox<String> cbCategory;
    private JButton btnAddBook;
    private JButton btnDeleteBook;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    private int bookIdCounter = 1;

    public ManageBooksPanel() {
        // Main Panel Setup
        setLayout(new BorderLayout(15, 15));
        setBackground(CREAM);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Top Panel: Title Header
        JLabel lblHeader = new JLabel("Book Management", SwingConstants.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(DARK_ESPRESSO);
        add(lblHeader, BorderLayout.NORTH);

        // 2. West Panel: Input Form (Title, Author, Category)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 1, 5, 5));
        formPanel.setBackground(CARAMEL);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(COFFEE_BROWN, 1), 
                        " Add New Book ", 
                        0, 0, 
                        new Font("SansSerif", Font.BOLD, 14), 
                        DARK_ESPRESSO
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel("Title:");
        lblTitle.setForeground(DARK_ESPRESSO);
        txtTitle = new JTextField();

        JLabel lblAuthor = new JLabel("Author:");
        lblAuthor.setForeground(DARK_ESPRESSO);
        txtAuthor = new JTextField();

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(DARK_ESPRESSO);
        String[] categories = {"Fiction", "Non-Fiction", "Science", "History", "Technology", "General"};
        cbCategory = new JComboBox<>(categories);
        cbCategory.setBackground(WHITE);

        btnAddBook = new JButton("Add Book");
        btnAddBook.setBackground(COFFEE_BROWN);
        btnAddBook.setForeground(WHITE);
        btnAddBook.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAddBook.setFocusPainted(false);

        formPanel.add(lblTitle);
        formPanel.add(txtTitle);
        formPanel.add(lblAuthor);
        formPanel.add(txtAuthor);
        formPanel.add(lblCategory);
        formPanel.add(cbCategory);
        formPanel.add(new JLabel("")); 
        formPanel.add(btnAddBook);

        add(formPanel, BorderLayout.WEST);

        // 3. Center Panel: Table & Delete Button
        JPanel tableContainer = new JPanel(new BorderLayout(10, 10));
        tableContainer.setBackground(CREAM);

        String[] columns = {"Book ID", "Title", "Author", "Category", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setBackground(WHITE);
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setBackground(COFFEE_BROWN);
        bookTable.getTableHeader().setForeground(WHITE);
        bookTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.getViewport().setBackground(WHITE);

        addSampleData();

        btnDeleteBook = new JButton("Delete Selected Book");
        btnDeleteBook.setBackground(COFFEE_BROWN);
        btnDeleteBook.setForeground(WHITE);
        btnDeleteBook.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnDeleteBook.setFocusPainted(false);

        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.add(btnDeleteBook, BorderLayout.SOUTH);

        add(tableContainer, BorderLayout.CENTER);

        // 4. Button Action Listeners
        
        btnAddBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        btnDeleteBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });
    }

    private void addSampleData() {
        tableModel.addRow(new Object[]{"B001", "To Kill a Mockingbird", "Harper Lee", "Fiction", "Available"});
        tableModel.addRow(new Object[]{"B002", "Java Programming", "John Doe", "Technology", "Available"});
        bookIdCounter = 3;
    }

    private void addBook() {
        String title = txtTitle.getText().trim();
        String author = txtAuthor.getText().trim();
        String category = (String) cbCategory.getSelectedItem();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please fill in all text fields!", 
                    "Input Error", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookId = String.format("B%03d", bookIdCounter++);
        
        tableModel.addRow(new Object[]{bookId, title, author, category, "Available"});

        txtTitle.setText("");
        txtAuthor.setText("");
        
        JOptionPane.showMessageDialog(this, 
                "Book successfully added!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                    "Please select a book from the table to delete.", 
                    "Selection Required", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this book?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, 
                    "Book deleted successfully.", 
                    "Deleted", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Manage Books Preview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 450);
        frame.setLocationRelativeTo(null);
        frame.add(new ManageBooksPanel());
        frame.setVisible(true);
    }
}
