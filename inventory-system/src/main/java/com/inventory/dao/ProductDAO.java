package com.inventory.dao;

import com.inventory.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
void addProduct(Product product) throws SQLException;
    List<Product> getAllProducts() throws SQLException;
    void updateProduct(Product product) throws SQLException;
    boolean deleteProduct(int id) throws SQLException;
    Product getProductById(int id) throws SQLException;
    List<Product> getProductsByPriceRange(double minPrice, double maxPrice) throws SQLException;

}
