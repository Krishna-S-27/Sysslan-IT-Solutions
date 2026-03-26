package ExpenseTracker;

import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExpenseTracker {
    private List<Expense> expenses;
    private String filename;
    private DateTimeFormatter dateFormat;

    class Expense {
        String date;
        String category;
        double amount;
        String description;

        Expense(String date, String category, double amount, String description) {
            this.date = date;
            this.category = category;
            this.amount = amount;
            this.description = description;
        }

        public String toString() {
            return date + " | " + category + " | " + amount + " | " + description;
        }
    }

    public ExpenseTracker() {
        this.expenses = new ArrayList<>();
        this.filename = "expenses.txt";
        this.dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        loadExpenses();
    }

    private void loadExpenses() {
        try {
            File file = new File(filename);
            if(!file.exists()) {
                System.out.println("New expense file created: " + filename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                if(parts.length == 4) {
                    String date = parts[0].trim();
                    String category = parts[1].trim();
                    double amount = Double.parseDouble(parts[2].trim());
                    String description = parts[3].trim();

                    expenses.add(new Expense(date, category, amount, description));
                }
            }

            reader.close();
            System.out.println("Loaded " + expenses.size() + " expenses from file");
        } catch(IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
    }

    private void saveExpenses() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            for(Expense exp : expenses) {
                writer.write(exp.date + " | " + exp.category + " | " + exp.amount + " | " + exp.description);
                writer.newLine();
            }

            writer.close();
        } catch(IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    public void addExpense() {
        Scanner sc = new Scanner(System.in);

        String date = LocalDate.now().format(dateFormat);

        System.out.print("Enter category (Food, Transport, Entertainment, Utilities, Other): ");
        String category = sc.nextLine();

        if(category.isEmpty()) {
            category = "Other";
        }

        System.out.print("Enter amount: ");
        double amount = 0;
        try {
            amount = Double.parseDouble(sc.nextLine());
            if(amount <= 0) {
                System.out.println("Amount must be greater than 0!");
                return;
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }

        System.out.print("Enter description: ");
        String description = sc.nextLine();

        if(description.isEmpty()) {
            description = "No description";
        }

        expenses.add(new Expense(date, category, amount, description));
        saveExpenses();
        System.out.println("Expense added successfully!");
    }

    public void viewAllExpenses() {
        if(expenses.isEmpty()) {
            System.out.println("No expenses found!");
            return;
        }

        System.out.println("\n========== All Expenses ==========");
        System.out.println(String.format("%-12s | %-15s | %-10s | %s", "Date", "Category", "Amount", "Description"));
        System.out.println("-----------------------------------");

        for(Expense exp : expenses) {
            System.out.println(String.format("%-12s | %-15s | %-10.2f | %s", exp.date, exp.category, exp.amount, exp.description));
        }

        System.out.println("==================================\n");
    }

    public void viewByCategory() {
        if(expenses.isEmpty()) {
            System.out.println("No expenses found!");
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter category to filter: ");
        String category = sc.nextLine();

        System.out.println("\n========== Expenses in " + category + " ==========");
        System.out.println(String.format("%-12s | %-15s | %-10s | %s", "Date", "Category", "Amount", "Description"));
        System.out.println("-----------------------------------");

        double total = 0;
        boolean found = false;

        for(Expense exp : expenses) {
            if(exp.category.equalsIgnoreCase(category)) {
                System.out.println(String.format("%-12s | %-15s | %-10.2f | %s", exp.date, exp.category, exp.amount, exp.description));
                total += exp.amount;
                found = true;
            }
        }

        if(!found) {
            System.out.println("No expenses found in this category!");
        } else {
            System.out.println("-----------------------------------");
            System.out.println(String.format("Total: %.2f", total));
        }

        System.out.println("==================================\n");
    }

    public void getTotalExpenses() {
        if(expenses.isEmpty()) {
            System.out.println("No expenses found!");
            return;
        }

        double total = 0;
        for(Expense exp : expenses) {
            total += exp.amount;
        }

        System.out.println("\nTotal Expenses: " + String.format("%.2f", total));
    }

    public void getCategoryBreakdown() {
        if(expenses.isEmpty()) {
            System.out.println("No expenses found!");
            return;
        }

        Map<String, Double> categoryTotal = new HashMap<>();

        for(Expense exp : expenses) {
            categoryTotal.put(exp.category, categoryTotal.getOrDefault(exp.category, 0.0) + exp.amount);
        }

        System.out.println("\n========== Category Breakdown ==========");

        for(String category : categoryTotal.keySet()) {
            double amount = categoryTotal.get(category);
            System.out.println(category + ": " + String.format("%.2f", amount));
        }

        System.out.println("========================================\n");
    }

    public void deleteExpense() {
        if(expenses.isEmpty()) {
            System.out.println("No expenses found!");
            return;
        }

        viewAllExpenses();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter expense number to delete (1-" + expenses.size() + "): ");

        try {
            int index = Integer.parseInt(sc.nextLine()) - 1;

            if(index >= 0 && index < expenses.size()) {
                Expense removed = expenses.remove(index);
                saveExpenses();
                System.out.println("Expense deleted: " + removed);
            } else {
                System.out.println("Invalid expense number!");
            }
        } catch(NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    public void displayMenu() {
        System.out.println("\n========== Expense Tracker ==========");
        System.out.println("1. Add Expense");
        System.out.println("2. View All Expenses");
        System.out.println("3. View Expenses by Category");
        System.out.println("4. Get Total Expenses");
        System.out.println("5. Category Breakdown");
        System.out.println("6. Delete Expense");
        System.out.println("7. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 7) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 7: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();

        System.out.println("Welcome to Expense Tracker");

        boolean running = true;
        while(running) {
            tracker.displayMenu();
            int choice = tracker.getValidChoice();

            switch(choice) {
                case 1:
                    tracker.addExpense();
                    break;

                case 2:
                    tracker.viewAllExpenses();
                    break;

                case 3:
                    tracker.viewByCategory();
                    break;

                case 4:
                    tracker.getTotalExpenses();
                    break;

                case 5:
                    tracker.getCategoryBreakdown();
                    break;

                case 6:
                    tracker.deleteExpense();
                    break;

                case 7:
                    System.out.println("Thank you for using Expense Tracker!");
                    running = false;
                    break;
            }
        }
    }
}