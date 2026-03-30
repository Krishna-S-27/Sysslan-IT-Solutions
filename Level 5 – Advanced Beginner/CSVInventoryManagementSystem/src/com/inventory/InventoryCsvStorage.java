package com.inventory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryCsvStorage {
    private static final String HEADER = "id,name,category,quantity,price,reorderLevel";

    public List<Product> load(Path csvPath) throws IOException {
        List<Product> products = new ArrayList<Product>();
        if (!Files.exists(csvPath)) {
            return products;
        }

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (firstLine && line.equalsIgnoreCase(HEADER)) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                List<String> values = parseCsvLine(line);
                if (values.size() != 6) {
                    throw new IllegalArgumentException("Invalid CSV row: " + line);
                }

                products.add(new Product(
                        values.get(0),
                        values.get(1),
                        values.get(2),
                        Integer.parseInt(values.get(3)),
                        Double.parseDouble(values.get(4)),
                        Integer.parseInt(values.get(5))
                ));
            }
        }

        return products;
    }

    public void save(Path csvPath, List<Product> products) throws IOException {
        if (csvPath.getParent() != null) {
            Files.createDirectories(csvPath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write(HEADER);
            writer.newLine();
            for (Product product : products) {
                writer.write(product.toCsvRow());
                writer.newLine();
            }
        }
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }

        values.add(current.toString());
        return values;
    }
}
