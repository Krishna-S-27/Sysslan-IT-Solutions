package com.inventory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InventoryManager {
    private final InventoryCsvStorage storage;
    private final Path csvPath;
    private final Map<String, Product> inventory = new LinkedHashMap<String, Product>();

    public InventoryManager(Path csvPath, InventoryCsvStorage storage) throws IOException {
        this.csvPath = csvPath;
        this.storage = storage;
        load();
    }

    public void load() throws IOException {
        inventory.clear();
        for (Product product : storage.load(csvPath)) {
            inventory.put(product.getId(), product);
        }
    }

    public void save() throws IOException {
        storage.save(csvPath, getAllProducts());
    }

    public void addProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product already exists: " + product.getId());
        }
        inventory.put(product.getId(), product);
    }

    public Product findById(String id) {
        return inventory.get(id);
    }

    public List<Product> searchByName(String query) {
        List<Product> matches = new ArrayList<Product>();
        String normalized = query.toLowerCase(Locale.ROOT);
        for (Product product : inventory.values()) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(normalized)) {
                matches.add(product);
            }
        }
        return matches;
    }

    public void updateStock(String id, int newQuantity) {
        Product product = requireProduct(id);
        product.setQuantity(newQuantity);
    }

    public void updatePrice(String id, double newPrice) {
        Product product = requireProduct(id);
        product.setPrice(newPrice);
    }

    public List<Product> getLowStockProducts() {
        List<Product> lowStock = new ArrayList<Product>();
        for (Product product : inventory.values()) {
            if (product.isLowStock()) {
                lowStock.add(product);
            }
        }
        Collections.sort(lowStock, new Comparator<Product>() {
            @Override
            public int compare(Product first, Product second) {
                return Integer.compare(first.getQuantity(), second.getQuantity());
            }
        });
        return lowStock;
    }

    public double getTotalInventoryValue() {
        double total = 0.0;
        for (Product product : inventory.values()) {
            total += product.getInventoryValue();
        }
        return total;
    }

    public Map<String, Integer> getCategoryCounts() {
        Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
        for (Product product : inventory.values()) {
            Integer count = counts.get(product.getCategory());
            counts.put(product.getCategory(), count == null ? 1 : count + 1);
        }
        return counts;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>(inventory.values());
        Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product first, Product second) {
                return first.getId().compareToIgnoreCase(second.getId());
            }
        });
        return products;
    }

    private Product requireProduct(String id) {
        Product product = inventory.get(id);
        if (product == null) {
            throw new IllegalArgumentException("Unknown product ID: " + id);
        }
        return product;
    }
}
