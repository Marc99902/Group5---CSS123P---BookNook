package com.booknook.gui;

import javax.swing.JFrame;
import java.awt.CardLayout;
import com.booknook.utils.Theme;

public class MainFrame extends JFrame{
    
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("BookNook Library System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        DashboardPanel dashboardPanel = new DashboardPanel(this);

        add(loginPanel, "LOGIN");
        add(dashboardPanel, "DASHBOARD");

        cardLayout.show(getContentPane(), "LOGIN");

        getContentPane().setBackground(Theme.CREAM);
    }

    public void showDashboard() {
        cardLayout.show(getContentPane(), "DASHBOARD");
    }

    public void showLogin() {
        cardLayout.show(getContentPane(), "LOGIN");
    }
}
