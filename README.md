# BookNook Library Management System

CSS123P Final Project, Group 5

A Java Swing desktop system for school librarians. It handles the book catalog, member registration, borrowing, returning, and searching. Records are saved to CSV files inside the data folder, so nothing is lost when the program closes.

## Login

Username: admin
Password: admin

## Run in VS Code

1. Install the Extension Pack for Java.
2. Open the BookNook folder.
3. Open src/com/booknook/Main.java and press the Run button above the main method.

## Run in Apache NetBeans

1. Go to File, then New Project.
2. Choose Java with Ant, then Java Project with Existing Sources.
3. Point the project folder to this BookNook folder and add the src folder as the source root.
4. Set the main class to com.booknook.Main and run the project.

## Project Structure

src/com/booknook/Main.java is the entry point.

src/com/booknook/core/LibraryManager.java holds all shared data and business logic.

src/com/booknook/models contains Book, User, and Transaction.

src/com/booknook/gui contains the Login, Dashboard, Manage Books, Members, Borrow and Return, and Search screens.

src/com/booknook/utils/Theme.java keeps every color and font consistent.

## Data Files

The first run creates a data folder with three CSV files: books.csv, users.csv, and transactions.csv. Delete that folder if you want to reset the system back to the sample data.
