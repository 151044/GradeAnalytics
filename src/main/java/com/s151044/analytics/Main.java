package com.s151044.analytics;

import com.formdev.flatlaf.FlatDarkLaf;
import com.s151044.analytics.ui.AnalyticsFrame;

import javax.swing.*;

public class Main {
    private static AnalyticsFrame frame;
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        frame = new AnalyticsFrame();
    }

    public static AnalyticsFrame getFrame() {
        return frame;
    }
}
