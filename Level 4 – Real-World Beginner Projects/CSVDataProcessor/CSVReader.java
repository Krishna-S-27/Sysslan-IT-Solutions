package CSVDataProcessor;

import java.io.*;
import java.util.*;

public class CSVReader {
    private ArrayList<String[]> data;
    private String[] headers;
    private String filename;

    public CSVReader(String filename) {
        this.filename = filename;
        this.data = new ArrayList<>();
        this.headers = null;
        loadCSV();
    }

    private void loadCSV() {
        try {
            File file = new File(filename);
            if(!file.exists()) {
                System.out.println("CSV file not found: " + filename);
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int rowCount = 0;

            while((line = reader.readLine()) != null) {
                if(line.isEmpty()) continue;

                String[] values = parseCSVLine(line);

                if(rowCount == 0) {
                    headers = values;
                } else {
                    data.add(values);
                }
                rowCount++;
            }

            reader.close();
            System.out.println("Loaded " + data.size() + " rows from " + filename);
            System.out.println("Headers: " + Arrays.toString(headers) + "\n");
        } catch(IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }

    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for(int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if(c == '"') {
                insideQuotes = !insideQuotes;
            } else if(c == ',' && !insideQuotes) {
                values.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        values.add(current.toString().trim());
        return values.toArray(new String[0]);
    }

    public String[] getHeaders() {
        return headers;
    }

    public ArrayList<String[]> getData() {
        return data;
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return headers != null ? headers.length : 0;
    }

    public String getValue(int row, int column) {
        if(row >= 0 && row < data.size() && column >= 0 && column < data.get(row).length) {
            return data.get(row)[column];
        }
        return null;
    }

    public String getValue(int row, String columnName) {
        int columnIndex = getColumnIndex(columnName);
        if(columnIndex != -1) {
            return getValue(row, columnIndex);
        }
        return null;
    }

    public int getColumnIndex(String columnName) {
        for(int i = 0; i < headers.length; i++) {
            if(headers[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    public void displayData() {
        if(data.isEmpty()) {
            System.out.println("No data available!");
            return;
        }

        System.out.println("\n========== CSV Data ==========");

        System.out.print(String.format("%-15s", headers[0]));
        for(int i = 1; i < headers.length; i++) {
            System.out.print(" | " + String.format("%-15s", headers[i]));
        }
        System.out.println();
        System.out.println("-".repeat(headers.length * 20));

        for(String[] row : data) {
            System.out.print(String.format("%-15s", row[0]));
            for(int i = 1; i < row.length; i++) {
                System.out.print(" | " + String.format("%-15s", row[i]));
            }
            System.out.println();
        }
        System.out.println("==============================\n");
    }
}