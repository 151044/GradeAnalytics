package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.ui.ListTableModel.ListTableColumn;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class DepartmentPanel extends CourseDisplay {
    private JTable table = new JTable();
    private JScrollPane pane;
    public DepartmentPanel() {
        setLayout(new BorderLayout());
    }
    public void set(List<Course> courses) {
        if (pane != null) {
            remove(pane);
        }
        ListTableColumn<String> depts = new ListTableColumn<>("Department", String.class);
        ListTableColumn<Double> cga = new ListTableColumn<>("CGA", Double.class);
        ListTableColumn<Integer> credits = new ListTableColumn<>("Credits", Integer.class);
        ListTableModel model = new ListTableModel(List.of(depts, cga, credits));
        table = new JTable(model);
        Map<String, List<Course>> groupedCourses = courses.stream().collect(Collectors.groupingBy(Course::dept));
        groupedCourses.forEach((dept, deptCourses) -> model.addRow(dept, deptCourses.stream()
                .filter(c -> c.grade().isGraded())
                .flatMapToDouble(c -> DoubleStream.generate(() -> c.grade().toGpa()).limit(c.credits()))
                .average().orElse(0.0),
                deptCourses.stream()
                        .filter(c -> c.grade().isGraded())
                        .mapToInt(Course::credits).sum()));
        table.setRowSorter(new TableRowSorter<>(model));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        pane = scrollPane;
        add(scrollPane, BorderLayout.CENTER);
    }
}
