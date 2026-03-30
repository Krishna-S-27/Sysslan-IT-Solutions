package com.inventory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CSVInventoryManagementSystem {
    public static void main(String[] args) {
        Path csvPath = args.length > 0
                ? Paths.get(args[0]).toAbsolutePath().normalize()
                : Paths.get("sample-data", "inventory.csv").toAbsolutePath().normalize();

        Path reportPath = args.length > 1
                ? Paths.get(args[1]).toAbsolutePath().normalize()
                : Paths.get("sample-data", "inventory-report.txt").toAbsolutePath().normalize();

        try {
            InventoryManager inventoryManager = new InventoryManager(csvPath, new InventoryCsvStorage());

            if (inventoryManager.findById("P1005") == null) {
                inventoryManager.addProduct(new Product("P1005", "Portable SSD 1TB", "Storage", 6, 99.99, 4));
            }

            inventoryManager.updateStock("P1002", 9);
            inventoryManager.updatePrice("P1003", 18.75);
            inventoryManager.save();

            InventoryReportGenerator reportGenerator = new InventoryReportGenerator();
            String report = reportGenerator.buildReport(inventoryManager);
            reportGenerator.writeReport(reportPath, report);

            System.out.println(report);

            List<Product> searchResults = inventoryManager.searchByName("usb");
            System.out.println("Search Results for 'usb': " + searchResults.size());
            for (Product product : searchResults) {
                System.out.println(product.getId() + " -> " + product.getName());
            }
        } catch (Exception exception) {
            System.err.println("Inventory task failed: " + exception.getMessage());
            exception.printStackTrace(System.err);
        }
    }
}
