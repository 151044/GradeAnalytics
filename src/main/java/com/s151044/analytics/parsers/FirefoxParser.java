package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;

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
                .map(FileParser::strMapToCourse).collect(Collectors.toList());
    }
}
