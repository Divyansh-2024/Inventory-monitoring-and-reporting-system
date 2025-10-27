package com.inventory.dao.impl;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import com.inventory.utility.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOImpl implements ProductDAO {

    // CREATE
    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (id, name, category, quantity, price, threshold) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getCategory());
            stmt.setInt(4, product.getQuantity());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getThreshold());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("✖ Error adding product: " + e.getMessage());
        }
    }

    // READ ALL
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapToProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("✖ Error fetching products: " + e.getMessage());
        }

        return products;
    }

    // READ ONE
    @Override
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToProduct(rs);
            }
        } catch (SQLException e) {
            System.out.println("✖ Error fetching product: " + e.getMessage());
        }
        return null;
    }

    // UPDATE
    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name=?, category=?, quantity=?, price=?, threshold=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getCategory());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getThreshold());
            stmt.setInt(6, product.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Product updated with ID: " + product.getId());
            } else {
                System.out.println("⚠️ No product found with ID: " + product.getId());
            }

        } catch (SQLException e) {
            System.out.println("✖ Error updating product: " + e.getMessage());
        }
    }

    // DELETE
    @Override
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("✖ Error deleting product: " + e.getMessage());
            return false;
        }
    }

    // FILTER BY PRICE RANGE
    @Override
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapToProduct(rs));
            }
        }

        return products;
    }

    // FETCH LOW STOCK PRODUCTS
    @Override
    public List<Product> getLowStockProducts() {
        List<Product> lowStockProducts = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE quantity < threshold";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lowStockProducts.add(mapToProduct(rs));
            }

        } catch (SQLException e) {
            System.out.println("✖ Error fetching low stock products: " + e.getMessage());
        }

        return lowStockProducts;
    }

    // MAP RESULTSET → PRODUCT
    private Product mapToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("category"),
                rs.getInt("quantity"),
                rs.getDouble("price"),
                rs.getInt("threshold")
        );
    }
}
