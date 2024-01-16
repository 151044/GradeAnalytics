package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MajorCourseFrame extends JFrame {
    private JTable table;
    private JScrollPane pane;
    private CountDownLatch latch = new CountDownLatch(2);
    private JButton submitButton = new JButton("Submit");
    private List<Course> ret;
    public MajorCourseFrame(List<Course> courses) {
        super("Major Course Selection");
        ret = new ArrayList<>(courses);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Please select all major courses, then press Submit."));

        for (Course c : courses) {
            JCheckBox box = new JCheckBox(c.dept() + " " + c.code());
            box.addItemListener(ie -> {
                ret.remove(c);
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    ret.add(new Course(c.dept(), c.code(), c.title(), c.credits(), true, c.grade(), c.semester()));
                } else {
                    ret.add(new Course(c.dept(), c.code(), c.title(), c.credits(), false, c.grade(), c.semester()));
                }
            });
            add(box);
        }

        submitButton.addActionListener(ignored -> {
            System.out.println(latch.getCount());
            latch.countDown();
            dispose();
        });
        add(submitButton);
        pack();
        setVisible(true);
    }
    public void await() throws InterruptedException {
        latch.countDown();
        latch.await();
    }
    public List<Course> getModified() {
        return ret;
    }
}
