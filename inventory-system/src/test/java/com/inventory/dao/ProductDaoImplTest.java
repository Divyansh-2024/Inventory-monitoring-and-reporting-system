package com.inventory.dao;

import com.inventory.dao.impl.ProductDAOImpl;
import com.inventory.model.Product;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOImplTest {

    private ProductDAOImpl productDAO;

    @BeforeEach
    void setUp() {
        productDAO = new ProductDAOImpl();
    }

    @Test
    void testAddAndGetAllProducts() throws SQLException {
        Product p = new Product(999, "Test Product", "TestCategory", 10, 299.99, 10);
        productDAO.addProduct(p);

        List<Product> products = productDAO.getAllProducts();
        assertTrue(products.stream().anyMatch(prod -> prod.getId() == 999));

        productDAO.deleteProduct(999);
    }

    @Test
    void testUpdateProduct() throws SQLException {
        Product p = new Product(1000, "Update Product", "TestCategory", 5, 150.0, 10);
        productDAO.addProduct(p);

        p.setQuantity(20);
        p.setPrice(175.0);
        productDAO.updateProduct(p);

        Product updated = productDAO.getAllProducts().stream()
                .filter(prod -> prod.getId() == 1000)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(20, updated.getQuantity());
        assertEquals(175.0, updated.getPrice());

        productDAO.deleteProduct(1000);
    }

    @Test
    void testDeleteProduct() throws SQLException {
        Product p = new Product(1001, "Delete Product", "TestCategory", 1, 50.0, 10);
        productDAO.addProduct(p);

        assertTrue(productDAO.deleteProduct(1001));
        assertFalse(productDAO.getAllProducts().stream()
                .anyMatch(prod -> prod.getId() == 1001));
    }

    @Test
    void testGetProductsByPriceRange() throws SQLException {
        Product p1 = new Product(2001, "Price1", "CategoryA", 10, 120.0, 10);
        Product p2 = new Product(2002, "Price2", "CategoryA", 5, 350.0, 10);
        Product p3 = new Product(2003, "Price3", "CategoryB", 7, 600.0, 10);
        productDAO.addProduct(p1);
        productDAO.addProduct(p2);
        productDAO.addProduct(p3);

        List<Product> filtered = productDAO.getProductsByPriceRange(100.0, 400.0);
        assertTrue(filtered.stream().allMatch(prod ->
                prod.getPrice() >= 100.0 && prod.getPrice() <= 400.0));

        productDAO.deleteProduct(2001);
        productDAO.deleteProduct(2002);
        productDAO.deleteProduct(2003);
    }

    @Test
    void testGetLowStockProducts() {
        Product lowStock = new Product(3001, "LowStockProduct", "CategoryLow", 3, 99.0, 10);
        Product normal = new Product(3002, "NormalStockProduct", "CategoryNormal", 15, 199.0, 10);
        productDAO.addProduct(lowStock);
        productDAO.addProduct(normal);

        List<Product> lowStockList = productDAO.getLowStockProducts();
        assertTrue(lowStockList.stream().anyMatch(p -> p.getName().equals("LowStockProduct")));
        assertFalse(lowStockList.stream().anyMatch(p -> p.getName().equals("NormalStockProduct")));

        productDAO.deleteProduct(3001);
        productDAO.deleteProduct(3002);
    }
}
