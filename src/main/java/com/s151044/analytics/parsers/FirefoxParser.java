package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import com.s151044.analytics.api.Semester;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The strategy for parsing SIS data copied from Firefox.
 */
public class FirefoxParser implements FileParser {
    /**
     * The number of lines used for each course.
     */
    private static final int COURSE_STRING_LENGTH = 6;
    @Override
    public List<Course> parseCourse(Path path) throws IOException {
        return FileParser.group(path, COURSE_STRING_LENGTH)
                .stream()
                .map(str -> str.stream().map(String::strip).toList())
                .filter(str -> !str.get(str.size() - 1).equals("In Progress"))
                .map(str -> {
                    String[] deptCode = str.get(0).split(" ");
                    String[] semester = str.get(2).split(" ");
                    return new Course(deptCode[0], deptCode[1], str.get(1),
                            (int) Double.parseDouble(str.get(4)), false,
                            new Grade(str.get(3)), new Semester(
                                    Integer.parseInt(semester[0].split("-")[0].substring(2)),
                                    Integer.parseInt(semester[0].split("-")[1]),
                            semester[1]));
                }).collect(Collectors.toList());
    }
}
