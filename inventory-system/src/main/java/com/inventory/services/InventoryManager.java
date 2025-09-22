package com.inventory.services;

import com.inventory.model.Product;
import com.inventory.dao.Productdao;
import com.inventory.exceptions.ProductNotFoundException;
import java.util.List;

public class InventoryManager {

    private Productdao productdao;

    public InventoryManager() {
        productdao = new Productdao();
    }

    // Add product
    public void addProduct(Product product) throws Exception {
        // Optional: check if product already exists
        List<Product> products = productdao.getAllProducts();
        for (Product p : products) {
            if (p.getId() == product.getId()) {
                throw new Exception("Product with ID " + product.getId() + " already exists.");
            }
        }
        productdao.addProduct(product);
    }

    // Remove product
    public void removeProduct(int id) throws ProductNotFoundException {
        if (!productdao.deleteProduct(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    // Update product quantity
    public void updateProductQty(int id, int qty) throws ProductNotFoundException {
       List<Product> products = productdao.getAllProducts();
       for(Product p : products){
            if(p.getId() == id){
                p.setQuantity(qty);
                productdao.updateProduct(p);
                return;
            }
        }
        throw new ProductNotFoundException("Product not found with ID: " + id);
    }

    // Search product by name
    public Product searchProduct(String name) throws ProductNotFoundException {
        List<Product> products = productdao.getAllProducts();
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        throw new ProductNotFoundException("Product not found with name: " + name);
    }

    // Display all products
    public void displayInventory() {
    List<Product> products = productdao.getAllProducts();
    if (products.isEmpty()) {
        System.out.println("⚠️ Inventory is empty.");
    } else {
        System.out.println("📦 Current Inventory:");
        for (Product p : products) {
            System.out.println(p);
        }
    }
}

}
