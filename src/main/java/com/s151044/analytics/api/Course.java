package com.s151044.analytics.api;

// The course file is formatted as Dept;Code;Name;Grade;Credits;Is Major(boolean true/false);Semester
// Example: COMP;1021;Introduction to Turtle;B;3;true;23-24 Fall
public record Course(String dept, String code, String title, int credits, boolean isMajor, Grade grade, Semester semester) { }
