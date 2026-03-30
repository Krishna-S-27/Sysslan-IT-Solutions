package com.inventory;

public class Product {
    private final String id;
    private final String name;
    private final String category;
    private int quantity;
    private double price;
    private final int reorderLevel;

    public Product(String id, String name, String category, int quantity, double price, int reorderLevel) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.reorderLevel = reorderLevel;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getInventoryValue() {
        return quantity * price;
    }

    public boolean isLowStock() {
        return quantity <= reorderLevel;
    }

    public String toCsvRow() {
        return id + "," + escape(name) + "," + escape(category) + "," + quantity + ","
                + String.format(java.util.Locale.US, "%.2f", price) + "," + reorderLevel;
    }

    private String escape(String value) {
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
