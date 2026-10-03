package com.booknook.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.booknook.utils.Theme;

public class DashboardPanel extends JPanel{

    private MainFrame mainFrame;

    private final Color SIDEBAR_COLOR = Theme.DARK_ESPRESSO;
    private final Color ACTIVE_COLOR = new Color(80, 52, 47);
    private final Color CARD_COLOR = new Color(255, 252, 247);
    private final Color MUTED_COLOR = new Color(130, 117, 105);

    public DashboardPanel(MainFrame mainFrame) {

        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Theme.CREAM);

        // Create the sidebar and main dashboard content
        add(createSidebar(), BorderLayout.WEST);
        add(createDashboardContent(), BorderLayout.CENTER);
    }

    
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(150, 600));
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setBorder(new EmptyBorder(18, 12, 12, 12));


        JLabel brand = new JLabel("BookNook");
        brand.setFont(new Font("Serif", Font.BOLD, 20));
        brand.setForeground(Theme.CREAM);
        brand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("LIBRARY SYSTEM");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 7));
        subtitle.setForeground(Theme.CARAMEL_CARD);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(brand);
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(22));

        // Navigation buttons
        JButton dashboardButton = createNavButton("Dashboard", true);
        JButton manageButton = createNavButton("Manage Books", false);
        JButton transactionButton = createNavButton("Borrow & Return", false);
        JButton exitButton = createNavButton("Exit", false);

        sidebar.add(dashboardButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(manageButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(transactionButton);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(exitButton);

        // Push project information to the bottom
        sidebar.add(Box.createVerticalGlue());

        JLabel projectLabel = new JLabel(
                "<html><center>CSS123P Final Project<br>Group 5 Proposal</center></html>"
        );
        projectLabel.setFont(new Font("SansSerif", Font.PLAIN, 7));
        projectLabel.setForeground(Theme.CARAMEL_CARD);
        projectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(projectLabel);

        // Dashboard is the current screen
        dashboardButton.addActionListener(e -> {
            // Already on the dashboard
        });

        // Temporary actions until the other panels are ready
        manageButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "The Manage Books screen will be connected here."
                )
        );

        transactionButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "The Borrow & Return screen will be connected here."
                )
        );

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit BookNook?",
                    "Exit BookNook",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        return sidebar;
    }

    private JButton createNavButton(String text, boolean selected) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 11));
        button.setForeground(Theme.CREAM);
        button.setBackground(selected ? ACTIVE_COLOR : SIDEBAR_COLOR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 12, 10, 5));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        button.setPreferredSize(new Dimension(126, 34));

        return button;
    }
    
    //Dashboard Content
    private JPanel createDashboardContent() {

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(Theme.CREAM);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Page heading
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Serif", Font.BOLD, 25));
        title.setForeground(Theme.DARK_ESPRESSO);

        JLabel description = new JLabel(
                "Welcome back! Here is a summary of library activity."
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 10));
        description.setForeground(MUTED_COLOR);

        heading.add(title);
        heading.add(Box.createVerticalStrut(4));
        heading.add(description);

        content.add(heading, BorderLayout.NORTH);

        // Center section
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        // Summary cards
        JPanel stats = new JPanel(new GridLayout(1, 3, 12, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 92));
        stats.setPreferredSize(new Dimension(700, 92));

        stats.add(createStatCard(
                "Total Books", "1,245", "▣  Cataloged"
        ));

        stats.add(createStatCard(
                "Active Loans", "84", "↗  Currently Borrowed"
        ));

        stats.add(createStatCard(
                "Registered Members", "340", "♙  Students & Staff"
        ));

        center.add(stats);
        center.add(Box.createVerticalStrut(14));

        // System status panel
        JPanel statusCard = new JPanel(new BorderLayout(0, 12));
        statusCard.setBackground(CARD_COLOR);
        statusCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(239, 232, 222), 1),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel statusTitle = new JLabel(
                "System Status & Announcement"
        );
        statusTitle.setFont(new Font("Serif", Font.BOLD, 14));
        statusTitle.setForeground(Theme.DARK_ESPRESSO);

        JTextArea announcement = new JTextArea(
                "• BookNook System Proposal Ready for Wednesday Presentation\n"
                + "• All module GUI screens are being developed\n"
                + "• Database connection and persistence are subject to "
                + "the group's implementation"
        );

        announcement.setFont(new Font("SansSerif", Font.PLAIN, 10));
        announcement.setForeground(MUTED_COLOR);
        announcement.setBackground(CARD_COLOR);
        announcement.setEditable(false);
        announcement.setLineWrap(true);
        announcement.setWrapStyleWord(true);
        announcement.setFocusable(false);

        statusCard.add(statusTitle, BorderLayout.NORTH);
        statusCard.add(announcement, BorderLayout.CENTER);

        center.add(statusCard);

        content.add(center, BorderLayout.CENTER);

        return content;
    }
    //Summary Card

    private JPanel createStatCard(
            String heading, String value, String description) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(239, 232, 222), 1),
                new EmptyBorder(10, 12, 10, 8)
        ));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("SansSerif", Font.PLAIN, 9));
        headingLabel.setForeground(MUTED_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Serif", Font.BOLD, 23));
        valueLabel.setForeground(Theme.DARK_ESPRESSO);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("SansSerif", Font.PLAIN, 8));
        descriptionLabel.setForeground(new Color(100, 130, 90));

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(2));
        card.add(valueLabel);
        card.add(descriptionLabel);

        return card;
    }
}
