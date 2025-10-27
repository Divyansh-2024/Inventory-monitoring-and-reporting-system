package com.inventory.services;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.impl.ProductDAOImpl;
import com.inventory.model.Product;
import java.util.List;
import java.util.ArrayList;

public class StockAlertService {
    private final ProductDAO dao = new ProductDAOImpl();

    public void checkStockAlert() {
        try {
            List<Product> products = dao.getAllProducts();
            List<Product> alertList = new ArrayList<>();

            // Collect all low-stock products
            for (Product p : products) {
                if (p.getQuantity() < p.getThreshold()) {
                    alertList.add(p);
                }
            }

            // Send email only if there are low-stock products
            if (!alertList.isEmpty()) {
                StringBuilder emailBody = new StringBuilder();
                emailBody.append("📦 LOW STOCK ALERT SUMMARY\n\n");
                emailBody.append("The following products are below their threshold levels:\n\n");

                for (Product p : alertList) {
                    int reorderQty = p.getThreshold() - p.getQuantity();
                    if (reorderQty < 0) reorderQty = 0; // safety check

                    emailBody.append("🔹Product: ").append(p.getName()).append("\n")
                             .append(" Current Quantity: ").append(p.getQuantity()).append("\n")
                             .append(" Threshold: ").append(p.getThreshold()).append("\n")
                             .append(" Recommended Reorder Qty: ").append(reorderQty).append("\n\n");
                }

                // Send single summary email
                EmailService.sendAlert(
                    System.getenv("MY_EMAIL"),
                    "📦 Low Stock Alert Summary (" + alertList.size() + " items)",
                    emailBody.toString()
                );

                
            }

        } catch (Exception e) {
            System.out.println("Error Checking Stock Alert: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
