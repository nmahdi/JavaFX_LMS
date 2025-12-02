import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Library {

    private final ArrayList<Book> books = new ArrayList<>();

    // Reads & loads the contents of a file into the books array list
    public void loadBooksFromFile(File file) throws IOException {
        // Create the file if it doesn't exist.
        if(!file.exists()) {
            file.createNewFile();
        }else{
            // If the file exists, read the contents using a scanner
            try(Scanner scanner = new Scanner(file)) {

                int line = 1;
                while(scanner.hasNextLine()) {
                    // Tokenize at every comma
                    String[] tokens = scanner.nextLine().split(",");
                    // Handle incorrectly formatted lines
                    if(tokens.length != 3) {
                        System.out.println("Invalid book at line " + line + ".");
                    }else{
                        books.add(new Book(tokens[0], tokens[1], tokens[2]));
                    }
                    line++;
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Writes the contents of the books arraylist into the specified file
    public void saveBooksToFile(File file) throws IOException {
        // If the file does not exist, create it
        if(!file.exists()){
            file.createNewFile();
        }
        // Build file contents using a StringBuilder
        StringBuilder builder = new StringBuilder();
        books.forEach(book -> builder.append(book.toFileString()).append("\n"));
        // Print to file
        try(PrintWriter writer = new PrintWriter(file)) {
            writer.write(builder.toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Adds the provided book to the array list
    public void addBook(Book book) {
        this.books.add(book);
    }

    // Removes the provided book from the array list
    public void removeBook(Book book) {
        this.books.remove(book);
    }

    // Determines if a book with the provided isbn exists
    public boolean hasBook(String isbn) {
        return searchByISBN(isbn) != null;
    }

    // Finds a specific book based on the provided isbn
    // Returns null if the book does not exist
    public Book searchByISBN(String isbn) {
        for(Book current : books) {
            if(current.getISBN().equals(isbn)) {
                return current;
            }
        }
        return null;
    }

    // Finds a specific book based on the provided title
    // Returns null if the book does not exist
    public Book searchByTitle(String title) {
        for(Book current : books) {
            if(current.getTitle().equals(title)) {
                return current;
            }
        }
        return null;
    }

    // Finds all the books created by the specified author.
    // Returns an array list
    public ArrayList<Book> searchByAuthor(String author) {
        ArrayList<Book> newList = new ArrayList<>();
        for(Book current : books) {
            if(current.getAuthor().equals(author)) {
                newList.add(current);
            }
        }
        return newList;
    }

    public void listAllBooks(TextArea output) {
        // Override previous output using .setText()
        output.setText("List of all books:\n");
        // Append each book followed by a new line
        books.forEach(book -> output.appendText(book + "\n"));
    }

    public boolean isEmpty() {
        return books.isEmpty();
    }

}