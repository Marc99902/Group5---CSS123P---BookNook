package com.booknook.models;

/**
 * Represents one book in the catalog.
 * A book always knows if it is available or borrowed.
 */
public class Book {

    private String bookId;
    private String title;
    private String author;
    private String category;
    private boolean available;

    public Book(String bookId, String title, String author, String category) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = true;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Used when saving the catalog to a CSV file.
     */
    public String toCsv() {
        return bookId + ";" + title + ";" + author + ";" + category + ";" + available;
    }

    /**
     * Rebuilds a book from one line of the CSV file.
     */
    public static Book fromCsv(String line) {
        String[] parts = line.split(";");
        Book book = new Book(parts[0], parts[1], parts[2], parts[3]);
        book.setAvailable(Boolean.parseBoolean(parts[4]));
        return book;
    }
}
