package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import com.s151044.analytics.ui.ListTableModel.ListTableColumn;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeBreakdownPanel extends CourseDisplay {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.00");
    private JTable rangeTable = new JTable();
    private JTable gradeTable = new JTable();
    private JScrollPane rangePane;
    private JScrollPane gradePane;
    private final GridBagConstraints cons = new GridBagConstraints();
    public GradeBreakdownPanel() {
        setLayout(new GridBagLayout());
        initializeConstraint();
    }
    @Override
    public void set(List<Course> courses) {
        if (rangePane != null) {
            removeAll();
            initializeConstraint();
        }
        addAndIncrement(new JLabel("Only grades which affect CGA have been counted."), 1);
        addAndIncrement(new JLabel("Grade Range Percentages:"), 1);
        ListTableColumn<String> gradeRanges = new ListTableColumn<>("Grade Range", String.class);
        ListTableColumn<Double> rangePercents = new ListTableColumn<>("Percentage (%)", Double.class);
        ListTableModel rangeModel = new ListTableModel(List.of(gradeRanges, rangePercents));
        rangeTable = new JTable(rangeModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                int headerHeight = getTableHeader().getPreferredSize().height;
                int height = headerHeight + (10 * getRowHeight());
                int width = getPreferredSize().width;
                return new Dimension(width, height);
            }
        };
        long numCourses = courses.stream().filter(c -> c.grade().isGraded()).count();
        Map<String, List<Course>> mappedCourses = courses.stream().filter(c -> c.grade().isGraded()).collect(Collectors.groupingBy(c -> c.grade().range()));
        mappedCourses.forEach((range, l) -> rangeModel.addRow(range, Double.parseDouble(FORMATTER.format((double) l.size() / numCourses * 100))));
        rangeTable.setRowSorter(new TableRowSorter<>(rangeModel));
        rangePane = new JScrollPane(rangeTable);
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        cons.fill = GridBagConstraints.BOTH;
        addAndIncrement(rangePane, 3);

        cons.weightx = 0.0;
        cons.weighty = 0.0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        addAndIncrement(new JLabel("Grade Percentages:"), 1);
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        cons.fill = GridBagConstraints.BOTH;
        ListTableColumn<Grade> grades = new ListTableColumn<>("Grade", Grade.class);
        ListTableColumn<Double> gradePercents = new ListTableColumn<>("Percentage (%)", Double.class);
        ListTableModel gradeModel = new ListTableModel(List.of(grades, gradePercents));
        gradeTable = new JTable(gradeModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                int headerHeight = getTableHeader().getPreferredSize().height;
                int height = headerHeight + (10 * getRowHeight());
                int width = getPreferredSize().width;
                return new Dimension(width, height);
            }
        };
        Map<Grade, List<Course>> gradedCourses = courses.stream().filter(c -> c.grade().isGraded()).collect(Collectors.groupingBy(Course::grade));
        gradedCourses.forEach((range, l) -> gradeModel.addRow(range, Double.parseDouble(FORMATTER.format((double) l.size() / numCourses * 100))));
        gradeTable.setRowSorter(new TableRowSorter<>(gradeModel));
        gradePane = new JScrollPane(gradeTable);
        addAndIncrement(gradePane, 3);
    }
    public void addAndIncrement(JComponent comp, int height) {
        cons.gridheight = height;
        add(comp, cons);
        cons.gridy += height;
    }
    private void initializeConstraint() {
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.weighty = 0.0;
    }
}
