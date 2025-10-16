package com.inventory;

import com.inventory.services.EmailService;
import com.inventory.model.Product;
import com.inventory.model.User;
import com.inventory.services.InventoryManager;
import com.inventory.services.UserService;
import com.inventory.utility.CSVHelper;
import com.inventory.utility.UI;
import com.inventory.exceptions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        User currentUser = null;

        UI.printHeader();

        // ===== LOGIN / REGISTER MENU =====
        // ===== LOGIN / REGISTER MENU =====
while (true) {
    UI.printLoginMenu();
    int choice = sc.nextInt();
    sc.nextLine();

    switch (choice) {
        case 1: // Login
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            try {
                currentUser = userService.login(username, password);

                // Only print success if login actually returns a user
                if (currentUser != null) {
                    UI.printSuccess("Login successful! Welcome, " + currentUser.getUserName() + "(" + currentUser.getRole() + ")" );
                    break; // exit login loop
                } else {
                    UI.printError("Invalid username or password!");
                }

            } catch (Exception e) {
                // Catch any unexpected exception
                UI.printError("Login failed: " + e.getMessage());
            }
            continue;

        case 2: // Register
            System.out.print("Choose a username: ");
            String newUsername = sc.nextLine();
            System.out.print("Choose a password: ");
            String newPassword = sc.nextLine();
            System.out.print("Enter role (admin/user): ");
            String role = sc.nextLine().toLowerCase();
            try {
                userService.register(newUsername, newPassword, role);
                UI.printSuccess("User registered successfully! You can login now.");
            } catch (DuplicateUserException e) {
                UI.printError(e.getMessage());
            }
            continue;

        case 3: // Exit
            UI.printInfo("Exited Successfully... Bye! 👋");
            sc.close();
            System.exit(0);

        default:
            UI.printWarning("Invalid choice! Try again.");
    }

    if (currentUser != null) break; // exit login loop after successful login
}


        // ===== INVENTORY MENU =====
        InventoryManager manager = new InventoryManager();

        while (true) {
            UI.printInventoryMenu(currentUser.getRole().equalsIgnoreCase("admin"));
            int option = sc.nextInt();
            sc.nextLine();

            try {
                if (currentUser.getRole().equalsIgnoreCase("admin")) {
                    switch (option) {
                        case 1 -> {
                            System.out.print(UI.CYAN + " Enter product ID: " + UI.RESET);
                            int addId = sc.nextInt();
                            sc.nextLine();
                            System.out.print(UI.CYAN + " Enter name: " + UI.RESET);
                            String addName = sc.nextLine();
                            System.out.print(UI.CYAN + " Enter category: " + UI.RESET);
                            String addCat = sc.nextLine();
                            System.out.print(UI.CYAN + " Enter quantity: " + UI.RESET);
                            int qty = sc.nextInt();
                            System.out.print(UI.CYAN + " Enter price: " + UI.RESET);
                            double price = sc.nextDouble();
                            sc.nextLine();
                            manager.addProduct(new Product(addId, addName, addCat, qty, price));
                            UI.printSuccess("Product added successfully!");
                        }
                        case 2 -> {
                            System.out.print(UI.CYAN + " Enter product ID to remove: " + UI.RESET);
                            int removeId = sc.nextInt();
                            sc.nextLine();
                            manager.removeProduct(removeId);
                            UI.printSuccess("Product removed successfully!");
                        }
                        case 3 -> manager.displayInventory();
                        case 4 -> {
                            UI.printSearchMenu();
                            int searchChoice = sc.nextInt();
                            sc.nextLine();
                            handleSearch(manager, searchChoice, sc);
                        }
                        case 5 -> {
                            System.out.print(UI.CYAN + " Enter product ID: " + UI.RESET);
                            int pid = sc.nextInt();
                            System.out.print(UI.CYAN + " Enter new quantity: " + UI.RESET);
                            int newQty = sc.nextInt();
                            sc.nextLine();
                            manager.updateProductQty(pid, newQty);
                            UI.printSuccess("Product quantity updated successfully!");
                        }
                        case 6 -> {
                            UI.printInfo("Generating inventory report...");
                            List<Product> products = manager.getAllProducts();
                            String filePath = CSVHelper.writeProductsCSV(products, "Admin");
                            EmailService.sendReport(
                                    System.getenv("MY_EMAIL"),
                                    "Daily Inventory Report",
                                    "Attached is your latest inventory report.",
                                    filePath);
                            UI.printSuccess("Report sent successfully! ");
                        }
                        case 7 -> {
                            UI.printInfo("Logged out successfully! 👋");
                            sc.close();
                            System.exit(0);
                        }
                        default -> UI.printWarning("Invalid option! Try again.");
                    }
                } else {
                    switch (option) {
                        case 1 -> manager.displayInventory();
                        case 2 -> {
                            UI.printSearchMenu();
                            int searchChoice = sc.nextInt();
                            sc.nextLine();
                            handleSearch(manager, searchChoice, sc);
                        }
                        case 3 -> {
                            UI.printInfo("Generating inventory report...");
                            List<Product> products = manager.getAllProducts();
                            String filePath = CSVHelper.writeProductsCSV(products, "User");
                            EmailService.sendReport(
                                    System.getenv("MY_EMAIL"),
                                    "Daily Inventory Report",
                                    "Attached is your latest inventory report.",
                                    filePath);
                            UI.printSuccess("Report sent successfully! ");
                        }
                        case 4 -> {
                            UI.printInfo("Logged out successfully!");
                            sc.close();
                            System.exit(0);
                        }
                        default -> UI.printWarning("Invalid option! Try again.");
                    }
                }
            } catch (DuplicateProductException | InvalidProductDataException | ProductNotFoundException e) {
                UI.printError(e.getMessage());
            } catch (Exception e) {
                UI.printError("Unexpected error: " + e.getMessage());
            }
        }
    }

    // ===== SEARCH HELPER =====
    private static void handleSearch(InventoryManager manager, int searchChoice, Scanner sc) throws SQLException {
        switch (searchChoice) {
            case 1 -> {
                System.out.print(UI.CYAN + " Enter product ID: " + UI.RESET);
                int id = sc.nextInt();
                sc.nextLine();
                try {
                    manager.searchProductById(id);
                } catch (ProductNotFoundException e) {
                    UI.printError(e.getMessage());
                }
            }
            case 2 -> {
                System.out.print(UI.CYAN + " Enter product name: " + UI.RESET);
                String name = sc.nextLine();
                try {
                    manager.searchProduct(name);
                } catch (ProductNotFoundException e) {
                    UI.printError(e.getMessage());
                }
            }
            case 3 -> {
                System.out.print(UI.CYAN + " Enter category: " + UI.RESET);
                String cat = sc.nextLine();
                try {
                    manager.searchProductByCategory(cat);
                } catch (ProductNotFoundException e) {
                    UI.printError(e.getMessage());
                }
            }
            case 4 -> {
                System.out.print(UI.CYAN + " Enter min price: " + UI.RESET);
                double min = sc.nextDouble();
                System.out.print(UI.CYAN + " Enter max price: " + UI.RESET);
                double max = sc.nextDouble();
                sc.nextLine();
                try {
                    manager.getProductsByPriceRange(min, max);
                } catch (ProductNotFoundException e) {
                    UI.printError(e.getMessage());
                }
            }
            default -> UI.printWarning("Invalid search option!");
        }
    }
}
