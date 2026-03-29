package LibraryManagementSystem;

import java.util.*;
import java.io.*;

public class LibraryManagementSystem {
    private ArrayList<Book> books;
    private ArrayList<Member> members;
    private String booksFilename;
    private String membersFilename;

    public LibraryManagementSystem(String booksFile, String membersFile) {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.booksFilename = booksFile;
        this.membersFilename = membersFile;
        loadBooks();
        loadMembers();
    }

    public void addBook(Book book) {
        if(book == null) {
            System.out.println("Book cannot be null!");
            return;
        }

        for(Book b : books) {
            if(b.getBookId().equals(book.getBookId())) {
                System.out.println("Book with this ID already exists!");
                return;
            }
        }

        books.add(book);
        saveBooks();
        System.out.println("Book added successfully!");
    }

    public void addMember(Member member) {
        if(member == null) {
            System.out.println("Member cannot be null!");
            return;
        }

        for(Member m : members) {
            if(m.getMemberId().equals(member.getMemberId())) {
                System.out.println("Member with this ID already exists!");
                return;
            }
        }

        members.add(member);
        saveMembers();
        System.out.println("Member added successfully!");
    }

    public Book searchBookById(String bookId) {
        for(Book b : books) {
            if(b.getBookId().equals(bookId)) {
                return b;
            }
        }
        return null;
    }

    public Book searchBookByTitle(String title) {
        for(Book b : books) {
            if(b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    public ArrayList<Book> searchBookByAuthor(String author) {
        ArrayList<Book> results = new ArrayList<>();
        for(Book b : books) {
            if(b.getAuthor().equalsIgnoreCase(author)) {
                results.add(b);
            }
        }
        return results;
    }

    public ArrayList<Book> searchBookByCategory(String category) {
        ArrayList<Book> results = new ArrayList<>();
        for(Book b : books) {
            if(b.getCategory().equalsIgnoreCase(category)) {
                results.add(b);
            }
        }
        return results;
    }

    public Member searchMemberById(String memberId) {
        for(Member m : members) {
            if(m.getMemberId().equals(memberId)) {
                return m;
            }
        }
        return null;
    }

    public boolean borrowBook(String memberId, String bookId) {
        Member member = searchMemberById(memberId);
        Book book = searchBookById(bookId);

        if(member == null) {
            System.out.println("Member not found!");
            return false;
        }

        if(book == null) {
            System.out.println("Book not found!");
            return false;
        }

        if(!member.canBorrow()) {
            System.out.println("Member has reached maximum borrowing limit!");
            return false;
        }

        if(!book.borrowBook()) {
            System.out.println("No copies available!");
            return false;
        }

        member.borrowBook();
        saveBooks();
        saveMembers();
        System.out.println("Book borrowed successfully!");
        return true;
    }

    public boolean returnBook(String memberId, String bookId) {
        Member member = searchMemberById(memberId);
        Book book = searchBookById(bookId);

        if(member == null) {
            System.out.println("Member not found!");
            return false;
        }

        if(book == null) {
            System.out.println("Book not found!");
            return false;
        }

        book.returnBook();
        member.returnBook();
        saveBooks();
        saveMembers();
        System.out.println("Book returned successfully!");
        return true;
    }

    public void displayAllBooks() {
        if(books.isEmpty()) {
            System.out.println("No books found!");
            return;
        }

        System.out.println("\n========== All Books ==========");
        System.out.println(String.format("%-8s | %-25s | %-20s | %-10s | %-12s",
                "Book ID", "Title", "Author", "Available", "Category"));
        System.out.println("-------------------------------------------------------------------");

        for(Book b : books) {
            System.out.println(String.format("%-8s | %-25s | %-20s | %-10d | %-12s",
                    b.getBookId(), b.getTitle(), b.getAuthor(), b.getAvailableCopies(), b.getCategory()));
        }
        System.out.println("================================\n");
    }

    public void displayAllMembers() {
        if(members.isEmpty()) {
            System.out.println("No members found!");
            return;
        }

        System.out.println("\n========== All Members ==========");
        System.out.println(String.format("%-10s | %-20s | %-25s | %-15s",
                "Member ID", "Name", "Email", "Books Borrowed"));
        System.out.println("------------------------------------------------------------------------");

        for(Member m : members) {
            System.out.println(String.format("%-10s | %-20s | %-25s | %-15s",
                    m.getMemberId(), m.getName(), m.getEmail(), m.getBorrowedCount() + "/5"));
        }
        System.out.println("==================================\n");
    }

    public void displayBookDetails(Book book) {
        if(book == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("\n========== Book Details ==========");
        System.out.println(book);
        System.out.println("==================================\n");
    }

    public void displayMemberDetails(Member member) {
        if(member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.println("\n========== Member Details ==========");
        System.out.println(member);
        System.out.println("====================================\n");
    }

    public void getLibraryStatistics() {
        int totalBooks = books.size();
        int totalAvailable = 0;
        int totalBorrowed = 0;

        for(Book b : books) {
            totalAvailable += b.getAvailableCopies();
            totalBorrowed += (b.getTotalCopies() - b.getAvailableCopies());
        }

        System.out.println("\n========== Library Statistics ==========");
        System.out.println("Total Books: " + totalBooks);
        System.out.println("Total Copies: " + getTotalCopies());
        System.out.println("Available Copies: " + totalAvailable);
        System.out.println("Borrowed Copies: " + totalBorrowed);
        System.out.println("Total Members: " + members.size());
        System.out.println("=========================================\n");
    }

    private int getTotalCopies() {
        int total = 0;
        for(Book b : books) {
            total += b.getTotalCopies();
        }
        return total;
    }

    public void getCategoryReport() {
        if(books.isEmpty()) {
            System.out.println("No books found!");
            return;
        }

        Map<String, Integer> categoryCount = new HashMap<>();
        Map<String, Integer> categoryAvailable = new HashMap<>();

        for(Book b : books) {
            String category = b.getCategory();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + b.getTotalCopies());
            categoryAvailable.put(category, categoryAvailable.getOrDefault(category, 0) + b.getAvailableCopies());
        }

        System.out.println("\n========== Category Report ==========");
        System.out.println(String.format("%-20s | %-10s | %-12s", "Category", "Total", "Available"));
        System.out.println("-------------------------------------------");

        for(String category : categoryCount.keySet()) {
            System.out.println(String.format("%-20s | %-10d | %-12d",
                    category, categoryCount.get(category), categoryAvailable.get(category)));
        }
        System.out.println("=====================================\n");
    }

    public void deleteBook(String bookId) {
        for(int i = 0; i < books.size(); i++) {
            if(books.get(i).getBookId().equals(bookId)) {
                Book removed = books.remove(i);
                saveBooks();
                System.out.println("Book deleted: " + removed.getTitle());
                return;
            }
        }
        System.out.println("Book not found!");
    }

    public void deleteMember(String memberId) {
        for(int i = 0; i < members.size(); i++) {
            if(members.get(i).getMemberId().equals(memberId)) {
                Member removed = members.remove(i);
                saveMembers();
                System.out.println("Member deleted: " + removed.getName());
                return;
            }
        }
        System.out.println("Member not found!");
    }

    private void saveBooks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(booksFilename));

            for(Book b : books) {
                writer.write(b.getBookId() + "|" + b.getTitle() + "|" + b.getAuthor() + "|" +
                        b.getIsbn() + "|" + b.getCategory() + "|" + b.getTotalCopies() + "|" +
                        b.getAvailableCopies() + "|" + b.getPrice() + "|" + b.getPublicationYear());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void saveMembers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(membersFilename));

            for(Member m : members) {
                writer.write(m.getMemberId() + "|" + m.getName() + "|" + m.getEmail() + "|" +
                        m.getPhone() + "|" + m.getMembershipDate() + "|" + m.getBorrowedCount());
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            File file = new File(booksFilename);
            if(!file.exists()) {
                System.out.println("New books file will be created: " + booksFilename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(booksFilename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 9) {
                    String bookId = parts[0].trim();
                    String title = parts[1].trim();
                    String author = parts[2].trim();
                    String isbn = parts[3].trim();
                    String category = parts[4].trim();
                    int totalCopies = Integer.parseInt(parts[5].trim());
                    double price = Double.parseDouble(parts[7].trim());
                    String publicationYear = parts[8].trim();

                    Book book = new Book(bookId, title, author, isbn, category, totalCopies, price, publicationYear);
                    int available = Integer.parseInt(parts[6].trim());
                    book.setAvailableCopies(available);

                    books.add(book);
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " books from " + booksFilename);
        } catch(IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void loadMembers() {
        try {
            File file = new File(membersFilename);
            if(!file.exists()) {
                System.out.println("New members file will be created: " + membersFilename + "\n");
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(membersFilename));
            String line;
            int count = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 6) {
                    String memberId = parts[0].trim();
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    String phone = parts[3].trim();
                    String membershipDate = parts[4].trim();
                    int borrowedCount = Integer.parseInt(parts[5].trim());

                    Member member = new Member(memberId, name, email, phone, membershipDate);
                    member.setBorrowedCount(borrowedCount);

                    members.add(member);
                    count++;
                }
            }

            reader.close();
            System.out.println("Loaded " + count + " members from " + membersFilename + "\n");
        } catch(IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Library Management System ==========");
        System.out.println("1. Add Book");
        System.out.println("2. Add Member");
        System.out.println("3. View All Books");
        System.out.println("4. View All Members");
        System.out.println("5. Search Book by ID");
        System.out.println("6. Search Book by Author");
        System.out.println("7. Search Book by Category");
        System.out.println("8. Borrow Book");
        System.out.println("9. Return Book");
        System.out.println("10. Library Statistics");
        System.out.println("11. Category Report");
        System.out.println("12. Delete Book");
        System.out.println("13. Delete Member");
        System.out.println("14. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 14) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 14: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Library Management System\n");

        LibraryManagementSystem system = new LibraryManagementSystem("books.txt", "members.txt");

        boolean running = true;
        while(running) {
            system.displayMenu();
            int choice = system.getValidChoice();

            Scanner sc = new Scanner(System.in);

            switch(choice) {
                case 1:
                    System.out.print("Enter book ID: ");
                    String bookId = sc.nextLine();
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Enter category: ");
                    String category = sc.nextLine();
                    System.out.print("Enter total copies: ");
                    int totalCopies = Integer.parseInt(sc.nextLine());
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print("Enter publication year: ");
                    String year = sc.nextLine();

                    Book book = new Book(bookId, title, author, isbn, category, totalCopies, price, year);
                    system.addBook(book);
                    break;

                case 2:
                    System.out.print("Enter member ID: ");
                    String memberId = sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = sc.nextLine();
                    System.out.print("Enter membership date (YYYY-MM-DD): ");
                    String membershipDate = sc.nextLine();

                    Member member = new Member(memberId, name, email, phone, membershipDate);
                    system.addMember(member);
                    break;

                case 3:
                    system.displayAllBooks();
                    break;

                case 4:
                    system.displayAllMembers();
                    break;

                case 5:
                    System.out.print("Enter book ID to search: ");
                    String searchId = sc.nextLine();
                    Book foundBook = system.searchBookById(searchId);
                    system.displayBookDetails(foundBook);
                    break;

                case 6:
                    System.out.print("Enter author name: ");
                    String searchAuthor = sc.nextLine();
                    ArrayList<Book> authorBooks = system.searchBookByAuthor(searchAuthor);

                    if(authorBooks.isEmpty()) {
                        System.out.println("No books found by this author!");
                    } else {
                        System.out.println("Found " + authorBooks.size() + " book(s):");
                        for(Book b : authorBooks) {
                            system.displayBookDetails(b);
                        }
                    }
                    break;

                case 7:
                    System.out.print("Enter category: ");
                    String searchCategory = sc.nextLine();
                    ArrayList<Book> categoryBooks = system.searchBookByCategory(searchCategory);

                    if(categoryBooks.isEmpty()) {
                        System.out.println("No books found in this category!");
                    } else {
                        System.out.println("Found " + categoryBooks.size() + " book(s):");
                        for(Book b : categoryBooks) {
                            System.out.println(String.format("%-8s | %-25s | %-20s",
                                    b.getBookId(), b.getTitle(), b.getAuthor()));
                        }
                    }
                    break;

                case 8:
                    System.out.print("Enter member ID: ");
                    String borrowMemberId = sc.nextLine();
                    System.out.print("Enter book ID: ");
                    String borrowBookId = sc.nextLine();
                    system.borrowBook(borrowMemberId, borrowBookId);
                    break;

                case 9:
                    System.out.print("Enter member ID: ");
                    String returnMemberId = sc.nextLine();
                    System.out.print("Enter book ID: ");
                    String returnBookId = sc.nextLine();
                    system.returnBook(returnMemberId, returnBookId);
                    break;

                case 10:
                    system.getLibraryStatistics();
                    break;

                case 11:
                    system.getCategoryReport();
                    break;

                case 12:
                    System.out.print("Enter book ID to delete: ");
                    String deleteBookId = sc.nextLine();
                    system.deleteBook(deleteBookId);
                    break;

                case 13:
                    System.out.print("Enter member ID to delete: ");
                    String deleteMemberId = sc.nextLine();
                    system.deleteMember(deleteMemberId);
                    break;

                case 14:
                    System.out.println("Thank you for using Library Management System!");
                    running = false;
                    break;
            }
        }
    }
}
