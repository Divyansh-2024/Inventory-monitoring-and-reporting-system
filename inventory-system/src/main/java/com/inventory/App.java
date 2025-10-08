package com.inventory;

import com.inventory.model.User;
import com.inventory.services.UserService;
import com.inventory.exceptions.DuplicateProductException;
import com.inventory.exceptions.InvalidProductDataException;
import com.inventory.exceptions.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.services.InventoryManager;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();
        Scanner sc = new Scanner(System.in);

        // while (true) {
        //     System.out.println("\n================== INVENTORY MENU ================= ");
        //     System.out.printf("| %-3s | %-42s |\n", "1️", "Add Product ");
        //     System.out.printf("| %-3s | %-42s |\n", "2️", "Remove Product ");
        //     System.out.printf("| %-3s | %-42s |\n", "3️", "Update Quantity ");
        //     System.out.printf("| %-3s | %-42s |\n", "4️", "Search Product ");
        //     System.out.printf("| %-3s | %-42s |\n", "5️", "Display All Products ");
        //     System.out.printf("| %-3s | %-42s |\n", "6️", "Exit ");
        //     System.out.println("===================================================");
        //     System.out.print(" Choose an option (1–6): ");

        //     int choice = sc.nextInt();
        //     sc.nextLine();

        //     switch (choice) {
        //         case 1:
        //             try {
        //                 System.out.print("Enter product ID: ");
        //                 int id = sc.nextInt();
        //                 sc.nextLine();
        //                 System.out.print("Enter product name: ");
        //                 String name = sc.nextLine();
        //                 System.out.print("Enter product category: ");
        //                 String category = sc.nextLine();
        //                 System.out.print("Enter quantity: ");
        //                 int qty = sc.nextInt();
        //                 System.out.print("Enter price: ");
        //                 double price = sc.nextDouble();

        //                 manager.addProduct(new Product(id, name, category, qty, price));
        //                 System.out.println("Product added successfully!");
        //             } catch (DuplicateProductException | InvalidProductDataException e) {
        //                 System.out.println(e.getMessage());
        //             }
        //             break;

        //         case 2:
        //             System.out.print("Enter product ID to remove: ");
        //             int id = sc.nextInt();
        //             try {
        //                 manager.removeProduct(id);
        //                 System.out.println(" Product removed successfully!");
        //             } catch (ProductNotFoundException e) {
        //                 System.out.println("" + e.getMessage());
        //             }
        //             break;

        //         case 3:
        //             System.out.print("Enter product ID to update: ");
        //             id = sc.nextInt();
        //             System.out.print("Enter new quantity: ");
        //             int qty = sc.nextInt();
        //             try {
        //                 manager.updateProductQty(id, qty);
        //                 System.out.println(" Quantity updated successfully!");
        //             } catch (ProductNotFoundException e) {
        //                 System.out.println("" + e.getMessage());
        //             }
        //             break;

        //         case 4:
        //             System.out.println("\n============ Search Options ===========");
        //             System.out.println("|         1. Search by ID             |");
        //             System.out.println("|         2. Search by Name           |");
        //             System.out.println("|         3. Search by Category       |");
        //             System.out.println("|         4. Search by Price Range    |");
        //             System.out.println("=======================================");
        //             System.out.print("Choose an option (1-3): ");
        //             int searchChoice = sc.nextInt();
        //             sc.nextLine();

        //             try {
        //                 switch (searchChoice) {
        //                     case 1:
        //                         System.out.print("Enter product ID: ");
        //                         int searchId = sc.nextInt();
        //                         Product p1 = manager.searchProductById(searchId);
        //                         System.out.println("Found: " + p1);
        //                         break;

        //                     case 2:
        //                         System.out.print("Enter product name: ");
        //                         String searchName = sc.nextLine();
        //                         Product p2 = manager.searchProduct(searchName);
        //                         System.out.println("Found: " + p2);
        //                         break;

        //                     case 3:
        //                         System.out.print("Enter product category: ");
        //                         String searchCategory = sc.nextLine();
        //                         List<Product> categoryProducts = manager.searchProductByCategory(searchCategory);
        //                         System.out.println("Products in category '" + searchCategory + "':");
        //                         for (Product p : categoryProducts) {
        //                             System.out.println(p);
        //                         }
        //                         break;

        //                     case 4:
        //                         System.out.print("Enter Minimum Price: ");
        //                         double minPrice = sc.nextDouble();
        //                          System.out.print("Enter Maximum Price: ");
        //                         double maxPrice = sc.nextDouble();
        //                         manager.getProductsByPriceRange(minPrice, maxPrice);
        //                         break;

        //                     default:
        //                         System.out.println("Invalid search option!");
        //                 }
        //             } catch (Exception e) {
        //                 System.out.println(e.getMessage());
        //             }
        //             break;

        //         case 5:
        //             manager.displayInventory();
        //             break;

        //         case 6:
        //             System.out.println("Exited Successfully...");
        //             sc.close();
        //             System.exit(0);

        //         default:
        //             System.out.println("Invalid choice! Try again.");
        //     }
        // }

        UserService userService = new UserService();
// Scanner sc = new Scanner(System.in);

User currentUser = null;

// Loop until login succeeds
while (currentUser == null) {
    System.out.println("=========== LOGIN ===========");
    System.out.print("Username: ");
    String username = sc.nextLine();
    System.out.print("Password: ");
    String password = sc.nextLine();

    currentUser = userService.login(username, password);

    if (currentUser == null) {
        System.out.println("Invalid credentials. Try again.\n");
    }
}

System.out.println("Welcome, " + currentUser.getUserName() + " (" + currentUser.getRole() + ")");

    }
}
