package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;
import com.s151044.analytics.api.Grade;
import com.s151044.analytics.api.Semester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ChromiumParser implements FileParser {
    @Override
    public List<Course> parseCourse(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .map(s -> Arrays.asList(s.split("\t")))
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
        }).toList();
    }
}
