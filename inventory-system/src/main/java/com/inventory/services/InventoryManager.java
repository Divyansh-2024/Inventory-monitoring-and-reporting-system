
package com.inventory.services;
import com.inventory.model.Product;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {

    private Map<Integer, Product> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
    }

    // Add product
    public void addProduct(Product product) {
        inventory.put(product.getId(), product);
        System.out.println("Product added successfully!");
    }

    // Remove product
    public void removeProduct(int id) {
        if (inventory.containsKey(id)) {
            inventory.remove(id);
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    // Update product quantity
    public void updateProductQty(int id, int qty) {
        if (inventory.containsKey(id)) {
            Product p = inventory.get(id);
            p.setQuantity(qty);
            System.out.println("Quantity updated successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    // Search product by name
    public void searchProduct(String name) {
        boolean found = false;
        for (Product p : inventory.values()) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found) System.out.println("Product not found!");
    }

    // Display all products
    public void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("Current Inventory:");
            for (Product p : inventory.values()) {
                System.out.println(p);
            }
        }
    }
}
