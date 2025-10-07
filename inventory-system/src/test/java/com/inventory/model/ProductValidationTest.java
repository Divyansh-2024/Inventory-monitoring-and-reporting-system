package com.inventory.model;

import com.inventory.services.InventoryManager;
import com.inventory.exceptions.DuplicateProductException;
import com.inventory.exceptions.InvalidProductDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductValidationTest {

    private InventoryManager manager;

    @BeforeEach
    void setUp() {
        manager = new InventoryManager();
    }

    @Test
    void testValidProduct() {
        Product validProduct = new Product(1, "Laptop", "Electronics", 5, 500.0);

        assertDoesNotThrow(() -> manager.addProduct(validProduct));

        assertDoesNotThrow(() -> manager.removeProduct(1));
    }

    @Test
    void testInvalidProductId() {
        Product invalidIdProduct = new Product(0, "Laptop", "Electronics", 5, 500.0);

        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(invalidIdProduct));

        assertEquals("Invalid product data provided!", exception.getMessage());
    }

    @Test
    void testEmptyProductName() {
        Product invalidNameProduct = new Product(2, "", "Electronics", 5, 500.0);

        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(invalidNameProduct));

        assertEquals("Invalid product data provided!", exception.getMessage());
    }

    @Test
    void testNullProductName() {
        Product nullNameProduct = new Product(3, null, "Electronics", 5, 500.0);

        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(nullNameProduct));

        assertEquals("Invalid product data provided!", exception.getMessage());
    }

    @Test
    void testNegativeQuantity() {
        Product negativeQtyProduct = new Product(4, "Laptop", "Electronics", -5, 500.0);

        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(negativeQtyProduct));

        assertEquals("Invalid product data provided!", exception.getMessage());
    }

    @Test
    void testNegativePrice() {
        Product negativePriceProduct = new Product(5, "Laptop", "Electronics", 5, -100.0);

        Exception exception = assertThrows(InvalidProductDataException.class,
                () -> manager.addProduct(negativePriceProduct));

        assertEquals("Invalid product data provided!", exception.getMessage());
    }

    @Test
    void testDuplicateProduct() throws InvalidProductDataException, DuplicateProductException {
        Product product1 = new Product(6, "Mouse", "Electronics", 10, 50.0);
        Product product2 = new Product(6, "Mouse", "Electronics", 10, 50.0);

        
        assertDoesNotThrow(() -> manager.addProduct(product1));
       
        Exception exception = assertThrows(DuplicateProductException.class,
                () -> manager.addProduct(product2));

        assertEquals("Product with ID 6 already exists.", exception.getMessage());

        assertDoesNotThrow(() -> manager.removeProduct(6));
    }
}
