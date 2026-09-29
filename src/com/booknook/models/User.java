package com.booknook.models;

public class User {
    private String studentId;
    private String fullName;

    public User(String studentId, String fullName) {
        this.studentId = studentId;
        this.fullName = fullName;
    }

    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
}