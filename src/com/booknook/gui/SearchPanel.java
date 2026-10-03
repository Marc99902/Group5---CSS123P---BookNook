package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.Book;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * BookNook Search Window
 * Member 4 - Sargento
 *
 * Searches books by title or author keyword.
 */
public class SearchPanel extends JPanel {
    private static final Color DARK_ESPRESSO = new Color(0x3E2C22);
    private static final Color COFFEE_BROWN = new Color(0x6F4E37);
    private static final Color LATTE_TAN = new Color(0xA0785A);
    private static final Color CARAMEL_CARD = new Color(0xE9DCC8);
    private static final Color CREAM = new Color(0xF5EFE6);

    private final LibraryManager libraryManager;
    private final JTextField searchField = new JTextField();
    private final DefaultTableModel tableModel;
    private final JTable resultsTable;

    public SearchPanel(LibraryManager libraryManager) {
        if (libraryManager == null) {
            throw new IllegalArgumentException("Library manager cannot be null.");
        }
        this.libraryManager = libraryManager;

        tableModel = new DefaultTableModel(
                new Object[]{"Book ID", "Title", "Author", "Category", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        buildUI();
        refreshResults(libraryManager.getBookList());
    }

    private void buildUI() {
        setLayout(new BorderLayout(12, 12));
        setBackground(CREAM);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Search Books");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(DARK_ESPRESSO);

        JLabel subtitle = new JLabel("Search by title or author keyword");
        subtitle.setForeground(COFFEE_BROWN);

        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.SOUTH);
        add(heading, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setBackground(CARAMEL_CARD);
        searchBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField.setPreferredSize(new Dimension(300, 34));
        searchField.setToolTipText("Enter a title or author keyword");
        searchBar.add(searchField, BorderLayout.CENTER);

        JButton searchButton = createButton("Search");
        JButton showAllButton = createButton("Show All");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(searchButton);
        buttonPanel.add(showAllButton);
        searchBar.add(buttonPanel, BorderLayout.EAST);

        searchButton.addActionListener(e -> performSearch());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshResults(libraryManager.getBookList());
        });

        searchField.addActionListener(e -> performSearch());

        add(searchBar, BorderLayout.CENTER);

        resultsTable.setRowHeight(28);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.getTableHeader().setBackground(COFFEE_BROWN);
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        resultsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(LATTE_TAN));
        add(scrollPane, BorderLayout.SOUTH);

        // Give the table most of the available vertical space.
        remove(scrollPane);
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(searchBar, BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(COFFEE_BROWN);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(LATTE_TAN));
        return button;
    }

    private void performSearch() {
        try {
            String keyword = searchField.getText().trim();

            if (keyword.isEmpty()) {
                throw new IllegalArgumentException("Please enter a title or author keyword.");
            }

            ArrayList<Book> results = libraryManager.searchBook(keyword);
            refreshResults(results);

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No books matched \"" + keyword + "\".",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Please Check", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Unable to complete the search.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshResults(ArrayList<Book> books) {
        tableModel.setRowCount(0);

        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getCategory(),
                    book.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }
}
