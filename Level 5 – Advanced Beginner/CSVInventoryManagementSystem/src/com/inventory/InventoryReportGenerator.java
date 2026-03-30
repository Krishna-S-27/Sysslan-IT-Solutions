package com.inventory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class InventoryReportGenerator {
    public String buildReport(InventoryManager inventoryManager) {
        StringBuilder builder = new StringBuilder();
        builder.append("CSV INVENTORY MANAGEMENT REPORT").append(System.lineSeparator());
        builder.append("Generated At : ").append(LocalDateTime.now()).append(System.lineSeparator());
        builder.append("Product Count: ").append(inventoryManager.getAllProducts().size()).append(System.lineSeparator());
        builder.append("Total Value  : ").append(String.format(java.util.Locale.US, "%.2f", inventoryManager.getTotalInventoryValue()))
                .append(System.lineSeparator());

        builder.append(System.lineSeparator()).append("Category Counts").append(System.lineSeparator());
        builder.append("---------------").append(System.lineSeparator());
        for (Map.Entry<String, Integer> entry : inventoryManager.getCategoryCounts().entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
        }

        builder.append(System.lineSeparator()).append("Low Stock Products").append(System.lineSeparator());
        builder.append("------------------").append(System.lineSeparator());
        List<Product> lowStockProducts = inventoryManager.getLowStockProducts();
        if (lowStockProducts.isEmpty()) {
            builder.append("No low stock products.").append(System.lineSeparator());
        } else {
            for (Product product : lowStockProducts) {
                builder.append(product.getId()).append(" | ")
                        .append(product.getName()).append(" | Qty: ")
                        .append(product.getQuantity()).append(" | Reorder Level: ")
                        .append(product.getReorderLevel()).append(System.lineSeparator());
            }
        }

        builder.append(System.lineSeparator()).append("Current Inventory").append(System.lineSeparator());
        builder.append("-----------------").append(System.lineSeparator());
        for (Product product : inventoryManager.getAllProducts()) {
            builder.append(product.getId()).append(" | ")
                    .append(product.getName()).append(" | ")
                    .append(product.getCategory()).append(" | Qty: ")
                    .append(product.getQuantity()).append(" | Price: ")
                    .append(String.format(java.util.Locale.US, "%.2f", product.getPrice())).append(" | Value: ")
                    .append(String.format(java.util.Locale.US, "%.2f", product.getInventoryValue()))
                    .append(System.lineSeparator());
        }

        return builder.toString();
    }

    public void writeReport(Path outputPath, String reportText) throws IOException {
        if (outputPath.getParent() != null) {
            Files.createDirectories(outputPath.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write(reportText);
        }
    }
}
