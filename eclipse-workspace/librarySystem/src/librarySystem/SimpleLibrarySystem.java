package librarySystem;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class SimpleLibrarySystem {
    static String[] genres = {"Mystery/Thriller", "Science Fiction", "Romance", "Fantasy", "Historical Fiction"};
    static String[][] books = {
        {"Gone Girl", "The Girl with the Dragon Tattoo", "The Silence of the Lambs", "The Da Vinci Code", "Sharp Objects"},
        {"Dune", "The Hitchhiker's Guide to the Galaxy", "The Handmaid's Tale", "1984", "The Three-Body Problem"},
        {"Pride and Prejudice", "The Notebook", "Me Before You", "The Time Traveler's Wife", "Eleanor Oliphant is Completely Fine"},
        {"The Lord of the Rings", "Harry Potter", "The Night Circus", "The Golden Compass", "The Name of the Wind"},
        {"The Book Thief", "All the Light We Cannot See", "The Historian", "The Other Boleyn Girl", "Wolf Hall"}
    };

    static boolean[][] borrowedStatus = new boolean[genres.length][5];
    static final int MAX_BORROW_LIMIT = 3;

    public static void main(String[] args) {
        boolean continueBorrowing = true;

        while (continueBorrowing) {
            String name = JOptionPane.showInputDialog(null, "Enter your name:");
            if (name == null || name.trim().isEmpty()) {
                showError("Name is required.");
                return;
            }

            String studentID = JOptionPane.showInputDialog(null, "Enter your student ID:");
            if (studentID == null || studentID.trim().isEmpty()) {
                showError("Student ID is required.");
                return;
            }

            StringBuilder summary = new StringBuilder();
            summary.append("Name: ").append(name).append("\n");
            summary.append("Student ID: ").append(studentID).append("\n\nBorrowed Books:\n");

            int borrowedCount = 0;

            while (borrowedCount < MAX_BORROW_LIMIT) {
                String selectedGenre = (String) JOptionPane.showInputDialog(
                        null,
                        "Choose a genre:",
                        "Genre Selection",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        genres,
                        genres[0]
                );

                if (selectedGenre == null) break;

                int genreIndex = findIndex(genres, selectedGenre);
                if (genreIndex == -1) continue;

                String[] allBooksInGenre = books[genreIndex];
                boolean[] borrowedInGenre = borrowedStatus[genreIndex];

                List<String> displayBooksList = new ArrayList<>();
                for (int i = 0; i < allBooksInGenre.length; i++) {
                    if (borrowedInGenre[i]) {
                        displayBooksList.add(allBooksInGenre[i] + " (Unavailable)");
                    } else {
                        displayBooksList.add(allBooksInGenre[i]);
                    }
                }

                String[] displayBooks = displayBooksList.toArray(new String[0]);

                String selectedDisplayBook = (String) JOptionPane.showInputDialog(
                        null,
                        "Choose a book:",
                        "Book Selection - " + selectedGenre,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        displayBooks,
                        displayBooks[0]
                );

                if (selectedDisplayBook == null) break;

                String cleanBookName = selectedDisplayBook.replace(" (Unavailable)", "");
                int bookIndex = findIndex(allBooksInGenre, cleanBookName);

                if (borrowedInGenre[bookIndex]) {
                    showError("Sorry, this book is already borrowed.");
                    continue;
                }

                String borrowDateStr = JOptionPane.showInputDialog(null, "Enter borrow date (YYYY-MM-DD):");
                if (borrowDateStr == null || borrowDateStr.trim().isEmpty()) continue;

                LocalDate borrowDate = parseDate(borrowDateStr.trim());
                if (borrowDate == null) continue;

                if (borrowDate.isAfter(LocalDate.now())) {
                    showError("Borrow date cannot be in the future.");
                    continue;
                }

                LocalDate returnDate = borrowDate.plusDays(7);
                summary.append("- ").append(cleanBookName)
                        .append(" (").append(selectedGenre).append(")\n")
                        .append("  Borrowed: ").append(borrowDate).append("\n")
                        .append("  Return by: ").append(returnDate).append("\n\n");

                borrowedStatus[genreIndex][bookIndex] = true;
                borrowedCount++;

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Would you like to borrow another book?",
                        "Continue?",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) break;
            }

            JOptionPane.showMessageDialog(null, summary.toString(), "Borrow Summary", JOptionPane.INFORMATION_MESSAGE);

            int moreUsers = JOptionPane.showConfirmDialog(
                    null,
                    "Would someone else like to borrow books?",
                    "Next User",
                    JOptionPane.YES_NO_OPTION
            );

            continueBorrowing = (moreUsers == JOptionPane.YES_OPTION);
        }

        JOptionPane.showMessageDialog(null, "Library session ended.", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
    }

    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static int findIndex(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) return i;
        }
        return -1;
    }
}
