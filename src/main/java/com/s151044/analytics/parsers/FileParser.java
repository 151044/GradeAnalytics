package com.s151044.analytics.parsers;

import com.s151044.analytics.api.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface FileParser {
    List<Course> parseCourse(Path path) throws IOException;
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
