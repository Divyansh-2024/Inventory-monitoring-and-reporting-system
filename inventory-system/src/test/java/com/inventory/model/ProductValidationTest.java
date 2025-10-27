package com.inventory.model;

import com.inventory.services.InventoryManager;
import com.inventory.dao.impl.ProductDAOImpl;

import com.inventory.exceptions.DuplicateProductException;
import com.inventory.exceptions.InvalidProductDataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private InventoryManager manager;

    @BeforeEach
    void setUp() {
        // Use the same DAO, but ensure DB is empty before each test
        manager = new InventoryManager(new ProductDAOImpl());
        // Optionally, clear any test products from DB if needed
        // e.g., manager.removeProduct(1); manager.removeProduct(2); etc.
    }

    @Test
    void testInvalidProductId() {
        Product invalidIdProduct = new Product(0, "Laptop", "Electronics", 5, 500.0, 10);
        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(invalidIdProduct));
        assertTrue(exception.getMessage().contains("Invalid product data provided"));
    }

    @Test
    void testEmptyProductName() {
        Product invalidNameProduct = new Product(2, "", "Electronics", 5, 500.0, 10);
        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(invalidNameProduct));
        assertTrue(exception.getMessage().contains("Invalid product data provided"));
    }

    @Test
    void testNullProductName() {
        Product nullNameProduct = new Product(3, null, "Electronics", 5, 500.0, 10);
        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(nullNameProduct));
        assertTrue(exception.getMessage().contains("Invalid product data provided"));
    }

    @Test
    void testNegativeQuantity() {
        Product negativeQtyProduct = new Product(4, "Laptop", "Electronics", -5, 500.0, 10);
        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(negativeQtyProduct));
        assertTrue(exception.getMessage().contains("Invalid product data provided"));
    }

    @Test
    void testNegativePrice() {
        Product negativePriceProduct = new Product(5, "Laptop", "Electronics", 5, -100.0, 10);
        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(negativePriceProduct));
        assertTrue(exception.getMessage().contains("Invalid product data provided"));
    }

    @Test
    void testValidProduct() {
        Product validProduct = new Product(1, "Laptop", "Electronics", 5, 500.0, 10);
        // Remove first in case it exists from previous test
        try { manager.removeProduct(1); } catch (Exception ignored) {}
        assertDoesNotThrow(() -> manager.addProduct(validProduct));
        assertDoesNotThrow(() -> manager.removeProduct(1));
    }

    @Test
    void testDuplicateProduct() {
        Product product1 = new Product(10, "Laptop", "Electronics", 5, 500.0, 10);
        Product duplicateProduct = new Product(10, "Laptop Pro", "Electronics", 3, 800.0, 5);

        // Clean DB first
        try { manager.removeProduct(10); } catch (Exception ignored) {}

        assertDoesNotThrow(() -> manager.addProduct(product1));

        Exception exception = assertThrows(DuplicateProductException.class,
                () -> manager.addProduct(duplicateProduct));

        assertTrue(exception.getMessage().contains("already exists"));

        // Clean up
        try { manager.removeProduct(10); } catch (Exception ignored) {}
    }
}
