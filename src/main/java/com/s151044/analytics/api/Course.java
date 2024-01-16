package com.s151044.analytics.api;

import java.util.Objects;

// The course file is formatted as Dept;Code;Name;Grade;Credits;Is Major(boolean true/false);Semester
// Example: COMP;1021;Introduction to Turtle;B;3;true;23-24 Fall
public record Course(String dept, String code, String title, int credits, boolean isMajor, Grade grade, Semester semester) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;
        return Objects.equals(dept, course.dept) && Objects.equals(code, course.code) && Objects.equals(semester, course.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dept, code, semester);
    }
}
