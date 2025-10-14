package com.inventory.services;

import com.inventory.model.Product;
import com.inventory.dao.ProductDAO;
import com.inventory.dao.impl.ProductDAOImpl;
import com.inventory.exceptions.DuplicateProductException;
import com.inventory.exceptions.InvalidProductDataException;
import com.inventory.exceptions.ProductNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class InventoryManager {

    private final ProductDAO productDAO;

    public InventoryManager() {
        productDAO = new ProductDAOImpl();
    }

    // Add product
    public void addProduct(Product product) throws DuplicateProductException, InvalidProductDataException {
        if (product == null || product.getId() <= 0 || product.getName() == null || product.getName().isEmpty()) {
            throw new InvalidProductDataException("⚠️ Invalid product data provided!");
        }

        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getId() == product.getId()) {
                    throw new DuplicateProductException("Product with ID " + product.getId() + " already exists.");
                }
            }
            productDAO.addProduct(product);
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            System.out.println("Database error while adding product: " + e.getMessage());
        }
    }

    // Remove product
    public void removeProduct(int id) throws ProductNotFoundException {
        try {
            if (!productDAO.deleteProduct(id)) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            } else {
                System.out.println("Product removed successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Database error while removing product: " + e.getMessage());
        }
    }

    // Update product quantity
    public void updateProductQty(int id, int qty) throws ProductNotFoundException {
        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getId() == id) {
                    p.setQuantity(qty);
                    productDAO.updateProduct(p);
                    System.out.println("✅ Quantity updated successfully for product ID " + id);
                    return;
                }
            }
            throw new ProductNotFoundException("Product not found with ID: " + id);
        } catch (SQLException e) {
            System.out.println("Database error while updating product: " + e.getMessage());
        }
    }

    // Search by name
    public Product searchProduct(String name) throws ProductNotFoundException {
        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(name)) {
                    printProductTable(List.of(p));
                    return p;
                }
            }
            throw new ProductNotFoundException("Product not found with name: " + name);
        } catch (SQLException e) {
            System.out.println("Database error while searching product: " + e.getMessage());
            return null;
        }
    }

    // Display all
    public void displayInventory() {
        try {
            List<Product> products = productDAO.getAllProducts();
            if (products.isEmpty()) {
                System.out.println("⚠️ Inventory is empty.");
            } else {
                printProductTable(products);
            }
        } catch (SQLException e) {
            System.out.println("Database error while displaying inventory: " + e.getMessage());
        }
    }

    // Search by ID
    public Product searchProductById(int id) throws ProductNotFoundException {
        try {
            Product product = productDAO.getProductById(id);
            if (product == null) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            }
            printProductTable(List.of(product));
            return product;
        } catch (SQLException e) {
            System.out.println("Database error while searching product by ID: " + e.getMessage());
            return null;
        }
    }

    // Search by category
    public List<Product> searchProductByCategory(String category) throws ProductNotFoundException {
        try {
            List<Product> products = productDAO.getAllProducts();
            List<Product> results = new ArrayList<>();
            for (Product p : products) {
                if (p.getCategory().equalsIgnoreCase(category)) {
                    results.add(p);
                }
            }
            if (results.isEmpty()) {
                throw new ProductNotFoundException("No products found in category: " + category);
            } else {
                printProductTable(results);
            }
            return results;
        } catch (SQLException e) {
            System.out.println("Database error while searching by category: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Print table
    public void printProductTable(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products to display!");
            return;
        }

        System.out.println("\nInventory List:");
        System.out.println("┌──────┬──────────────────────────────┬───────────────────────┬───────────┬───────────┐");
        System.out.printf("| %-4s | %-28s | %-21s | %-9s | %-9s |\n",
                "ID", "Name", "Category", "Qty", "Price");
        System.out.println("├──────┼──────────────────────────────┼───────────────────────┼───────────┼───────────┤");

        for (Product p : products) {
            System.out.printf("| %-4d | %-28s | %-21s | %-9d | %-9.2f |\n",
                    p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice());
        }

        System.out.println("└──────┴──────────────────────────────┴───────────────────────┴───────────┴───────────┘");
    }

    // Search by price range
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) throws ProductNotFoundException {
        try {
            List<Product> filteredProducts = productDAO.getProductsByPriceRange(minPrice, maxPrice);

            if (filteredProducts.isEmpty()) {
                throw new ProductNotFoundException(
                        "No products found in the price range: " + minPrice + " - " + maxPrice);
            } else {
                System.out.println("\nProducts in price range " + minPrice + " - " + maxPrice + ":");
                printProductTable(filteredProducts);
            }

            return filteredProducts;

        } catch (SQLException e) {
            System.out.println("Database error while fetching products by price range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    //csv report
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            System.out.println("Database error while getting all products: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
