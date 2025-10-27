package com.inventory.services;

import com.inventory.exceptions.DuplicateProductException;
import com.inventory.exceptions.InvalidProductDataException;
import com.inventory.exceptions.ProductNotFoundException;
import com.inventory.model.Product;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryManagerTest {

    private InventoryManager manager;

    @BeforeEach
    void setUp() {
        manager = new InventoryManager(); // fresh instance for each test
    }

    // ---------------- Add Product Scenarios ----------------

    @Test
    @DisplayName("Add product successfully")
    void testAddProductSuccess() {
        Product p = new Product(1, "Laptop", "Electronics", 10, 999.99, 10); // threshold=10
        assertDoesNotThrow(() -> manager.addProduct(p));

        assertDoesNotThrow(() -> {
            Product found = manager.searchProductById(1);
            assertEquals("Laptop", found.getName());
            assertEquals("Electronics", found.getCategory());
            assertEquals(10, found.getQuantity());
            assertEquals(999.99, found.getPrice());
            assertEquals(10, found.getThreshold());
        });
    }

    @Test
    @DisplayName("Add duplicate product should throw DuplicateProductException")
    void testAddDuplicateProduct() throws DuplicateProductException, InvalidProductDataException {
        Product p1 = new Product(2, "Phone", "Electronics", 5, 499.99, 10);
        manager.addProduct(p1);

        Product duplicate = new Product(2, "Phone X", "Electronics", 3, 699.99, 10);
        assertThrows(DuplicateProductException.class, () -> manager.addProduct(duplicate));
    }

    @Test
    @DisplayName("Add invalid product should throw InvalidProductDataException")
    void testAddInvalidProduct() {
        Product invalid = new Product(0, "", "Electronics", 5, 499.99, 10);
        assertThrows(InvalidProductDataException.class, () -> manager.addProduct(invalid));
    }

    // ---------------- Remove Product Scenarios ----------------

    @Test
    @DisplayName("Remove product successfully")
    void testRemoveProductSuccess() throws DuplicateProductException, InvalidProductDataException, ProductNotFoundException {
        Product p = new Product(3, "Book", "Books", 20, 19.99, 10);
        manager.addProduct(p);

        assertDoesNotThrow(() -> manager.removeProduct(3));
        assertThrows(ProductNotFoundException.class, () -> manager.searchProductById(3));
    }

    @Test
    @DisplayName("Remove non-existing product should throw ProductNotFoundException")
    void testRemoveNonExistingProduct() {
        assertThrows(ProductNotFoundException.class, () -> manager.removeProduct(999));
    }

    // ---------------- Update Quantity Scenarios ----------------

    @Test
    @DisplayName("Update product quantity successfully")
    void testUpdateQuantitySuccess() throws DuplicateProductException, InvalidProductDataException, ProductNotFoundException {
        Product p = new Product(4, "Toy Car", "Toys", 15, 9.99, 10);
        manager.addProduct(p);

        manager.updateProductQty(4, 25);
        Product updated = manager.searchProductById(4);
        assertEquals(25, updated.getQuantity());
    }

    @Test
    @DisplayName("Update quantity for non-existing product should throw ProductNotFoundException")
    void testUpdateQuantityNonExisting() {
        assertThrows(ProductNotFoundException.class, () -> manager.updateProductQty(999, 10));
    }

    // ---------------- Search Product Scenarios ----------------

    @Test
    @DisplayName("Search product by ID successfully")
    void testSearchById() throws DuplicateProductException, InvalidProductDataException, ProductNotFoundException {
        Product p = new Product(5, "Banana", "Grocery", 50, 0.99, 10);
        manager.addProduct(p);

        Product found = manager.searchProductById(5);
        assertEquals("Banana", found.getName());
        assertEquals(10, found.getThreshold());
    }

    @Test
    @DisplayName("Search product by non-existing ID should throw ProductNotFoundException")
    void testSearchByIdNonExisting() {
        assertThrows(ProductNotFoundException.class, () -> manager.searchProductById(999));
    }

    @Test
    @DisplayName("Search product by name successfully")
    void testSearchByName() throws DuplicateProductException, InvalidProductDataException, ProductNotFoundException {
        Product p = new Product(6, "Apple", "Grocery", 30, 1.99, 10);
        manager.addProduct(p);

        Product found = manager.searchProduct("Apple");
        assertEquals("Apple", found.getName());
        assertEquals(10, found.getThreshold());
    }

    @Test
    @DisplayName("Search product by non-existing name should throw ProductNotFoundException")
    void testSearchByNameNonExisting() {
        assertThrows(ProductNotFoundException.class, () -> manager.searchProduct("NonExist"));
    }

    // ---------------- Search by Category Scenarios ----------------

    @Test
    @DisplayName("Search products by category successfully")
    void testSearchByCategory() throws DuplicateProductException, InvalidProductDataException, ProductNotFoundException {
        Product p1 = new Product(7, "Shirt", "Clothes", 10, 19.99, 10);
        Product p2 = new Product(8, "Pants", "Clothes", 5, 29.99, 10);
        manager.addProduct(p1);
        manager.addProduct(p2);

        List<Product> clothes = manager.searchProductByCategory("Clothes");
        assertEquals(2, clothes.size());
    }

    @Test
    @DisplayName("Search by non-existing category should throw ProductNotFoundException")
    void testSearchByNonExistingCategory() {
        assertThrows(ProductNotFoundException.class, () -> manager.searchProductByCategory("NonExist"));
    }

    // ---------------- Display Inventory Scenario ----------------

    @Test
    @DisplayName("Display inventory should not throw exception")
    void testDisplayInventory() throws DuplicateProductException, InvalidProductDataException {
        Product p = new Product(9, "Notebook", "Stationery", 100, 2.49, 10);
        manager.addProduct(p);

        assertDoesNotThrow(() -> manager.displayInventory());
    }
}
