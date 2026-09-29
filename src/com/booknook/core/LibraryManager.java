package com.booknook.core;

import com.booknook.models.Book;
import com.booknook.models.User;
import com.booknook.models.Transaction;
import java.util.ArrayList;

public class LibraryManager {
    // Shared Data Storage - all GUI windows will read/write to these lists[cite: 6]
    private ArrayList<Book> bookList;
    private ArrayList<User> userList;
    private ArrayList<Transaction> transactionHistory;

    public LibraryManager() {
        bookList = new ArrayList<>();
        userList = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        
        // Add dummy data for testing
        userList.add(new User("2024-0001", "Juan Dela Cruz"));
    }

    // 1. Hardcoded Librarian Login[cite: 6]
    public boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("admin"); 
    }

    // 2. Add Book Logic[cite: 6]
    public void addBook(Book newBook) {
        bookList.add(newBook);
    }

    // 3. Delete Book Logic[cite: 6]
    public boolean deleteBook(String bookId) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBookId().equals(bookId)) {
                bookList.remove(i);
                return true; // Successfully deleted
            }
        }
        return false; // Book not found
    }

    // 4. Borrow Book Logic[cite: 6]
    public String borrowBook(String bookId, String studentId) {
        Book targetBook = null;
        User targetUser = null;

        // Find the book
        for (Book b : bookList) {
            if (b.getBookId().equals(bookId)) targetBook = b;
        }
        // Find the user
        for (User u : userList) {
            if (u.getStudentId().equals(studentId)) targetUser = u;
        }

        if (targetBook == null) return "Error: Book not found.";
        if (targetUser == null) return "Error: User not found.";
        if (!targetBook.isAvailable()) return "Error: Book is already borrowed.";

        // Execute transaction
        targetBook.setAvailable(false); // Update book status
        String transId = "TXN" + (transactionHistory.size() + 1);
        transactionHistory.add(new Transaction(transId, targetBook, targetUser));
        
        return "Success: Book borrowed by " + targetUser.getFullName();
    }

    // 5. Return Book Logic[cite: 6]
    public String returnBook(String bookId) {
        for (Transaction t : transactionHistory) {
            // Find active transaction for this book
            if (t.getBook().getBookId().equals(bookId) && t.getStatus().equals("BORROWED")) {
                t.markAsReturned();
                t.getBook().setAvailable(true); // Make available again
                return "Success: Book returned.";
            }
        }
        return "Error: No active borrowing record found for this book.";
    }

    // 6. Search Book Logic[cite: 6]
    public ArrayList<Book> searchBook(String keyword) {
        ArrayList<Book> searchResults = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Book b : bookList) {
            // Checks if keyword matches title or author (ignoring case)
            if (b.getTitle().toLowerCase().contains(lowerKeyword) || 
                b.getAuthor().toLowerCase().contains(lowerKeyword)) {
                searchResults.add(b);
            }
        }
        return searchResults;
    }
    
    // Getter so GUI can populate JTables
    public ArrayList<Book> getBookList() {
        return bookList;
    }
}