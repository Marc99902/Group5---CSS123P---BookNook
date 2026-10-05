package com.booknook.core;

import com.booknook.models.Book;
import com.booknook.models.Transaction;
import com.booknook.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The brain of BookNook.
 * All panels share one LibraryManager so every screen
 * reads and writes the same data. Records are saved to
 * simple CSV files inside the data folder, so nothing
 * is lost when the program closes.
 */
public class LibraryManager {

    private final ArrayList<Book> bookList = new ArrayList<>();
    private final ArrayList<User> userList = new ArrayList<>();
    private final ArrayList<Transaction> transactionHistory = new ArrayList<>();

    private final Path dataFolder = Paths.get("data");
    private int bookCounter = 1;
    private int transactionCounter = 1;

    public LibraryManager() {
        loadAll();
        if (bookList.isEmpty()) {
            seedSampleData();
        }
    }

    // Login

    public boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    // Book management

    public String nextBookId() {
        return String.format("B%03d", bookCounter);
    }

    public String addBook(String title, String author, String category) {
        Book book = new Book(nextBookId(), title, author, category);
        bookList.add(book);
        bookCounter++;
        saveBooks();
        return book.getBookId();
    }

    public boolean deleteBook(String bookId) {
        Book book = findBook(bookId);
        if (book == null) {
            return false;
        }
        if (!book.isAvailable()) {
            return false;
        }
        bookList.remove(book);
        saveBooks();
        return true;
    }

    public Book findBook(String bookId) {
        for (Book b : bookList) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                return b;
            }
        }
        return null;
    }

    // Member management

    public String addUser(String studentId, String fullName) {
        if (findUser(studentId) != null) {
            return "Error: That Student ID is already registered.";
        }
        userList.add(new User(studentId, fullName));
        saveUsers();
        return "Success: " + fullName + " is now registered.";
    }

    public User findUser(String studentId) {
        for (User u : userList) {
            if (u.getStudentId().equalsIgnoreCase(studentId)) {
                return u;
            }
        }
        return null;
    }

    // Borrow and return

    public String borrowBook(String bookId, String studentId) {
        Book book = findBook(bookId);
        User user = findUser(studentId);

        if (book == null) {
            return "Error: Book not found.";
        }
        if (user == null) {
            return "Error: Student ID is not registered.";
        }
        if (!book.isAvailable()) {
            return "Error: That book is already borrowed.";
        }

        book.setAvailable(false);
        String transId = String.format("TXN%03d", transactionCounter++);
        transactionHistory.add(new Transaction(transId, book, user));
        saveBooks();
        saveTransactions();
        return "Success: " + book.getTitle() + " borrowed by " + user.getFullName() + ".";
    }

    public String returnBook(String bookId) {
        for (Transaction t : transactionHistory) {
            if (t.getBook().getBookId().equalsIgnoreCase(bookId)
                    && t.getStatus().equals("BORROWED")) {
                t.markAsReturned();
                t.getBook().setAvailable(true);
                saveBooks();
                saveTransactions();
                return "Success: " + t.getBook().getTitle() + " has been returned.";
            }
        }
        return "Error: No active loan found for that Book ID.";
    }

    // Search

    public ArrayList<Book> searchBook(String keyword) {
        ArrayList<Book> results = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (Book b : bookList) {
            if (b.getTitle().toLowerCase().contains(key)
                    || b.getAuthor().toLowerCase().contains(key)
                    || b.getCategory().toLowerCase().contains(key)) {
                results.add(b);
            }
        }
        return results;
    }

    // Dashboard statistics

    public int countActiveLoans() {
        int count = 0;
        for (Transaction t : transactionHistory) {
            if (t.getStatus().equals("BORROWED")) {
                count++;
            }
        }
        return count;
    }

    // Getters for the tables

    public ArrayList<Book> getBookList() {
        return bookList;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    // File persistence with plain CSV files

    private void loadAll() {
        try {
            Files.createDirectories(dataFolder);
            loadBooks();
            loadUsers();
            loadTransactions();
        } catch (IOException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
        }
    }

    private void loadBooks() throws IOException {
        Path file = dataFolder.resolve("books.csv");
        if (!Files.exists(file)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    bookList.add(Book.fromCsv(line));
                }
            }
        }
        bookCounter = bookList.size() + 1;
    }

    private void loadUsers() throws IOException {
        Path file = dataFolder.resolve("users.csv");
        if (!Files.exists(file)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    userList.add(User.fromCsv(line));
                }
            }
        }
    }

    private void loadTransactions() throws IOException {
        Path file = dataFolder.resolve("transactions.csv");
        if (!Files.exists(file)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(";");
                Book book = findBook(parts[1]);
                User user = findUser(parts[2]);
                if (book != null && user != null) {
                    Transaction t = new Transaction(parts[0], book, user);
                    if (parts[4].equals("RETURNED")) {
                        t.markAsReturned();
                    }
                    transactionHistory.add(t);
                }
            }
        }
        transactionCounter = transactionHistory.size() + 1;
    }

    private void saveBooks() {
        Path file = dataFolder.resolve("books.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Book b : bookList) {
                writer.write(b.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save books: " + e.getMessage());
        }
    }

    private void saveUsers() {
        Path file = dataFolder.resolve("users.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (User u : userList) {
                writer.write(u.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save users: " + e.getMessage());
        }
    }

    private void saveTransactions() {
        Path file = dataFolder.resolve("transactions.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Transaction t : transactionHistory) {
                writer.write(t.toCsv());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not save transactions: " + e.getMessage());
        }
    }

    // Starter data so the demo never begins on an empty system

    private void seedSampleData() {
        addBook("To Kill a Mockingbird", "Harper Lee", "Fiction");
        addBook("Java How to Program", "Paul Deitel", "Technology");
        addBook("A Brief History of Time", "Stephen Hawking", "Science");
        addBook("Noli Me Tangere", "Jose Rizal", "History");
        addBook("The Little Prince", "Antoine de Saint Exupery", "Fiction");

        addUser("2024-0001", "Juan Dela Cruz");
        addUser("2024-0002", "Maria Clara Santos");
        addUser("2024-0003", "Jose Rizal Mercado");
    }
}
