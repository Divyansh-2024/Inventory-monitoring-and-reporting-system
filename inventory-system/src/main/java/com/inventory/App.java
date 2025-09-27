package com.inventory;

import com.inventory.exceptions.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.services.InventoryManager;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==========================");
            System.out.println("----- Inventory Menu -----");
            System.out.println("==========================");
            System.out.println("1. Add Product");
            System.out.println("2. Remove Product");
            System.out.println("3. Update Product Quantity");
            System.out.println("4. Search Product");
            System.out.println("5. Display All Products");
            System.out.println("6. Exit");
            System.out.print("Choose an option (1-6): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter product ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter product name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter product category: ");
                        String category = sc.nextLine();
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        System.out.print("Enter price: ");
                        double price = sc.nextDouble();

                        manager.addProduct(new Product(id, name, category, qty, price));
                        System.out.println("✅ Product added successfully!");
                    } catch (Exception e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter product ID to remove: ");
                    int id = sc.nextInt();
                    try {
                        manager.removeProduct(id);
                        System.out.println("🗑 Product removed successfully!");
                    } catch (ProductNotFoundException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter product ID to update: ");
                    id = sc.nextInt();
                    System.out.print("Enter new quantity: ");
                    int qty = sc.nextInt();
                    try {
                        manager.updateProductQty(id, qty);
                        System.out.println("✅ Quantity updated successfully!");
                    } catch (ProductNotFoundException e) {
                        System.out.println("❌ " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("\n🔍 Search Options:");
                    System.out.println("1. Search by ID");
                    System.out.println("2. Search by Name");
                    System.out.println("3. Search by Category");
                    System.out.print("Choose an option (1-3): ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine(); // consume newline

                    try {
                        switch (searchChoice) {
                            case 1:
                                System.out.print("Enter product ID: ");
                                int searchId = sc.nextInt();
                                Product p1 = manager.searchProductById(searchId);
                                System.out.println("✅ Found: " + p1);
                                break;

                            case 2:
                                System.out.print("Enter product name: ");
                                String searchName = sc.nextLine();
                                Product p2 = manager.searchProduct(searchName);
                                System.out.println("✅ Found: " + p2);
                                break;

                            case 3:
                                System.out.print("Enter product category: ");
                                String searchCategory = sc.nextLine();
                                List<Product> categoryProducts = manager.searchProductByCategory(searchCategory);
                                System.out.println("✅ Products in category '" + searchCategory + "':");
                                for (Product p : categoryProducts) {
                                    System.out.println(p);
                                }
                                break;

                            default:
                                System.out.println("❌ Invalid search option!");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    manager.displayInventory();
                    break;

                case 6:
                    System.out.println("Exited Successfully...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
