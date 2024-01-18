package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The main application frame of this course.
 */
public class AnalyticsFrame extends JFrame {
    private final List<CourseDisplay> panels = new ArrayList<>();

    /**
     * Constructs a new AnalyticsFrame instance.
     */
    public AnalyticsFrame() {
        super("Grade Analytics");
        setLayout(new BorderLayout());
        JTabbedPane pane = new JTabbedPane();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        // All the tabs of the application
        CourseDisplayPanel display = new CourseDisplayPanel();
        DepartmentPanel depts = new DepartmentPanel();
        GradeBreakdownPanel breakdown = new GradeBreakdownPanel();
        GradeBreakdownPiePanel pieBreakdown = new GradeBreakdownPiePanel();
        GradeAveragePanel averagePanel = new GradeAveragePanel();
        GradeAverageChartPanel chartAverage = new GradeAverageChartPanel();
        panels.add(display);
        panels.add(depts);
        panels.add(breakdown);
        panels.add(pieBreakdown);
        panels.add(averagePanel);
        panels.add(chartAverage);
        pane.addTab("Courses Loaded", display);
        pane.addTab("Grade Breakdown", breakdown);
        pane.addTab("Grade Breakdown (Pie)", pieBreakdown);
        pane.addTab("Department Grade Average", depts);
        pane.addTab("Grade Averages", averagePanel);
        pane.addTab("Grade Averages (Line)", chartAverage);
        add(pane, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    /**
     * Updates the UI with the list of courses loaded.
     * Invalidates, revalidates, repaints, and repacks all the course displays.
     * @param courses The courses to load
     */
    public void fireCourseLoaded(List<Course> courses) {
        panels.forEach(p -> SwingUtilities.invokeLater(() -> {
            p.set(courses);
            invalidate();
            revalidate();
            repaint();
            pack();
        }));
    }
}
