public class Book {

    private final String isbn, title, author;

    public Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public String getISBN() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    // Used to display a book to UI
    @Override
    public String toString() {
        return "ISBN: " + isbn + ", Title: " + title + ", Author: " + author;
    }

    // Returns a ready to be printed string that will be saved to file, without a '\n' char.
    public String toFileString() {
        return isbn + "," + title + "," + author;
    }

}
