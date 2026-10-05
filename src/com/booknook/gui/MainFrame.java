package com.booknook.gui;

import com.booknook.core.LibraryManager;
import com.booknook.utils.Theme;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

/**
 * The main window of BookNook.
 * It owns the shared LibraryManager, shows the login
 * screen first, then reveals the sidebar shell where
 * every feature panel lives inside one CardLayout.
 */
public class MainFrame extends JFrame {

    private final LibraryManager libraryManager;
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    private CardLayout contentLayout;
    private JPanel contentPanel;
    private final ArrayList<JButton> navButtons = new ArrayList<>();

    private DashboardPanel dashboardPanel;
    private ManageBooksPanel manageBooksPanel;
    private MembersPanel membersPanel;
    private TransactionPanel transactionPanel;
    private SearchPanel searchPanel;

    public MainFrame() {
        libraryManager = new LibraryManager();

        setTitle("BookNook Library Management System");
        setSize(1024, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(960, 600));

        rootPanel.add(new LoginPanel(this), "LOGIN");
        rootPanel.add(buildAppShell(), "APP");

        setContentPane(rootPanel);
        showLogin();
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    public void showLogin() {
        rootLayout.show(rootPanel, "LOGIN");
    }

    public void showApp() {
        rootLayout.show(rootPanel, "APP");
        showScreen("DASHBOARD");
    }

    /**
     * Switches the visible feature panel and refreshes
     * its data so the screen is never stale.
     */
    public void showScreen(String name) {
        contentLayout.show(contentPanel, name);
        highlightNav(name);

        switch (name) {
            case "DASHBOARD" -> dashboardPanel.refresh();
            case "BOOKS" -> manageBooksPanel.refresh();
            case "MEMBERS" -> membersPanel.refresh();
            case "TRANSACTIONS" -> transactionPanel.refresh();
            case "SEARCH" -> searchPanel.refresh();
            default -> { }
        }
    }

    /**
     * Called by panels after they change data so the
     * dashboard numbers update the next time it shows.
     */
    public void notifyDataChanged() {
        dashboardPanel.refresh();
    }

    private JPanel buildAppShell() {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(Theme.CREAM);

        shell.add(buildSidebar(), BorderLayout.WEST);

        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(Theme.CREAM);

        dashboardPanel = new DashboardPanel(this, libraryManager);
        manageBooksPanel = new ManageBooksPanel(this, libraryManager);
        membersPanel = new MembersPanel(this, libraryManager);
        transactionPanel = new TransactionPanel(this, libraryManager);
        searchPanel = new SearchPanel(libraryManager);

        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(manageBooksPanel, "BOOKS");
        contentPanel.add(membersPanel, "MEMBERS");
        contentPanel.add(transactionPanel, "TRANSACTIONS");
        contentPanel.add(searchPanel, "SEARCH");

        shell.add(contentPanel, BorderLayout.CENTER);
        return shell;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(190, 600));
        sidebar.setBackground(Theme.DARK_ESPRESSO);
        sidebar.setBorder(new EmptyBorder(24, 14, 16, 14));

        JLabel brand = new JLabel("BookNook");
        brand.setFont(Theme.titleFont(24));
        brand.setForeground(Theme.CREAM);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("LIBRARY SYSTEM");
        subtitle.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 10));
        subtitle.setForeground(Theme.LATTE_TAN);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(28));

        addNavButton(sidebar, "Dashboard", "DASHBOARD");
        addNavButton(sidebar, "Manage Books", "BOOKS");
        addNavButton(sidebar, "Members", "MEMBERS");
        addNavButton(sidebar, "Borrow and Return", "TRANSACTIONS");
        addNavButton(sidebar, "Search", "SEARCH");

        sidebar.add(Box.createVerticalGlue());

        JButton logoutButton = createNavButton("Log Out");
        logoutButton.addActionListener(e -> logout());
        sidebar.add(logoutButton);

        sidebar.add(Box.createVerticalStrut(12));

        JLabel credit = new JLabel("<html>CSS123P Final Project<br>Group 5</html>");
        credit.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 10));
        credit.setForeground(Theme.LATTE_TAN);
        credit.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(credit);

        return sidebar;
    }

    private void addNavButton(JPanel sidebar, String text, String card) {
        JButton button = createNavButton(text);
        button.putClientProperty("card", card);
        button.addActionListener(e -> showScreen(card));
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(6));
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(Theme.bodyFont(java.awt.Font.PLAIN, 13));
        button.setForeground(Theme.CREAM);
        button.setBackground(Theme.DARK_ESPRESSO);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(11, 14, 11, 10));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        navButtons.add(button);
        return button;
    }

    private void highlightNav(String activeCard) {
        for (JButton button : navButtons) {
            Object card = button.getClientProperty("card");
            boolean active = activeCard.equals(card);
            button.setBackground(active ? Theme.COFFEE_BROWN : Theme.DARK_ESPRESSO);
        }
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Log out and return to the login screen?",
                "Log Out",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            showLogin();
        }
    }

    /**
     * Shared factory so every primary button in the
     * system looks the same.
     */
    public static JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Theme.COFFEE_BROWN);
        button.setForeground(Theme.WHITE_MOCHA);
        button.setFont(Theme.bodyFont(java.awt.Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.LATTE_TAN),
                new EmptyBorder(8, 16, 8, 16)));
        return button;
    }
}
