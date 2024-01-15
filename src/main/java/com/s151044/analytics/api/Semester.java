package com.s151044.analytics.api;

import java.util.List;

public record Semester(int first, int second, String semester) implements Comparable<Semester> {
    private static final List<String> SEMESTERS = List.of("Fall", "Winter", "Spring", "Summer");
    public Semester {
        if ((second - first) != 1) {
            throw new IllegalArgumentException("Illegal semester length: " + first + "-" + second);
        }
        if (!SEMESTERS.contains(semester)) {
            throw new IllegalArgumentException("No such semester: " + semester);
        }
    }

    @Override
    public String toString() {
        return first + "-" + second + " " + semester;
    }

    @Override
    public int compareTo(Semester otherSemester) {
        if (first < otherSemester.first) {
            return 1;
        } else if (first > otherSemester.first) {
            return -1;
        } else {
            return -Integer.compare(SEMESTERS.indexOf(semester), SEMESTERS.indexOf(otherSemester.semester));
        }
    }
    public int offset(Semester other) {
        if (equals(other)) {
            return 0;
        }
        int comparison = compareTo(other);
        Semester larger, smaller;
        if (comparison > 0) {
            larger = other;
            smaller = this;
        } else {
            larger = this;
            smaller = other;
        }
        if (larger.first - smaller.first > 0) {
            return (larger.first - smaller.first - 1) * 4
                    + (SEMESTERS.size() - 1 - SEMESTERS.indexOf(smaller.semester))
                    + SEMESTERS.indexOf(larger.semester) + 1;
        } else {
            return SEMESTERS.indexOf(larger.semester) - SEMESTERS.indexOf(smaller.semester);
        }
    }
    public Semester offset(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Negative offset in semester: " + i);
        }
        if (SEMESTERS.indexOf(semester) + i < SEMESTERS.size()) {
            return new Semester(first, second, SEMESTERS.get(SEMESTERS.indexOf(semester) + i));
        }
        int required = SEMESTERS.size() - SEMESTERS.indexOf(semester);
        return new Semester(first + 1, second + 1, "Fall").offset(i - required);
    }
}
