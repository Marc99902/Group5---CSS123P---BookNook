package com.booknook.models;

import java.time.LocalDate;

/**
 * Represents one borrow or return record.
 * Every loan gets its own transaction so the history is traceable.
 */
public class Transaction {

    private String transactionId;
    private Book book;
    private User user;
    private LocalDate borrowDate;
    private String status;

    public Transaction(String transactionId, Book book, User user) {
        this.transactionId = transactionId;
        this.book = book;
        this.user = user;
        this.borrowDate = LocalDate.now();
        this.status = "BORROWED";
    }

    public void markAsReturned() {
        this.status = "RETURNED";
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public String getStatus() {
        return status;
    }

    public String toCsv() {
        return transactionId + ";" + book.getBookId() + ";" + user.getStudentId()
                + ";" + borrowDate + ";" + status;
    }
}
