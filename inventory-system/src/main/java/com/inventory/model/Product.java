
package com.inventory.model;

public class Product {
    private int id;
    private String name;
    private String category;
    private int quantity;
    private double price;
    private int threshold;

    public Product(int id, String name, String category, int quantity, double price, int threshold) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.threshold=threshold;
    }

    // Getters and setters
    public int getId() {
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

    public int getThreshold(){
        return threshold;
    }

    public void setId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID must be greater than 0");
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name can't be empty!");
        this.name = name;
    }

    public void setCategory(String category) {
         if (category == null || category.trim().isEmpty())
            throw new IllegalArgumentException("Category can't be empty!");
        this.category = category;
    }

    public void setQuantity(int quantity) {
        if(quantity<0) throw new IllegalArgumentException("Quantity can't be negative");
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        if (price <= 0)
            throw new IllegalArgumentException("price must be greater than 0");
        this.price = price;
    }

    public void setThreshold(int threshold){
        this.threshold=threshold;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Cateogry: " + category + " | Quantity: " + quantity + " | Price: "
                + price + "| Threshold: "+ threshold;
    }
}
