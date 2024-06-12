package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeBreakdownPiePanel extends CourseDisplay {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.00");
    public GradeBreakdownPiePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    @Override
    public void set(List<Course> courses) {
        removeAll();
        long numCourses = courses.stream().filter(c -> c.grade().isGraded()).count();
        Map<String, List<Course>> mappedCourses = courses.stream().filter(c -> c.grade().isGraded()).collect(Collectors.groupingBy(c -> c.grade().range()));
        PieChart rangeChart = new PieChartBuilder().title("Grade Ranges").build();
        rangeChart.setXAxisTitle("Grade Range");
        rangeChart.setYAxisTitle("Percentage (%)");
        mappedCourses.forEach((range, l) -> rangeChart.addSeries(range,
                Double.parseDouble(FORMATTER.format((double) l.size() / numCourses * 100))));
        add(new XChartPanel<>(rangeChart));

        Map<Grade, List<Course>> gradedCourses = courses.stream().filter(c -> c.grade().isGraded()).collect(Collectors.groupingBy(Course::grade));
        PieChart gradeChart = new PieChartBuilder().title("Grades").build();
        gradeChart.setXAxisTitle("Grade");
        gradeChart.setYAxisTitle("Percentage (%)");
        gradedCourses.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> gradeChart.addSeries(entry.getKey().toString(),
                Double.parseDouble(FORMATTER.format((double) entry.getValue().size() / numCourses * 100))));
        add(new XChartPanel<>(gradeChart));
    }
}
