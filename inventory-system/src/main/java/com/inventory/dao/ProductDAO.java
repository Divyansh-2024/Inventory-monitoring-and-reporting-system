package com.inventory.dao;

import com.inventory.model.Product;
import java.util.List;

public interface ProductDAO {
    void addProduct(Product product);
    List<Product> getAllProducts();
    void updateProduct(Product product);
    boolean deleteProduct(int id);
    Product getProductById(int id);
}
