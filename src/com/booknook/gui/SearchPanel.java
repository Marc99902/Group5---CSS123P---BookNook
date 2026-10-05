package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.Book;
import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

/**
 * Finds books by typing any part of the title,
 * the author, or the category. Results update the
 * table instantly from the live catalog.
 */
public class SearchPanel extends JPanel {

    private final LibraryManager libraryManager;
    private final JTextField searchField = new JTextField();
    private final DefaultTableModel tableModel;
    private final JTable resultsTable;

    public SearchPanel(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;

        setLayout(new BorderLayout(14, 14));
        setBackground(Theme.CREAM);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildHeading(), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Book ID", "Title", "Author", "Category", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(28);
        resultsTable.setFillsViewportHeight(true);
        resultsTable.getTableHeader().setBackground(Theme.COFFEE_BROWN);
        resultsTable.getTableHeader().setForeground(Theme.WHITE_MOCHA);
        resultsTable.getTableHeader().setFont(Theme.bodyFont(java.awt.Font.BOLD, 12));
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.SOFT_LINE));

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(buildSearchBar(), BorderLayout.NORTH);
        center.add(scrollPane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildHeading() {
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);

        JLabel title = new JLabel("Search Books", SwingConstants.LEFT);
        title.setFont(Theme.titleFont(24));
        title.setForeground(Theme.DARK_ESPRESSO);

        JLabel subtitle = new JLabel("Search by title, author, or category");
        subtitle.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
        subtitle.setForeground(Theme.LATTE_TAN);

        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.SOUTH);
        return heading;
    }

    private JPanel buildSearchBar() {
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setBackground(Theme.CARAMEL_CARD);
        searchBar.setBorder(new EmptyBorder(12, 12, 12, 12));

        searchField.setBackground(Theme.WHITE_MOCHA);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(8, 10, 8, 10)));
        searchField.setToolTipText("Type a title, author, or category");
        searchBar.add(searchField, BorderLayout.CENTER);

        JButton searchButton = MainFrame.styledButton("Search");
        JButton showAllButton = MainFrame.styledButton("Show All");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttons.setOpaque(false);
        buttons.add(searchButton);
        buttons.add(showAllButton);
        searchBar.add(buttons, BorderLayout.EAST);

        searchButton.addActionListener(e -> performSearch());
        searchField.addActionListener(e -> performSearch());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refresh();
        });

        return searchBar;
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please type a title, author, or category first.",
                    "Empty Search",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Book> results = libraryManager.searchBook(keyword);
        showResults(results);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No books matched \"" + keyword + "\".",
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showResults(ArrayList<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getBookId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getCategory(),
                    b.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }

    public void refresh() {
        showResults(libraryManager.getBookList());
    }
}
