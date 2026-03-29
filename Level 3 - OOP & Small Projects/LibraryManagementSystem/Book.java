package LibraryManagementSystem;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;
    private double price;
    private String publicationYear;

    public Book(String bookId, String title, String author, String isbn, String category,
                int totalCopies, double price, String publicationYear) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.price = price;
        this.publicationYear = publicationYear;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean borrowBook() {
        if(availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        if(availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public String toString() {
        return "Book ID: " + bookId + "\nTitle: " + title + "\nAuthor: " + author +
                "\nISBN: " + isbn + "\nCategory: " + category + "\nTotal Copies: " + totalCopies +
                "\nAvailable Copies: " + availableCopies + "\nPrice: " + String.format("%.2f", price) +
                "\nPublication Year: " + publicationYear;
    }
}
