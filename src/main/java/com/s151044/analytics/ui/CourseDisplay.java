package com.s151044.analytics.ui;

import com.s151044.analytics.api.Course;

import javax.swing.*;
import java.util.List;

/**
 * A common interface for JPanels which support (re)loading courses.
 * All panels which display course data should extend this abstract class.
 */
public abstract class CourseDisplay extends JPanel {
    /**
     * Refreshes the panel with the list of courses provided.
     * Implementers should discard the old UI elements before adding new ones.
     * @param courses The courses to add
     */
    public abstract void set(List<Course> courses);
}
