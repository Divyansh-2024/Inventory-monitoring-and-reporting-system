package com.inventory.services;

import com.inventory.model.Product;
import com.inventory.dao.ProductDAO;
import com.inventory.dao.impl.ProductDAOImpl;
import com.inventory.exceptions.ProductNotFoundException;
import java.util.List;

public class InventoryManager {

    private ProductDAO productDAO; // use interface type

    public InventoryManager() {
        productDAO = new ProductDAOImpl(); // use implementation
    }

    // Add product
    public void addProduct(Product product) throws Exception {
        // Optional: check if product already exists
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                throw new Exception("Product with ID " + product.getId() + " already exists.");
            }
        }
        productDAO.addProduct(product);
    }

    // Remove product
    public void removeProduct(int id) throws ProductNotFoundException {
        if (!productDAO.deleteProduct(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    // Update product quantity
    public void updateProductQty(int id, int qty) throws ProductNotFoundException {
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            if (p.getId() == id) {
                p.setQuantity(qty);
                productDAO.updateProduct(p);
                return;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    // Search product by name
    public Product searchProduct(String name) throws ProductNotFoundException {
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        throw new ProductNotFoundException("Product not found with name: " + name);
    }

    // Display all products
    public void displayInventory() {
        List<Product> products = productDAO.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("⚠️ Inventory is empty.");
        } else {
            System.out.println("📦 Current Inventory:");
            for (Product p : products) {
                System.out.println(p);
            }
        }
    }

    public Product searchProductById(int id) throws ProductNotFoundException {
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    // Search by Category
    public List<Product> searchProductByCategory(String category) throws ProductNotFoundException {
        List<Product> products = productDAO.getAllProducts();
        List<Product> results = new java.util.ArrayList<>();
        for (Product p : products) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                results.add(p);
            }
        }
        if (results.isEmpty()) {
            throw new ProductNotFoundException("No products found in category: " + category);
        }
        return results;

    }
}
