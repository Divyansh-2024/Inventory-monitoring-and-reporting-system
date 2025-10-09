package com.inventory;

import com.inventory.model.Product;
import com.inventory.model.User;
import com.inventory.services.InventoryManager;
import com.inventory.services.UserService;
import com.inventory.exceptions.*;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        User currentUser = null;
        System.out.println("----------------------------------------------");
        System.out.println("| Welcome To The Inventory Management System |");
        System.out.println("----------------------------------------------");
        System.out.println();
        System.out.println("  Please choose the option below: ");
        System.out.println("----------------------------------");

        // ===== LOGIN / REGISTER MENU =====
        while (true) {
            System.out.println("\n=============================================================");
            System.out.printf("| %-3s | %-51s |\n", "1", "Login User");
            System.out.printf("| %-3s | %-51s |\n", "2", "Register User");
            System.out.printf("| %-3s | %-51s |\n", "3", "Exit Application");
            System.out.println("=============================================================");
            System.out.print("Choose an option (1-3): ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Username: ");
                    String username = sc.nextLine();
                    System.out.print("Password: ");
                    String password = sc.nextLine();
                    try {
                        currentUser = userService.login(username, password);
                        System.out.println(
                                "Welcome, " + currentUser.getUserName() + " (" + currentUser.getRole() + ")");
                        break;
                    } catch (InvalidCredentialsException e) {
                        System.out.println(e.getMessage());
                    }
                    continue;

                case 2:
                    System.out.print("Choose a username: ");
                    String newUsername = sc.nextLine();
                    System.out.print("Choose a password: ");
                    String newPassword = sc.nextLine();
                    System.out.print("Enter role (admin/user): ");
                    String role = sc.nextLine().toLowerCase();

                    try {
                        userService.register(newUsername, newPassword, role);
                        System.out.println("User registered successfully! You can login now.");
                    } catch (DuplicateUserException e) {
                        System.out.println(e.getMessage());
                    }
                    continue;

                case 3:
                    System.out.println("Exited Successfully...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("⚠️ Invalid choice! Try again.");
            }

            if (currentUser != null)
                break;
        }

        // ===== INVENTORY MENU =====
        InventoryManager manager = new InventoryManager();

        while (true) {
            System.out.println("\n================== INVENTORY MENU ================= ");
            System.out.printf("| %-3s | %-42s |\n", "1️", "Add Product ");
            System.out.printf("| %-3s | %-42s |\n", "2️", "Delete Product ");

            if (currentUser.getRole().equalsIgnoreCase("admin")) {
                System.out.printf("| %-3s | %-42s |\n", "3️", "View All Products ");
                System.out.printf("| %-3s | %-42s |\n", "4️", "Search Product ");
                System.out.printf("| %-3s | %-42s |\n", "5️", "Update Product Quantity ");
                System.out.printf("| %-3s | %-42s |\n", "6️", "Exit ");
            } else {
                System.out.printf("| %-3s | %-42s |\n", "3️", "Exit ");
            }
            System.out.println("===================================================");
            System.out.print("Choose an option (1-6): ");
            int option = sc.nextInt();
            sc.nextLine();

            try {
                if (currentUser.getRole().equalsIgnoreCase("admin")) {
                    switch (option) {
                        case 1: // Add
                            System.out.print("Enter product ID: ");
                            int addId = sc.nextInt();
                            sc.nextLine();
                            System.out.print("Enter name: ");
                            String addName = sc.nextLine();
                            System.out.print("Enter category: ");
                            String addCat = sc.nextLine();
                            System.out.print("Enter quantity: ");
                            int qty = sc.nextInt();
                            System.out.print("Enter price: ");
                            double price = sc.nextDouble();
                            sc.nextLine();
                            manager.addProduct(new Product(addId, addName, addCat, qty, price));
                            break;

                        case 2: // Remove
                            System.out.print("Enter product ID to remove: ");
                            int removeId = sc.nextInt();
                            sc.nextLine();
                            manager.removeProduct(removeId);
                            break;
                        case 3: // View All
                            manager.displayInventory();
                            break;
                        case 4: // Search
                            System.out.println("\n============ Search Options ===========");
                            System.out.println("| 1. Search By ID                     |");
                            System.out.println("| 2. Search By Name                   |");
                            System.out.println("| 3. Search By Category               |");
                            System.out.println("| 4. Search By Price Range            |");
                            System.out.println("=======================================");
                            System.out.print("Choose search option (1-4): ");
                            int searchChoice = sc.nextInt();
                            sc.nextLine();
                            switch (searchChoice) {
                                case 1 -> {
                                    System.out.print("Enter product ID: ");
                                    int id = sc.nextInt();
                                    sc.nextLine();
                                    manager.searchProductById(id);
                                }
                                case 2 -> {
                                    System.out.print("Enter product name: ");
                                    String name = sc.nextLine();
                                    manager.searchProduct(name);
                                }
                                case 3 -> {
                                    System.out.print("Enter category: ");
                                    String cat = sc.nextLine();
                                    manager.searchProductByCategory(cat);
                                }
                                case 4 -> {
                                    System.out.print("Enter min price: ");
                                    double min = sc.nextDouble();
                                    System.out.print("Enter max price: ");
                                    double max = sc.nextDouble();
                                    sc.nextLine();
                                    manager.getProductsByPriceRange(min, max);
                                }
                                default -> System.out.println("⚠️ Invalid search option!");
                            }
                            break;

                        case 5: // Update Quantity
                            System.out.print("Enter product ID: ");
                            int pid = sc.nextInt();
                            System.out.print("Enter new quantity: ");
                            int newQty = sc.nextInt();
                            sc.nextLine();
                            manager.updateProductQty(pid, newQty);
                            break;

                        case 6: // Exit
                            System.out.println("Logged out successfully!");
                            sc.close();
                            System.exit(0);

                        default:
                            System.out.println("⚠️ Invalid option! Try again.");
                    }
                } else { // Regular user
                    switch (option) {
                        case 1 -> manager.displayInventory();
                        case 2 -> {
                             System.out.println("\n============ Search Options ===========");
                            System.out.println("| 1. Search By ID                     |");
                            System.out.println("| 2. Search By Name                   |");
                            System.out.println("| 3. Search By Category               |");
                            System.out.println("| 4. Search By Price Range            |");
                            System.out.println("=======================================");
                            System.out.print("Choose search option (1-4): ");
                            int searchChoice = sc.nextInt();
                            sc.nextLine();
                            switch (searchChoice) {
                                case 1 -> {
                                    System.out.print("Enter product ID: ");
                                    int id = sc.nextInt();
                                    sc.nextLine();
                                    manager.searchProductById(id);
                                }
                                case 2 -> {
                                    System.out.print("Enter product name: ");
                                    String name = sc.nextLine();
                                    manager.searchProduct(name);
                                }
                                case 3 -> {
                                    System.out.print("Enter category: ");
                                    String cat = sc.nextLine();
                                    manager.searchProductByCategory(cat);
                                }
                                case 4 -> {
                                    System.out.print("Enter min price: ");
                                    double min = sc.nextDouble();
                                    System.out.print("Enter max price: ");
                                    double max = sc.nextDouble();
                                    sc.nextLine();
                                    manager.getProductsByPriceRange(min, max);
                                }
                                default -> System.out.println("⚠️ Invalid search option!");
                            }
                        }
                        case 3 -> {
                            System.out.println("Logged out successfully!");
                            sc.close();
                            System.exit(0);
                        }
                        default -> System.out.println("⚠️ Invalid option! Try again.");
                    }
                }
            } catch (DuplicateProductException | InvalidProductDataException | ProductNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }
}
