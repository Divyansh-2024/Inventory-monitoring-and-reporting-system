
package com.inventory.services;

import com.inventory.model.Product;
import com.inventory.dao.Productdao;
import java.util.List;

public class InventoryManager {

    private Productdao productdao;

    public InventoryManager() {
        productdao = new Productdao();
    }

    // Add product
    public void addProduct(Product product) {
        productdao.addProduct(product);
    }

    // Remove product
    public void removeProduct(int id) {
        productdao.deleteProduct(id);
    }

    // Update product quantity
    public void updateProductQty(int id, int qty) {
       List<Product> products=productdao.getAllProducts();

       for(Product p:products){
            if(p.getId()==id){
                p.setQuantity(qty);
                productdao.updateProduct(p);
                return;
            }
        }
        System.out.println("Product not found with this ID.");
    }

    // Search product by name
    public void searchProduct(String name) {
        List<Product> products = productdao.getAllProducts();
        boolean found = false;
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.out.println(p);
                found = true;
            }
        }
        if (!found) System.out.println("❌ Product not found!");
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
