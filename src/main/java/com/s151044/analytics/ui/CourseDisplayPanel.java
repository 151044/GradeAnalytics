package com.s151044.analytics.ui;

import com.s151044.analytics.Main;
import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import com.s151044.analytics.api.Semester;
import com.s151044.analytics.parsers.DefaultParser;
import com.s151044.analytics.parsers.FileParser;
import com.s151044.analytics.ui.ListTableModel.ListTableColumn;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CourseDisplayPanel extends CourseDisplay {
    private JTable table;
    private JScrollPane pane;
    private final GridBagConstraints cons = new GridBagConstraints();
    public CourseDisplayPanel() {
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.BOTH;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.weighty = 0.0;

        setLayout(new GridBagLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        JLabel coursesLabel = new JLabel("Courses File:");
        JTextField pathField = new JTextField(20);
        pathField.setEnabled(false);
        JButton fileChooserButton = new JButton("Choose");
        inputPanel.add(coursesLabel);
        inputPanel.add(pathField);
        inputPanel.add(fileChooserButton);
        add(inputPanel, cons);
        cons.gridy++;

        fileChooserButton.addActionListener(ignored -> {
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> fileChooserButton.setEnabled(false));
                JFileChooser chooser = new JFileChooser(Path.of("").toFile().getAbsolutePath());
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    Path p = chooser.getSelectedFile().toPath();
                    try {
                        FileParser parser = FileParser.detectType(p);
                        List<Course> courses = parser.parseCourse(p);
                        if (!(parser instanceof DefaultParser)) {
                            MajorCourseFrame frame = new MajorCourseFrame(courses);
                            frame.await();
                            courses = frame.getModified();
                        }
                        Main.getFrame().fireCourseLoaded(courses);
                        pathField.setText(p.toFile().getName());
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                SwingUtilities.invokeLater(() -> fileChooserButton.setEnabled(true));
            }).start();
        });
    }
    @Override
    public void set(List<Course> courses) {
        if (pane != null) {
            remove(pane);
        }
        ListTableColumn<String> depts = new ListTableColumn<>("Department", String.class);
        ListTableColumn<String> codes = new ListTableColumn<>("Code", String.class);
        ListTableColumn<String> titles = new ListTableColumn<>("Title", String.class);
        ListTableColumn<Integer> credits = new ListTableColumn<>("Credits", Integer.class);
        ListTableColumn<Boolean> isMajor = new ListTableColumn<>("Is Major Course?", Boolean.class);
        ListTableColumn<Grade> grade = new ListTableColumn<>("Grade", Grade.class);
        ListTableColumn<Semester> semesters = new ListTableColumn<>("Semester", Semester.class);
        ListTableModel model = new ListTableModel(List.of(depts, codes, titles, credits, isMajor, grade, semesters));
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        courses.forEach(c -> model.addRow(c.dept(), c.code(), c.title(), c.credits(), c.isMajor(), c.grade(), c.semester()));
        table.setRowSorter(new TableRowSorter<>(model));
        JScrollPane scrollPane = new JScrollPane(table);
        pane = scrollPane;

        cons.gridheight = GridBagConstraints.REMAINDER;
        cons.weightx = 1.0;
        cons.weighty = 1.0;
        add(scrollPane, cons);
    }
}
