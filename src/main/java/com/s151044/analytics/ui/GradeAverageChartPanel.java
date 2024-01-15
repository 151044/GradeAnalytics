package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Semester;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class GradeAverageChartPanel extends CourseDisplay {
    private static final DecimalFormat FORMATTER = new DecimalFormat("#.00");
    public GradeAverageChartPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
    @Override
    public void set(List<Course> courses) {
        removeAll();
        Map<Semester, List<Course>> bySem = courses.stream().collect(Collectors.groupingBy(Course::semester));
        int numCredits = 0, numMajorCredits = 0;
        double cga = 0.0, mcga = 0.0;
        List<Map.Entry<Semester, List<Double>>> data = new ArrayList<>();
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
            if (semCredits != 0) {
                data.add(Map.entry(entry.getKey(), List.of(tga, cga, mtga, mcga)));
            }
        }

        XYChart chart = new XYChartBuilder().title("TGA, CGA, MTGA and MCGA").xAxisTitle("Semester").yAxisTitle("GPA").build();
        List<Semester> ordered = data.stream().map(Map.Entry::getKey).sorted().toList();
        List<Integer> offsets = data.stream().map(e -> ordered.get(ordered.size() - 1).offset(e.getKey())).toList();
        List<String> legends = List.of("TGA", "CGA", "MTGA", "MCGA");
        for (int i = 0; i < legends.size(); i++) {
            int finalI = i;
            if (legends.get(i).equals("MTGA")) {
                List<Map.Entry<Integer, Double>> mtgaData = new ArrayList<>();
                for (Map.Entry<Semester, List<Double>> entry : data) {
                    double mtga = entry.getValue().get(i);
                    if (mtga != 0) {
                        mtgaData.add(Map.entry(ordered.get(ordered.size() - 1).offset(entry.getKey()), mtga));
                    }
                }
                chart.addSeries("MTGA", mtgaData.stream().map(Map.Entry::getKey).toList(),
                        mtgaData.stream().map(Map.Entry::getValue).toList());
            } else {
                chart.addSeries(legends.get(i), offsets, data.stream().map(entry -> entry.getValue().get(finalI)).toList());
            }
        }
        add(new XChartPanel<>(chart));
    }
}
