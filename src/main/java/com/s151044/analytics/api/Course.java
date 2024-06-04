package com.s151044.analytics.api;

import java.util.Objects;

// The course file is formatted as Dept;Code;Name;Grade;Credits;Is Major(boolean true/false);Semester
// Example: COMP;1021;Introduction to Turtle;B;3;true;23-24 Fall

/**
 * Class representing a course taken. Courses in progress should not be parsed.
 * @param dept The offering department of the course; should be of length 4
 * @param code The course code of the course; can contain letters and numbers
 * @param title The name of the course
 * @param credits The number of credits granted on completion of the course
 * @param isMajor Boolean representing if the course should be counted towards MCGA
 * @param grade The obtained grade of the course
 * @param semester The semester of the course
 */
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
