package com.inventory.dao;

import com.inventory.model.Product;
import com.inventory.dao.impl.ProductDAOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductDAOImplMockitoTest {

    @Mock
    private ProductDAOImpl productDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProductsByPriceRange() throws SQLException {
        Product p1 = new Product(1, "Mock1", "Cat1", 5, 150.0);
        Product p2 = new Product(2, "Mock2", "Cat2", 3, 300.0);

        when(productDAO.getProductsByPriceRange(100.0, 400.0)).thenReturn(Arrays.asList(p1, p2));

        List<Product> filtered = productDAO.getProductsByPriceRange(100.0, 400.0);

        assertEquals(2, filtered.size());
        assertTrue(filtered.stream().allMatch(prod -> prod.getPrice() >= 100.0 && prod.getPrice() <= 400.0));

        verify(productDAO, times(1)).getProductsByPriceRange(100.0, 400.0);
    }

    @Test
    void testAddProduct() throws SQLException {
        Product p = new Product(10, "MockAdd", "CatAdd", 1, 50.0);
        doNothing().when(productDAO).addProduct(p);

        productDAO.addProduct(p);
        verify(productDAO, times(1)).addProduct(p);
    }

    @Test
    void testDeleteProduct() throws SQLException {
        when(productDAO.deleteProduct(10)).thenReturn(true);

        assertTrue(productDAO.deleteProduct(10));
        verify(productDAO, times(1)).deleteProduct(10);
    }

    @Test
    void testUpdateProduct() throws SQLException {
        Product p = new Product(11, "MockUpdate", "CatUpdate", 2, 75.0);
        doNothing().when(productDAO).updateProduct(p);

        productDAO.updateProduct(p);
        verify(productDAO, times(1)).updateProduct(p);
    }

    @Test
    void testGetAllProducts() throws SQLException {
        List<Product> mockList = Arrays.asList(
                new Product(1, "A", "CatA", 5, 100.0),
                new Product(2, "B", "CatB", 10, 200.0)
        );
        when(productDAO.getAllProducts()).thenReturn(mockList);

        List<Product> products = productDAO.getAllProducts();
        assertEquals(2, products.size());
        verify(productDAO, times(1)).getAllProducts();
    }
}

