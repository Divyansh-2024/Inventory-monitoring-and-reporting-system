package com.inventory.utility;

import com.inventory.model.Product;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CSVHelperTest {

    private final String REPORT_DIR = "reports/";

    @BeforeEach
    void cleanReports() throws IOException {
        // Ensure directory is clean before each test
        Path reportDirPath = Paths.get(REPORT_DIR);
        if (Files.exists(reportDirPath)) {
            try (var files = Files.list(reportDirPath)) {
                files.forEach(path -> path.toFile().delete());
            }
        } else {
            Files.createDirectory(reportDirPath);
        }
    }

    @Test
    void testWriteProductsCSV_ShouldCreateReportFile() {
        List<Product> products = List.of(
                new Product(1, "Laptop", "Electronics", 10, 75000.0, 5),
                new Product(2, "Mouse", "Accessories", 25, 500.0, 10)
        );

        String generatedBy = "Divyansh";
        String filePath = CSVHelper.writeProductsCSV(products, generatedBy);

        assertNotNull(filePath, "File path should not be null");
        assertTrue(Files.exists(Paths.get(filePath)), "Report file should be created");
    }

    @Test
    void testWriteProductsCSV_ShouldContainProductData() throws IOException {
        List<Product> products = List.of(
                new Product(1, "Keyboard", "Accessories", 15, 1200.0, 5)
        );

        String filePath = CSVHelper.writeProductsCSV(products, "Tester");

        assertNotNull(filePath);
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        assertTrue(lines.stream().anyMatch(line -> line.contains("Keyboard")), "File should contain product name");
        assertTrue(lines.stream().anyMatch(line -> line.contains("Accessories")), "File should contain category");
        assertTrue(lines.stream().anyMatch(line -> line.contains("1200.0")), "File should contain price");
    }

    @Test
    void testWriteProductsCSV_ShouldHandleEmptyList() throws IOException {
        String filePath = CSVHelper.writeProductsCSV(Collections.emptyList(), "Tester");

        assertNotNull(filePath);
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        assertTrue(lines.stream().anyMatch(line -> line.contains("⚠️ No Products Available")),
                "File should show no products message");
    }

    @Test
    void testWriteProductsCSV_ShouldHandleNullList() throws IOException {
        String filePath = CSVHelper.writeProductsCSV(null, "Tester");

        assertNotNull(filePath);
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        assertTrue(lines.stream().anyMatch(line -> line.contains("⚠️ No Products Available")),
                "File should handle null product list safely");
    }

    @AfterEach
    void cleanupReports() throws IOException {
        // Clean up generated report files after each test
        try (var files = Files.list(Paths.get(REPORT_DIR))) {
            files.forEach(path -> path.toFile().delete());
        }
    }
}

