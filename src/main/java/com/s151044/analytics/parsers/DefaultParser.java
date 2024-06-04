package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import com.s151044.analytics.api.Semester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * The strategy to parse the default input format, specified in the Course class.
 */
public class DefaultParser implements FileParser {
    @Override
    public List<Course> parseCourse(Path path) throws IOException {
        return Files.readAllLines(path).stream().map(s -> {
            String[] arr = s.split(";");
            String[] semArr = arr[6].split(" ");
            String[] semDuration = semArr[0].split("-");
            return new Course(arr[0], arr[1], arr[2], Integer.parseInt(arr[4]),
                    Boolean.parseBoolean(arr[5]), new Grade(arr[3]),
                    new Semester(Integer.parseInt(semDuration[0]), Integer.parseInt(semDuration[1]),
                            semArr[1]));
        }).toList();
    }
}
