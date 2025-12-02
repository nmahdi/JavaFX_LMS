# **LMS User Manual**

## **Introduction**
The Library Management System (LMS) utilizes the JavaFX library to create a desktop
application that allows users to manage a collection of books. The application supports adding,
removing, searching, listing, and saving books, as well as displaying book covers stored in the
images sub-folder.

## **System & Program Requirements**
LMS requires Java version 17+ as well as JavaFX SDK. The application requires the following
files/classes:
- LMS.java (Main JavaFX application class)
- Library.java (Handles file & list operations)
- Book.java (Container class for storing book information)
Furthermore, a file named “library_data.txt” can be added to the root folder of the project as the
program to load pre-defined book data. 
Additionally, JPEG images can be added to the images
sub-folder –can be created manually or is created automatically by the application– to display
book covers.

## **Running & Usage**
This application can be loaded via a JavaFX-configured IDE by running the main method inside
of LMS.java. This will initialize an application window named “LMS” after loading any predefined book data.
LMS supports the following operations:
- Add Book: Adds a book to the library. Requires non duplicate ISBN.
- Remove Book: Removes a book from the library via ISBN.
- Search Book: Searches the library for a specific book via ISBN.
- Display Book Image: Displays a book’s cover if it exists via ISBN.
- List All Books: Displays all the loaded books inside of the library.
- Save All Books: Saves all the loaded books to “library_data.txt”
Additionally, instead of pre-loading all the book covers on startup, the program stores a book’s
cover when it’s displayed instead of discarding it later. This reduces startup/image display time.

## **Application Layout**
The UI contains text fields for ISBN, Title, and Author, as well as buttons for every operation.
Every operation’s output will be displayed in an unmodifiable text area. 

<img width="1201" height="1646" alt="image" src="https://github.com/user-attachments/assets/cfc7b54f-bf61-4ca4-ba7c-11494b4e371a" />
