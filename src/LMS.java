import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class LMS extends Application {

    /*
    * Notes For Grader:
    * Professor Jayyousi gave me permission to use the Collection.forEach() method for handling operations.
    * Please make sure the images are stored inside the root folder of the project.
    * */

    // Pre-defined book data file
    private final File libraryData = new File("library_data.txt");

    private final Library library = new Library();
    private final ImageHandler imageHandler = new ImageHandler();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Read file & create library object
        if(libraryData.exists()) {
            library.loadBooksFromFile(libraryData);
        }

        // Used to properly align UI elements, will be a child layout of the root
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(4));
        gridPane.setHgap(20);
        gridPane.setVgap(8);

        // Initialize fields
        LabeledField isbn = new LabeledField("ISBN:", gridPane, 0);
        LabeledField title = new LabeledField("Title:", gridPane, 1);
        LabeledField author = new LabeledField("Author:", gridPane, 2);

        // Initialize buttons and add them to the grid pane
        // Row 3
        ButtonContainer addBook = new ButtonContainer("Add Book");
        ButtonContainer removeBook = new ButtonContainer("Remove Book");
        gridPane.add(addBook, 0, 3);
        gridPane.add(removeBook, 1, 3);

        // Row 4
        ButtonContainer searchISBN = new ButtonContainer("Search Book by ISBN");
        ButtonContainer searchAuthor = new ButtonContainer("Search Books by Author");
        gridPane.add(searchISBN, 0, 4);
        gridPane.add(searchAuthor, 1, 4);

        // Row 5
        ButtonContainer displayImg = new ButtonContainer("Display Book Image by ISBN");
        ButtonContainer listBooks = new ButtonContainer("List All Books");
        gridPane.add(displayImg, 0, 5);
        gridPane.add(listBooks, 1, 5);

        // Row 6
        ButtonContainer save = new ButtonContainer("Save All Books");
        ButtonContainer exit = new ButtonContainer("Exit");
        gridPane.add(save, 0, 6);
        gridPane.add(exit, 1, 6);

        // Create output text area & label
        Label outputLabel = new Label("Output:");
        gridPane.add(outputLabel, 0, 7);

        // Create output area
        TextArea output = new TextArea();
        output.setEditable(false);
        // Spans two columns
        gridPane.add(output, 0, 8, 2, 1);

        // Create empty image view
        ImageView imageView = new ImageView();

        // Set button actions
        addBook.setOnAction(e -> {
            // Parse user input
            String bookISBN = isbn.getInput();
            String bookTitle = title.getInput();
            String bookAuthor = author.getInput();

            // Handle missing book data
            if(bookISBN.isEmpty() || bookTitle.isEmpty() || bookAuthor.isEmpty()) {
                output.setText("Please ensure to input a valid ISBN, Title, and Author.");
                return;
            }
            if(library.hasBook(bookISBN)) {
                output.setText("A book with this ISBN already exists.");
                return;
            }
            // Clear fields
            isbn.clear();
            title.clear();
            author.clear();

            // Add book to library
            Book book = new Book(bookISBN, bookTitle, bookAuthor);
            library.addBook(book);
            // Display output
            output.setText("Book added successfully!\n");
            output.appendText(book + "\n");
        });

        removeBook.setOnAction(e -> {
            String bookISBN = isbn.getInput();
            // Handle empty ISBN
            if(bookISBN.isEmpty()) {
                output.setText("Please enter a valid ISBN.");
                return;
            }

            // Find book value, returns null if book isn't found
            Book book = library.searchByISBN(bookISBN);
            if(book == null) {
                output.setText("Book not found. Please check the ISBN.");
                return;
            }
            // Clear isbn field
            isbn.clear();
            // Remove book
            library.removeBook(book);
            // Display output
            output.setText("Book removed successfully!");
        });

        searchISBN.setOnAction(e -> {
            String bookISBN = isbn.getInput();
            // Handle empty ISBN
            if(bookISBN.isEmpty()) {
                output.setText("Please enter a valid ISBN.");
                return;
            }

            // Find book value, returns null if book isn't found
            Book book = library.searchByISBN(bookISBN);
            if(book == null) {
                output.setText("Book not found. Please check the ISBN.");
                return;
            }
            // Display output
            output.setText("Book found: " + book);
        });

        searchAuthor.setOnAction(e -> {
            String bookAuthor = author.getInput();

            // Handle empty author
            if(bookAuthor.isEmpty()) {
                output.setText("Please enter a valid author.");
                return;
            }

            ArrayList<Book> foundBooks = library.searchByAuthor(bookAuthor);
            // Handle no books found
            if(foundBooks.isEmpty()) {
                output.setText("No books found by this author.");
                return;
            }
            // Update output
            output.setText("Books by " + bookAuthor + ":\n");
            foundBooks.forEach(book -> output.appendText(book + "\n"));
        });

        displayImg.setOnAction(e -> {
            String bookISBN = isbn.getInput();
            // Handle empty ISBN
            if(bookISBN.isEmpty()) {
                output.setText("Please enter a valid ISBN.");
                return;
            }

            // Find book value, returns null if book isn't found
            Book book = library.searchByISBN(bookISBN);
            if(book == null) {
                output.setText("Book not found. Please check the ISBN.");
                return;
            }
            Image image = imageHandler.findByISBN(bookISBN);
            if(image != null) {
                // Display image
                imageView.setImage(image);
            }else{
                // If no image is found, display message in output
                output.setText("No image exists for this book.");
            }

        });

        listBooks.setOnAction(e -> {
            // Handle empty library
            if(library.isEmpty()) {
                output.setText("There are currently no books.");
            }else{
                library.listAllBooks(output);
            }
        });

        save.setOnAction(e -> {
            try {
                library.saveBooksToFile(libraryData);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            output.setText("Books saved to " + libraryData.getName() + " successfully!");
        });

        exit.setOnAction(e -> {
            try {
                library.saveBooksToFile(libraryData);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            output.setText("Application exited successfully.");
            Platform.exit();
        });

        // Create root with proper settings
        VBox root = new VBox(gridPane, imageView);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(12));

        // Create scene & show primary stage
        primaryStage.setTitle("LMS");
        primaryStage.setScene(new Scene(root, 600, 800));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

// Will handle loading & storing images
class ImageHandler {

    private final File imageFolder = new File("images");

    // Parallel array list to maintain loaded images in memory
    private final ArrayList<Image> imageList = new ArrayList<>();
    private final ArrayList<String> isbnList = new ArrayList<>();

    public ImageHandler() {
        // If the image folder does not exist, create it
        if(!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
    }

    // Tries to find an image by the provided isbn. Returns null if an image is not found.
    public Image findByISBN(String isbn) {
        // If the img is loaded already, return it
        if(isbnList.contains(isbn)) {
            int index = isbnList.indexOf(isbn);
            return imageList.get(index);
        }
        // Load from file if img isn't already loaded
        File imgFile = new File(imageFolder, isbn + ".jpg");
        if(imgFile.exists()) {
            try {
                Image img = new Image(new FileInputStream(imgFile));
                imageList.add(img);
                isbnList.add(isbn);
                return img;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}

// Container class for text fields that need a label. Used to reduce code bloat
class LabeledField {

    private final TextField field;

    public LabeledField(String label, GridPane parent, int row) {
        this.field = new TextField();
        this.field.setMinWidth(300);
        this.field.setMaxWidth(300);
        parent.add(new Label(label), 0, row);
        parent.add(this.field, 1, row);
    }

    public void clear() {
        this.field.clear();
    }

    // Will return the string value inside the text field
    public String getInput() {
        return field.getText();
    }

}

// Container class for buttons. Used to reduce code bloat
class ButtonContainer extends Button{

    public ButtonContainer(String text) {
        super(text);
        this.setMinWidth(200);
        this.setMaxWidth(200);
    }

}