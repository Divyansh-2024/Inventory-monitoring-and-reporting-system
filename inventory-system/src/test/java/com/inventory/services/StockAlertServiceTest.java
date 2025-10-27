package com.inventory.services;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.mockito.Mockito.*;

class StockAlertServiceTest {

    private StockAlertService service;
    private ProductDAO mockDao;

    @BeforeEach
    void setUp() {
        mockDao = mock(ProductDAO.class);

        // Override the dao field in StockAlertService for testing
        service = new StockAlertService() {
            @Override
            public void checkStockAlert() {
                try {
                    var field = StockAlertService.class.getDeclaredField("dao");
                    field.setAccessible(true);
                    field.set(this, mockDao);
                    super.checkStockAlert();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void testCheckStockAlert_WhenLowStockProductsExist_ShouldSendEmail() throws Exception {
        // Arrange
        List<Product> products = List.of(
                new Product(1, "Mouse", "Electronics", 3, 200.0, 5),
                new Product(2, "Laptop", "Electronics", 10, 60000.0, 15)
        );

        when(mockDao.getAllProducts()).thenReturn(products);

        // ✅ Act and Assert within the same mockStatic scope
        try (MockedStatic<EmailService> mockEmail = mockStatic(EmailService.class)) {

            service.checkStockAlert();

            mockEmail.verify(() ->
                    EmailService.sendAlert(
                            anyString(),
                            contains("Low Stock Alert Summary"),
                            contains("Mouse")
                    ), times(1)
            );
        }
    }
}
