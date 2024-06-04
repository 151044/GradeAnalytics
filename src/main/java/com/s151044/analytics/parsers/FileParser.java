package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a strategy to parse input files to course information.
 */
public interface FileParser {
    /**
     * Converts a file into a list of courses.
     * @param path The path to read from
     * @return A list of courses, parsed from the input path
     * @throws IOException If the file cannot be read
     */
    List<Course> parseCourse(Path path) throws IOException;

    /**
     * Groups lines in a file into lists of a given size.
     * Leftover strings are also appended to the end of the list.
     * @param p The path to read from
     * @param size The size of each group
     * @return A list of list of strings of the given size
     * @throws IOException If the file cannot be read
     */
    static List<List<String>> group(Path p, int size) throws IOException {
        List<List<String>> result = new ArrayList<>();
        List<String> in = Files.readAllLines(p);
        List<String> toAdd = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            if (i != 0 && i % size == 0) {
                result.add(toAdd);
                toAdd = new ArrayList<>();
            }
            toAdd.add(in.get(i));
        }
        result.add(toAdd);
        return result;
    }

    /**
     * Detects and allocates an appropriate instance of FileParser for the given file type.
     * @param path The path to read from
     * @return A FileParser instance capable of handling the input format
     * @throws IOException If the file cannot be read
     */
    static FileParser detectType(Path path) throws IOException {
        String first = Files.readAllLines(path).get(0);
        if (first.contains(";")) {
            return new DefaultParser();
        } else if (first.length() < 10) {
            return new FirefoxParser();
        } else {
            return new ChromiumParser();
        }
    }
}
