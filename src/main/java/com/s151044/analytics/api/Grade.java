package com.s151044.analytics.api;

import java.util.List;
import java.util.Map;

public record Grade(String grade) implements Comparable<Grade> {
    public Grade {
        if (grade.isEmpty()) {
            throw new IllegalArgumentException("Empty grade initialization.");
        }
    }
    private static final Map<String, Integer> CONVERTOR = Map.of(
            "F", 0,
            "D", 1,
            "C", 2,
            "B", 3,
            "A", 4
    );
    private static final List<String> NON_GRADED = List.of("XF", "PA", "DI", "PP", "T", "P");
    public double toGpa() {
        if (NON_GRADED.contains(grade)) {
            return 0;
        }
        int letter = CONVERTOR.entrySet().stream().filter(s -> grade.startsWith(s.getKey())).findFirst().orElseThrow()
                .getValue();
        if (grade.endsWith("+")) {
            return letter + 0.3;
        } else if (grade.endsWith("-")) {
            return letter - 0.3;
        } else {
            return letter;
        }
    }

    public boolean isGraded() {
        return !NON_GRADED.contains(grade);
    }

    public String range() {
        if (!isGraded()) {
            return grade;
        }
        return grade.charAt(0) + "";
    }

    @Override
    public int compareTo(Grade otherGrade) {
        if (otherGrade.grade.equals(grade)) {
            return 0; // obvious
        }
        if (isGraded() && !otherGrade.isGraded()) {
            return -1;
        }
        if (!isGraded() && otherGrade.isGraded()) {
            return 1;
        }
        if (!isGraded() && !otherGrade.isGraded()) {
            return Integer.compare(NON_GRADED.indexOf(grade), NON_GRADED.indexOf(otherGrade.grade));
        }
        // We know that the concept of "larger" and "smaller" is correct, but we reverse the returned value
        // to make the ordering in the JTables make sense
        if (otherGrade.grade.charAt(0) == grade.charAt(0)) {
            if (grade.endsWith("+")) {
                return -1; // if we're A+ and others not equals, we must be larger
            } else if (grade.endsWith("-")) {
                return 1; // if we're A- and others not equals, we must be smaller
            } else {
                // we must be A
                if (otherGrade.grade.endsWith("+")) {
                    return 1; // A, other A+
                } else {
                    return -1; // A, other A-
                }
            }
        }
        return Character.compare(grade.charAt(0), otherGrade.grade.charAt(0));
    }

    @Override
    public String toString() {
        return grade;
    }
}
