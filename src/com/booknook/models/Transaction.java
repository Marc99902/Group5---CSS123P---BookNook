package com.booknook.models;
import java.time.LocalDate;

public class Transaction {
    private String transactionId;
    private Book book;
    private User user;
    private LocalDate borrowDate;
    private String status; // "BORROWED" or "RETURNED"

    public Transaction(String transactionId, Book book, User user) {
        this.transactionId = transactionId;
        this.book = book;
        this.user = user;
        this.borrowDate = LocalDate.now(); // Automatically grabs today's date
        this.status = "BORROWED";
    }

    // Mark the transaction as completed
    public void markAsReturned() {
        this.status = "RETURNED";
    }
    
    public Book getBook() { return book; }
    public String getStatus() { return status; }
}