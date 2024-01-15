package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;

import javax.swing.*;
import java.util.List;

public abstract class CourseDisplay extends JPanel {
    public abstract void set(List<Course> courses);
}
