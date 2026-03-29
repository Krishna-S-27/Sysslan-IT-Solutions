package CSVDataProcessor;

import java.util.*;
import java.io.*;

public class DataAnalytics {
    private CSVReader csvReader;

    public DataAnalytics(CSVReader csvReader) {
        this.csvReader = csvReader;
    }

    public void displaySummary() {
        System.out.println("\n========== Data Summary ==========");
        System.out.println("Total Rows: " + csvReader.getRowCount());
        System.out.println("Total Columns: " + csvReader.getColumnCount());
        System.out.println("Headers: " + Arrays.toString(csvReader.getHeaders()));
        System.out.println("==================================\n");
    }

    public double getNumericColumnSum(String columnName) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return 0;
        }

        double sum = 0;
        for(String[] row : csvReader.getData()) {
            try {
                sum += Double.parseDouble(row[columnIndex].trim());
            } catch(NumberFormatException e) {
                continue;
            }
        }
        return sum;
    }

    public double getNumericColumnAverage(String columnName) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return 0;
        }

        double sum = 0;
        int count = 0;
        for(String[] row : csvReader.getData()) {
            try {
                sum += Double.parseDouble(row[columnIndex].trim());
                count++;
            } catch(NumberFormatException e) {
                continue;
            }
        }

        return count > 0 ? sum / count : 0;
    }

    public double getNumericColumnMax(String columnName) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return Double.MIN_VALUE;
        }

        double max = Double.MIN_VALUE;
        for(String[] row : csvReader.getData()) {
            try {
                double value = Double.parseDouble(row[columnIndex].trim());
                if(value > max) {
                    max = value;
                }
            } catch(NumberFormatException e) {
                continue;
            }
        }

        return max;
    }

    public double getNumericColumnMin(String columnName) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return Double.MAX_VALUE;
        }

        double min = Double.MAX_VALUE;
        for(String[] row : csvReader.getData()) {
            try {
                double value = Double.parseDouble(row[columnIndex].trim());
                if(value < min) {
                    min = value;
                }
            } catch(NumberFormatException e) {
                continue;
            }
        }

        return min;
    }

    public Map<String, Integer> getCategoryCount(String columnName) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return new HashMap<>();
        }

        Map<String, Integer> categoryCount = new HashMap<>();
        for(String[] row : csvReader.getData()) {
            String category = row[columnIndex].trim();
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }

        return categoryCount;
    }

    public void displayCategoryDistribution(String columnName) {
        Map<String, Integer> distribution = getCategoryCount(columnName);

        if(distribution.isEmpty()) {
            System.out.println("No data found!");
            return;
        }

        System.out.println("\n========== Category Distribution: " + columnName + " ==========");
        System.out.println(String.format("%-20s | %-10s | %-15s", "Category", "Count", "Percentage"));
        System.out.println("-".repeat(50));

        int total = csvReader.getRowCount();
        for(String category : distribution.keySet()) {
            int count = distribution.get(category);
            double percentage = (count * 100.0) / total;
            System.out.println(String.format("%-20s | %-10d | %-14.2f%%", category, count, percentage));
        }
        System.out.println("=".repeat(50) + "\n");
    }

    public void displayNumericStatistics(String columnName) {
        System.out.println("\n========== Statistics: " + columnName + " ==========");
        System.out.println("Sum       : " + String.format("%.2f", getNumericColumnSum(columnName)));
        System.out.println("Average   : " + String.format("%.2f", getNumericColumnAverage(columnName)));
        System.out.println("Maximum   : " + String.format("%.2f", getNumericColumnMax(columnName)));
        System.out.println("Minimum   : " + String.format("%.2f", getNumericColumnMin(columnName)));
        System.out.println("=".repeat(45) + "\n");
    }

    public ArrayList<String[]> filterByColumn(String columnName, String value) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return new ArrayList<>();
        }

        ArrayList<String[]> filtered = new ArrayList<>();
        for(String[] row : csvReader.getData()) {
            if(row[columnIndex].equalsIgnoreCase(value)) {
                filtered.add(row);
            }
        }

        return filtered;
    }

    public void displayFiltered(ArrayList<String[]> filteredData, String filterName) {
        if(filteredData.isEmpty()) {
            System.out.println("No matching records found!");
            return;
        }

        System.out.println("\n========== Filtered Results: " + filterName + " ==========");
        System.out.println("Matching Records: " + filteredData.size());
        System.out.println();

        String[] headers = csvReader.getHeaders();
        System.out.print(String.format("%-15s", headers[0]));
        for(int i = 1; i < headers.length; i++) {
            System.out.print(" | " + String.format("%-15s", headers[i]));
        }
        System.out.println();
        System.out.println("-".repeat(headers.length * 20));

        for(String[] row : filteredData) {
            System.out.print(String.format("%-15s", row[0]));
            for(int i = 1; i < row.length; i++) {
                System.out.print(" | " + String.format("%-15s", row[i]));
            }
            System.out.println();
        }
        System.out.println("=".repeat(headers.length * 20) + "\n");
    }

    public ArrayList<String[]> sortByColumn(String columnName, boolean ascending) {
        int columnIndex = csvReader.getColumnIndex(columnName);
        if(columnIndex == -1) {
            System.out.println("Column not found: " + columnName);
            return new ArrayList<>();
        }

        ArrayList<String[]> sorted = new ArrayList<>(csvReader.getData());
        final int index = columnIndex;
        final boolean asc = ascending;

        sorted.sort((a, b) -> {
            String valA = a[index].trim();
            String valB = b[index].trim();

            try {
                double numA = Double.parseDouble(valA);
                double numB = Double.parseDouble(valB);
                return asc ? Double.compare(numA, numB) : Double.compare(numB, numA);
            } catch(NumberFormatException e) {
                return asc ? valA.compareTo(valB) : valB.compareTo(valA);
            }
        });

        return sorted;
    }

    public void displaySorted(ArrayList<String[]> sortedData, String columnName, boolean ascending) {
        if(sortedData.isEmpty()) {
            System.out.println("No data to sort!");
            return;
        }

        String order = ascending ? "Ascending" : "Descending";
        System.out.println("\n========== Sorted by " + columnName + " (" + order + ") ==========");

        String[] headers = csvReader.getHeaders();
        System.out.print(String.format("%-15s", headers[0]));
        for(int i = 1; i < headers.length; i++) {
            System.out.print(" | " + String.format("%-15s", headers[i]));
        }
        System.out.println();
        System.out.println("-".repeat(headers.length * 20));

        for(String[] row : sortedData) {
            System.out.print(String.format("%-15s", row[0]));
            for(int i = 1; i < row.length; i++) {
                System.out.print(" | " + String.format("%-15s", row[i]));
            }
            System.out.println();
        }
        System.out.println("=".repeat(headers.length * 20) + "\n");
    }

    public void exportSummaryReport(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("CSV DATA ANALYSIS REPORT\n");
            writer.write("=".repeat(50) + "\n\n");

            writer.write("SUMMARY\n");
            writer.write("Total Rows: " + csvReader.getRowCount() + "\n");
            writer.write("Total Columns: " + csvReader.getColumnCount() + "\n");
            writer.write("Headers: " + Arrays.toString(csvReader.getHeaders()) + "\n\n");

            writer.write("COLUMN ANALYSIS\n");
            writer.write("-".repeat(50) + "\n");

            String[] headers = csvReader.getHeaders();
            for(String header : headers) {
                writer.write("\nColumn: " + header + "\n");

                try {
                    double avg = getNumericColumnAverage(header);
                    writer.write("  Average: " + String.format("%.2f", avg) + "\n");
                    writer.write("  Max: " + String.format("%.2f", getNumericColumnMax(header)) + "\n");
                    writer.write("  Min: " + String.format("%.2f", getNumericColumnMin(header)) + "\n");
                } catch(Exception e) {
                    Map<String, Integer> dist = getCategoryCount(header);
                    writer.write("  Categories: " + dist.size() + "\n");
                }
            }

            writer.close();
            System.out.println("Report exported to " + filename);
        } catch(IOException e) {
            System.out.println("Error exporting report: " + e.getMessage());
        }
    }
}
