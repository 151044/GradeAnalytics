package com.s151044.analytics.api;

// The course file is formatted as Dept,Code,Name,Grade,Credits,Is Major(boolean true/false),Semester
public record Course(String dept, String code, String title, int credits, boolean isMajor, Grade grade, Semester semester) { }
