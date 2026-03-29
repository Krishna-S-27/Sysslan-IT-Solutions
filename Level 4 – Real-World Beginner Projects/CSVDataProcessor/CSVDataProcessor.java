package CSVDataProcessor;

import java.util.*;

public class CSVDataProcessor {
    private CSVReader csvReader;
    private DataAnalytics analytics;

    public CSVDataProcessor(String filename) {
        this.csvReader = new CSVReader(filename);
        this.analytics = new DataAnalytics(csvReader);
    }

    public void displayMenu() {
        System.out.println("\n========== CSV Data Processor ==========");
        System.out.println("1. View Data");
        System.out.println("2. View Summary");
        System.out.println("3. Numeric Statistics");
        System.out.println("4. Category Distribution");
        System.out.println("5. Filter by Column");
        System.out.println("6. Sort by Column");
        System.out.println("7. Export Report");
        System.out.println("8. Exit");
        System.out.print("Enter choice: ");
    }

    private int getValidChoice() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            try {
                int choice = Integer.parseInt(sc.nextLine());
                if(choice >= 1 && choice <= 8) {
                    return choice;
                } else {
                    System.out.print("Enter a number between 1 and 8: ");
                }
            } catch(NumberFormatException e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to CSV Data Processor\n");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter CSV filename: ");
        String filename = sc.nextLine();

        if(filename.isEmpty()) {
            System.out.println("No filename provided!");
            return;
        }

        CSVDataProcessor processor = new CSVDataProcessor(filename);

        if(processor.csvReader.getRowCount() == 0) {
            System.out.println("No data loaded. Exiting...");
            return;
        }

        boolean running = true;
        while(running) {
            processor.displayMenu();
            int choice = processor.getValidChoice();

            switch(choice) {
                case 1:
                    processor.csvReader.displayData();
                    break;

                case 2:
                    processor.analytics.displaySummary();
                    break;

                case 3:
                    System.out.println("Available columns:");
                    String[] headers = processor.csvReader.getHeaders();
                    for(int i = 0; i < headers.length; i++) {
                        System.out.println((i + 1) + ". " + headers[i]);
                    }
                    System.out.print("Select column number: ");
                    int colNum = Integer.parseInt(sc.nextLine());

                    if(colNum >= 1 && colNum <= headers.length) {
                        processor.analytics.displayNumericStatistics(headers[colNum - 1]);
                    } else {
                        System.out.println("Invalid selection!");
                    }
                    break;

                case 4:
                    System.out.println("Available columns:");
                    String[] headers2 = processor.csvReader.getHeaders();
                    for(int i = 0; i < headers2.length; i++) {
                        System.out.println((i + 1) + ". " + headers2[i]);
                    }
                    System.out.print("Select column number: ");
                    int catNum = Integer.parseInt(sc.nextLine());

                    if(catNum >= 1 && catNum <= headers2.length) {
                        processor.analytics.displayCategoryDistribution(headers2[catNum - 1]);
                    } else {
                        System.out.println("Invalid selection!");
                    }
                    break;

                case 5:
                    System.out.println("Available columns:");
                    String[] headers3 = processor.csvReader.getHeaders();
                    for(int i = 0; i < headers3.length; i++) {
                        System.out.println((i + 1) + ". " + headers3[i]);
                    }
                    System.out.print("Select column number: ");
                    int filterNum = Integer.parseInt(sc.nextLine());

                    if(filterNum >= 1 && filterNum <= headers3.length) {
                        System.out.print("Enter value to filter: ");
                        String filterValue = sc.nextLine();
                        ArrayList<String[]> filtered = processor.analytics.filterByColumn(
                                headers3[filterNum - 1], filterValue);
                        processor.analytics.displayFiltered(filtered,
                                headers3[filterNum - 1] + " = " + filterValue);
                    } else {
                        System.out.println("Invalid selection!");
                    }
                    break;

                case 6:
                    System.out.println("Available columns:");
                    String[] headers4 = processor.csvReader.getHeaders();
                    for(int i = 0; i < headers4.length; i++) {
                        System.out.println((i + 1) + ". " + headers4[i]);
                    }
                    System.out.print("Select column number: ");
                    int sortNum = Integer.parseInt(sc.nextLine());

                    if(sortNum >= 1 && sortNum <= headers4.length) {
                        System.out.print("Sort ascending? (y/n): ");
                        boolean ascending = sc.nextLine().equalsIgnoreCase("y");
                        ArrayList<String[]> sorted = processor.analytics.sortByColumn(
                                headers4[sortNum - 1], ascending);
                        processor.analytics.displaySorted(sorted, headers4[sortNum - 1], ascending);
                    } else {
                        System.out.println("Invalid selection!");
                    }
                    break;

                case 7:
                    System.out.print("Enter export filename: ");
                    String exportFile = sc.nextLine();
                    processor.analytics.exportSummaryReport(exportFile);
                    break;

                case 8:
                    System.out.println("Thank you for using CSV Data Processor!");
                    running = false;
                    break;
            }
        }

        sc.close();
    }
}
