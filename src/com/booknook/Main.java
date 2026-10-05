package com.booknook;

import com.booknook.gui.MainFrame;

import javax.swing.SwingUtilities;

/**
 * Entry point of BookNook.
 * Starts the GUI safely on the Event Dispatch Thread.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
