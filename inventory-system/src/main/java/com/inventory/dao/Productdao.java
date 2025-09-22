package com.inventory.dao;

import com.inventory.model.Product;
import com.inventory.utility.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Productdao {

    // CREATE
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (id, name, category, quantity, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());           
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());
            stmt.executeUpdate();
            System.out.println("✅ Product added with ID: " + product.getId());

        } catch (SQLException e) {
            System.out.println("❌ Error adding product: " + e.getMessage());
        }
    }

    // READ
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching products: " + e.getMessage());
        }
        return products;
    }

    // UPDATE
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, quantity=?, price=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(1, product.getCategory());
            stmt.setInt(2, product.getQuantity());
            stmt.setDouble(3, product.getPrice());
            stmt.setInt(4, product.getId());
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Product updated with ID: " + product.getId());
            else
                System.out.println("⚠️ No product found with ID: " + product.getId());

        } catch (SQLException e) {
            System.out.println("❌ Error updating product: " + e.getMessage());
        }
    }

    // DELETE
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("🗑 Product deleted with ID: " + id);
            else
                System.out.println("⚠️ No product found with ID: " + id);

        } catch (SQLException e) {
            System.out.println("❌ Error deleting product: " + e.getMessage());
        }
    }
}
