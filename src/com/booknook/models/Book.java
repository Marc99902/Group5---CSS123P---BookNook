package com.booknook.models;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable; // Tracks if it is borrowed or not

    // Constructor initializes a new book
    public Book(String bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true; // By default, newly added books are available
    }

    // Getters and Setters (Encapsulation)
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}