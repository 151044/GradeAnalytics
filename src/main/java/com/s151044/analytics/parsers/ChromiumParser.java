package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * The strategy for parsing SIS data copied from Chromium-based browser.
 */
public class ChromiumParser implements FileParser {
    @Override
    public List<Course> parseCourse(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .map(s -> Arrays.asList(s.split("\t")))
                .filter(str -> !str.get(str.size() - 1).equals("In Progress"))
                .map(FileParser::strMapToCourse).toList();
    }
}
