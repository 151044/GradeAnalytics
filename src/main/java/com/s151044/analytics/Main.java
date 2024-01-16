package com.s151044.analytics;

import com.formdev.flatlaf.FlatDarkLaf;
import com.s151044.analytics.parsers.ChromiumParser;
import com.s151044.analytics.parsers.FirefoxParser;
import com.s151044.analytics.ui.AnalyticsFrame;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    private static AnalyticsFrame frame;
    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
        System.out.println(new ChromiumParser().parseCourse(Path.of("data/sis-copy-chromium.txt")));
        UIManager.setLookAndFeel(new FlatDarkLaf());
        frame = new AnalyticsFrame();
    }

    public static AnalyticsFrame getFrame() {
        return frame;
    }
}
