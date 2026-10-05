package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.models.Transaction;
import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * The home screen after login.
 * Shows live counts from the shared LibraryManager and
 * a short activity feed built from the latest loans.
 */
public class DashboardPanel extends JPanel {

    private final LibraryManager libraryManager;

    private JLabel booksValue;
    private JLabel loansValue;
    private JLabel membersValue;
    private JPanel activityList;

    public DashboardPanel(MainFrame mainFrame, LibraryManager libraryManager) {
        this.libraryManager = libraryManager;

        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.CREAM);
        setBorder(new EmptyBorder(24, 24, 24, 24));

        add(buildHeading(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        refresh();
    }

    private JPanel buildHeading() {
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(Theme.titleFont(26));
        title.setForeground(Theme.DARK_ESPRESSO);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel("Welcome back. Here is a live summary of library activity.");
        description.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
        description.setForeground(Theme.LATTE_TAN);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(description);
        return heading;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 3, 14, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        booksValue = new JLabel("0");
        loansValue = new JLabel("0");
        membersValue = new JLabel("0");

        stats.add(createStatCard("Total Books", booksValue, "Titles in the catalog"));
        stats.add(createStatCard("Active Loans", loansValue, "Books currently borrowed"));
        stats.add(createStatCard("Registered Members", membersValue, "Students and staff"));

        center.add(stats);
        center.add(Box.createVerticalStrut(16));

        JPanel activityCard = new JPanel(new BorderLayout(0, 10));
        activityCard.setBackground(Theme.WHITE_MOCHA);
        activityCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.SOFT_LINE, 1),
                new EmptyBorder(16, 18, 16, 18)));

        JLabel activityTitle = new JLabel("Recent Activity");
        activityTitle.setFont(Theme.titleFont(16));
        activityTitle.setForeground(Theme.DARK_ESPRESSO);
        activityCard.add(activityTitle, BorderLayout.NORTH);

        activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);
        activityCard.add(activityList, BorderLayout.CENTER);

        center.add(activityCard);
        center.add(Box.createVerticalGlue());
        return center;
    }

    private JPanel createStatCard(String heading, JLabel valueLabel, String note) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.WHITE_MOCHA);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Theme.SOFT_LINE, 1),
                new EmptyBorder(12, 14, 12, 12)));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 12));
        headingLabel.setForeground(Theme.LATTE_TAN);
        headingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(Theme.titleFont(28));
        valueLabel.setForeground(Theme.DARK_ESPRESSO);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel noteLabel = new JLabel(note);
        noteLabel.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 11));
        noteLabel.setForeground(Theme.SAGE_GREEN);
        noteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(valueLabel);
        card.add(noteLabel);
        return card;
    }

    /**
     * Recomputes every number and rebuilds the activity
     * feed from the live transaction history.
     */
    public void refresh() {
        booksValue.setText(String.valueOf(libraryManager.getBookList().size()));
        loansValue.setText(String.valueOf(libraryManager.countActiveLoans()));
        membersValue.setText(String.valueOf(libraryManager.getUserList().size()));

        activityList.removeAll();

        var history = libraryManager.getTransactionHistory();
        if (history.isEmpty()) {
            JLabel empty = new JLabel("No transactions yet. Borrow a book to see it here.");
            empty.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
            empty.setForeground(Theme.LATTE_TAN);
            activityList.add(empty);
        } else {
            int start = Math.max(0, history.size() - 5);
            for (int i = history.size() - 1; i >= start; i--) {
                Transaction t = history.get(i);
                String line = t.getTransactionId() + "  |  "
                        + t.getBook().getTitle() + "  |  "
                        + t.getUser().getFullName() + "  |  "
                        + t.getStatus();
                JLabel row = new JLabel(line);
                row.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
                row.setForeground(Theme.DARK_ESPRESSO);
                row.setBorder(new EmptyBorder(4, 0, 4, 0));
                activityList.add(row);
            }
        }

        activityList.revalidate();
        activityList.repaint();
    }
}
