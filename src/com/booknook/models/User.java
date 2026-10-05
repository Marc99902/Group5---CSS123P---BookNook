package com.booknook.models;

/**
 * Represents a registered student borrower.
 */
public class User {

    private String studentId;
    private String fullName;

    public User(String studentId, String fullName) {
        this.studentId = studentId;
        this.fullName = fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public String toCsv() {
        return studentId + ";" + fullName;
    }

    public static User fromCsv(String line) {
        String[] parts = line.split(";");
        return new User(parts[0], parts[1]);
    }
}
