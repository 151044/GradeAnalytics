package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Semester;
import com.s151044.analytics.ui.ListTableModel.ListTableColumn;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class GradeAveragePanel extends CourseDisplay {
    public GradeAveragePanel() {
        setLayout(new BorderLayout());
    }
    private JTable table;
    private JScrollPane pane;
    @Override
    public void set(List<Course> courses) {
        if (pane != null) {
            remove(pane);
        }
        ListTableColumn<Semester> semesters = new ListTableColumn<>("Semester", Semester.class);
        ListTableColumn<Double> tgas = new ListTableColumn<>("TGA", Double.class);
        ListTableColumn<Double> cgas = new ListTableColumn<>("CGA", Double.class);
        ListTableColumn<Double> mtgas = new ListTableColumn<>("MTGA", Double.class);
        ListTableColumn<Double> mcgas = new ListTableColumn<>("MCGA", Double.class);
        ListTableModel model = new ListTableModel(List.of(semesters, tgas, cgas, mtgas, mcgas));
        table = new JTable(model);
        Map<Semester, List<Course>> bySem = courses.stream().collect(Collectors.groupingBy(Course::semester));
        int numCredits = 0, numMajorCredits = 0;
        double cga = 0.0, mcga = 0.0;
        for (Map.Entry<Semester, List<Course>> entry : bySem.entrySet().stream()
                .sorted(Map.Entry.<Semester, List<Course>>comparingByKey().reversed()).toList()) {
            List<Course> semCourses = entry.getValue();
            int semCredits = semCourses.stream().filter(c -> c.grade().isGraded()).mapToInt(Course::credits).sum();
            int semMajorCredits = semCourses.stream().filter(c -> c.grade().isGraded() && c.isMajor()).mapToInt(Course::credits).sum();
            double tga = semCourses.stream().filter(c -> c.grade().isGraded())
                    .flatMapToDouble(c -> DoubleStream.generate(() -> c.grade().toGpa())
                    .limit(c.credits())).average().orElse(0.0);
            double mtga = semCourses.stream().filter(c -> c.grade().isGraded() && c.isMajor())
                    .flatMapToDouble(c -> DoubleStream.generate(() -> c.grade().toGpa())
                    .limit(c.credits())).average().orElse(0.0);
            cga = (cga * numCredits + tga * semCredits) / (numCredits + semCredits);
            mcga = (mcga * numMajorCredits + mtga * semMajorCredits) / (numMajorCredits + semMajorCredits);
            numCredits += semCredits;
            numMajorCredits += semMajorCredits;
            model.addRow(entry.getKey(), tga, cga, mtga, mcga);
        }
        table.setRowSorter(new TableRowSorter<>(model));
        pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
    }
}
