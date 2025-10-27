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

    // ANSI Colors
    private static final String RESET = "\u001B[0m";
    private static final String GREEN_BOLD = "\u001B[1;32m";
    private static final String RED_BOLD = "\u001B[1;31m";
    private static final String YELLOW_BOLD = "\u001B[1;33m";
    private static final String BLUE_BOLD = "\u001B[1;34m";
    private static final String CYAN_BOLD = "\u001B[1;36m";
    private static final String MAGENTA_BOLD = "\u001B[1;35m";

    public InventoryManager() {
        productDAO = new ProductDAOImpl();
    }

    public InventoryManager(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    // ✅ Add product
    public void addProduct(Product product) throws DuplicateProductException, InvalidProductDataException {
        if (product == null || product.getId() <= 0 || product.getName() == null || product.getName().isEmpty()
                || product.getQuantity() < 0 || product.getPrice() < 0) {
            throw new InvalidProductDataException("Invalid product data provided!");
        }

        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getId() == product.getId()) {
                    throw new DuplicateProductException("Product with ID " + product.getId() + " already exists.");
                }
            }
            productDAO.addProduct(product);
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while adding product: " + e.getMessage() + RESET);
        }
    }

    // ✅ Remove product
    public void removeProduct(int id) throws ProductNotFoundException {
        try {
            if (!productDAO.deleteProduct(id)) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            } else {
                System.out.println(GREEN_BOLD + "Product removed successfully!" + RESET);
            }
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while removing product: " + e.getMessage() + RESET);
        }
    }

    // ✅ Update quantity
    public void updateProductQty(int id, int qty) throws ProductNotFoundException {
        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getId() == id) {
                    p.setQuantity(qty);
                    productDAO.updateProduct(p);
                    return;
                }
            }
            throw new ProductNotFoundException("Product not found with ID: " + id);
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while updating product: " + e.getMessage() + RESET);
        }
    }

    // ✅ Search by name
    public Product searchProduct(String name) throws ProductNotFoundException {
        try {
            List<Product> products = productDAO.getAllProducts();
            for (Product p : products) {
                if (p.getName().equalsIgnoreCase(name)) {
                    System.out.println(CYAN_BOLD + "\n Product found:" + RESET);
                    printProductTable(List.of(p));
                    return p;
                }
            }
            throw new ProductNotFoundException("Product not found with name: " + name);
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while searching product: " + e.getMessage() + RESET);
            return null;
        }
    }

    // ✅ Display all
    public void displayInventory() {
        try {
            List<Product> products = productDAO.getAllProducts();
            if (products.isEmpty()) {
                System.out.println(YELLOW_BOLD + "⚠️ Inventory is empty." + RESET);
            } else {
                System.out.println(BLUE_BOLD + "\nCOMPLETE INVENTORY LIST:" + RESET);
                printProductTable(products);
            }
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while displaying inventory: " + e.getMessage() + RESET);
        }
    }

    // ✅ Search by ID
    public Product searchProductById(int id) throws ProductNotFoundException {
        try {
            Product product = productDAO.getProductById(id);
            if (product == null) {
                throw new ProductNotFoundException("Product not found with ID: " + id);
            }
            System.out.println(CYAN_BOLD + "\nProduct details for ID " + id + ":" + RESET);
            printProductTable(List.of(product));
            return product;
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while searching product by ID: " + e.getMessage() + RESET);
            return null;
        }
    }

    // ✅ Search by category
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
                System.out.println(CYAN_BOLD + "\n Products in category: " + category + RESET);
                printProductTable(results);
            }
            return results;
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while searching by category: " + e.getMessage() + RESET);
            return new ArrayList<>();
        }
    }

    // ✅ Price range search
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) throws ProductNotFoundException {
        try {
            List<Product> filteredProducts = productDAO.getProductsByPriceRange(minPrice, maxPrice);

            if (filteredProducts.isEmpty()) {
                throw new ProductNotFoundException(
                        "⚠️ No products found in the price range " + minPrice + " - " + maxPrice);
            } else {
                System.out.println(
                        CYAN_BOLD + "\n Products priced between " + minPrice + " and " + maxPrice + ":" + RESET);
                printProductTable(filteredProducts);
            }

            return filteredProducts;

        } catch (SQLException e) {
            System.out.println(
                    RED_BOLD + "Database error while fetching products by price range: " + e.getMessage() + RESET);
            return new ArrayList<>();
        }
    }

    // ✅ Get all products (for CSV/email report)
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException e) {
            System.out.println(RED_BOLD + "Database error while getting all products: " + e.getMessage() + RESET);
            return new ArrayList<>();
        }
    }

    // ✅ Print product table
    public void printProductTable(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(YELLOW_BOLD + "⚠️ No products to display!" + RESET);
            return;
        }

        System.out.println(MAGENTA_BOLD
                + "┌──────┬──────────────────────────────┬───────────────────────┬───────────┬───────────┬───────────┐"
                + RESET);
        System.out.printf(MAGENTA_BOLD + "| %-4s | %-28s | %-21s | %-9s | %-9s | %-9s |\n" + RESET,
                "ID", "Name", "Category", "Qty", "Price", "Threshold");
        System.out.println(MAGENTA_BOLD
                + "├──────┼──────────────────────────────┼───────────────────────┼───────────┼───────────┼───────────┤"
                + RESET);

        for (Product p : products) {
            System.out.printf(BLUE_BOLD + "| %-4d | %-28s | %-21s | %-9d | %-9.2f | %-9d |\n" + RESET,
                    p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice(), p.getThreshold());
        }

        System.out.println(MAGENTA_BOLD
                + "└──────┴──────────────────────────────┴───────────────────────┴───────────┴───────────┴───────────┘"
                + RESET);
    }
}
