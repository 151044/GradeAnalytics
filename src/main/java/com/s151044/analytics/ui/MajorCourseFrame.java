package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Frame used to select major courses from a list of discovered courses.
 */
public class MajorCourseFrame extends JFrame {
    private final CountDownLatch latch = new CountDownLatch(2);
    private JButton submitButton = new JButton("Submit");
    private final List<Course> ret;

    /**
     * Constructs a new MajorCourseFrame object with the list of courses specified,
     * @param courses The courses to display
     */
    public MajorCourseFrame(List<Course> courses) {
        super("Major Course Selection");
        ret = new ArrayList<>(courses);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel label = new JLabel("Please select all major courses, then press Submit.");
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        add(label);

        for (Course c : courses) {
            JCheckBox box = new JCheckBox(c.dept() + " " + c.code() + ", " + c.semester());
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
            latch.countDown();
            dispose();
        });
        add(submitButton);
        pack();
        setVisible(true);
    }

    /**
     * Blocks the calling thread until the user has finished selecting major courses.
     * @throws InterruptedException If the wait is interrupted
     */
    public void await() throws InterruptedException {
        latch.countDown();
        latch.await();
    }

    /**
     * Gets the list of courses, modified with major course information.
     * This method should not be called before {@link #await()} returns.
     * @return The list of courses modified by the user.
     */
    public List<Course> getModified() {
        return ret;
    }
}
