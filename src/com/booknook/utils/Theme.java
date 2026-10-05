package com.booknook.utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Central place for the BookNook coffee house look.
 * Every panel reads its colors and fonts from here
 * so the whole system stays consistent.
 */
public class Theme {

    public static final Color DARK_ESPRESSO = Color.decode("#3E2C22");
    public static final Color COFFEE_BROWN = Color.decode("#6F4E37");
    public static final Color LATTE_TAN = Color.decode("#A0785A");
    public static final Color CARAMEL_CARD = Color.decode("#E9DCC8");
    public static final Color CREAM = Color.decode("#F5EFE6");
    public static final Color WHITE_MOCHA = Color.WHITE;
    public static final Color SAGE_GREEN = new Color(100, 130, 90);
    public static final Color SOFT_LINE = new Color(220, 208, 190);

    /**
     * Serif display font for titles. Falls back to the
     * default serif if Georgia is not installed.
     */
    public static Font titleFont(int size) {
        return new Font("Georgia", Font.BOLD, size);
    }

    /**
     * Clean sans font for labels, buttons, and tables.
     */
    public static Font bodyFont(int style, int size) {
        return new Font("Segoe UI", style, size);
    }
}
