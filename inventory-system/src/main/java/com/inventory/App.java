package com.inventory;

import com.inventory.model.Product;
import com.inventory.model.User;
import com.inventory.services.*;
import com.inventory.utility.CSVHelper;
import com.inventory.exceptions.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static UserService userService;
    private static final InventoryManager manager = new InventoryManager();

    // ===== ANSI Colors =====
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public static void main(String[] args) throws SQLException {
        // StockAlertService alertService = new StockAlertService();
        // ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // scheduler.scheduleAtFixedRate(alertService::checkStockAlert, 0, 5, TimeUnit.MINUTES);
        // System.out.println("Stock alert scheduler started...");

        userService = new UserService(); // initialize here to handle SQLException

        printHeader();
        User currentUser = handleLoginOrRegister();

        if (currentUser == null) {
            printError("Exiting... could not login or register.");
            return;
        }

        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            handleAdminMenu(currentUser);
        } else {
            handleUserMenu(currentUser);
        }
    }

    // =========================================================
    // HEADER & MENUS
    // =========================================================
    private static void printHeader() {
        System.out.println(BLUE + "╔═══════════════════════════════════════════════╗" + RESET);
        System.out.println(
                BLUE + "║" + CYAN + BOLD + "    WELCOME TO INVENTORY MANAGEMENT SYSTEM     " + BLUE + "║" + RESET);
        System.out.println(BLUE + "╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void printLoginMenu() {
        System.out.println(PURPLE + "╔════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║" + YELLOW + "     LOGIN / REGISTER MENU      " + PURPLE + "║" + RESET);
        System.out.println(PURPLE + "╚════════════════════════════════╝" + RESET);
        System.out.println(CYAN + "1. Login ");
        System.out.println("2. Register ");
        System.out.println("3. Verify Email ");
        System.out.println("4. Exit " + RESET);
        System.out.print(YELLOW + "Enter choice: " + RESET);
    }

    private static void printInventoryMenu(boolean isAdmin) {
        System.out.println(BLUE + "\n╔═══════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║" + CYAN + "          INVENTORY MENU           " + BLUE + "║" + RESET);
        System.out.println(BLUE + "╚═══════════════════════════════════╝" + RESET);

        if (isAdmin) {
            System.out.println(GREEN + "╔════╔══════════════════════════════╗" + RESET);
            System.out.println(GREEN + "║ 1. ║ Add Product                  ║" + RESET);
            System.out.println(GREEN + "║ 2. ║ Remove Product               ║" + RESET);
            System.out.println(GREEN + "║ 3. ║ View All Products            ║" + RESET);
            System.out.println(GREEN + "║ 4. ║ Search Product               ║" + RESET);
            System.out.println(GREEN + "║ 5. ║ Update Quantity              ║" + RESET);
            System.out.println(GREEN + "║ 6. ║ Generate Report              ║" + RESET);
            System.out.println(GREEN + "║ 7. ║ Logout                       ║" + RESET);
            System.out.println(GREEN + "╚════╚══════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "Choose a option (1-7): " + RESET);
        } else {
            System.out.println(GREEN + "╔════╔══════════════════════════════╗" + RESET);
            System.out.println(GREEN + "║ 1. ║ View All Products            ║" + RESET);
            System.out.println(GREEN + "║ 2. ║ Search Product               ║" + RESET);
            System.out.println(GREEN + "║ 3. ║ Generate Report              ║" + RESET);
            System.out.println(GREEN + "║ 4. ║ Logout                       ║" + RESET);
            System.out.println(GREEN + "╚════╚══════════════════════════════╝" + RESET);
            System.out.print(YELLOW + "Choose a option (1-4): " + RESET);
        }
    }

    private static void printSearchMenu() {
        System.out.println(PURPLE + "\n╔═══════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║" + CYAN + "     PRODUCT SEARCH OPTIONS        " + PURPLE + "║" + RESET);
        System.out.println(PURPLE + "╚═══════════════════════════════════╝" + RESET);
        System.out.println(CYAN +"╔════╔══════════════════════════════╗" + RESET);
        System.out.println(CYAN +"║ 1. ║ Search by ID                 ║" + RESET);
        System.out.println(CYAN +"║ 2. ║ Search by Name               ║" + RESET);
        System.out.println(CYAN +"║ 3. ║ Search by Category           ║" + RESET);
        System.out.println(CYAN +"║ 4. ║ Search by Price Range        ║" + RESET);
        System.out.println(CYAN +"╚════╚══════════════════════════════╝" + RESET);
        System.out.print(YELLOW + " Enter your choice (1-4): " + RESET);
    }

    // =========================================================
    // LOGIN / REGISTER / VERIFY EMAIL
    // =========================================================
    private static User handleLoginOrRegister() {
        User currentUser = null;

        while (true) {
            printLoginMenu();
            int choice = readInt();

            switch (choice) {
                case 1 -> currentUser = loginUser();
                case 2 -> registerUser();
                case 3 -> verifyEmailMenu();
                case 4 -> {
                    printInfo("Exited Successfully... Bye!");
                    sc.close();
                    System.exit(0);
                }
                default -> printWarning("Invalid choice! Try again.");
            }

            if (currentUser != null)
                break;
        }

        return currentUser;
    }

    private static User loginUser() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        try {
            User user = userService.login(username, password);
            if (user != null) {
                if (!user.is_verified()) {
                    printWarning("Email not verified! Please verify your email first.");
                    return null;
                }
                printSuccess(BOLD + "Login successful! Welcome, " + user.getUserName());
                System.out.println(GREEN + BOLD + "Role: " + user.getRole().toUpperCase());
                return user;
            } else {
                printError("Invalid username or password!");
            }
        } catch (Exception e) {
            printError("Login failed: " + e.getMessage());
        }
        return null;
    }

    private static void registerUser() {
        System.out.print("Choose a username: ");
        String username = sc.nextLine();
        System.out.print("Choose a password: ");
        String password = sc.nextLine();
        System.out.print("Enter role (admin/user): ");
        String role = sc.nextLine().toLowerCase();
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        try {
            boolean success = userService.register(username, password, role, email, false);
            if (success)
                printSuccess("User registered successfully! Please verify your email before login.");
        } catch (DuplicateUserException e) {
            printError("Registration error " + e.getMessage());
        }
    }

    private static void handleLoginOrRegisterLoop() {
        while (true) {
            User currentUser = handleLoginOrRegister();
            if (currentUser != null) {
                if (currentUser.getRole().equalsIgnoreCase("admin")) {
                    handleAdminMenu(currentUser);
                } else {
                    handleUserMenu(currentUser);
                }
            }
        }
    }

    private static void verifyEmailMenu() {
        System.out.println( PURPLE + "==========Email Verification==========" + RESET);
        System.out.print("Enter your registered email: ");
        String email = sc.nextLine();

        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                printError("Email not found!");
                return;
            }
            if (user.is_verified()) {
                printInfo("Email is already verified!");
                return;
            }

            // Generate OTP
            String otp = OTPService.generateOTP(email);

            // Send OTP to email using real mail service
            EmailService.sendOTP(email, otp);
            printInfo("OTP has been sent to your registered email address.");

            // Ask user to enter the OTP they received
            System.out.print("Enter the OTP ( or type 'exit' to cancel ): ");
            String enteredOTP = sc.nextLine();

            // Validate OTP
            if (enteredOTP.equalsIgnoreCase("exit")) {
                System.out.println("Returning to main menu ...");
                return;
            } else if (userService.verifyEmailWithOTP(email, enteredOTP)) {
                printSuccess("Email verified successfully! You can login now.");
            } else {
                printError("⚠️ Invalid or expired OTP!");
            }
        } catch (Exception e) {
            printError("Error verifying email: " + e.getMessage());
        }
    }

    // =========================================================
    // ADMIN / USER MENU HANDLERS
    // =========================================================
    private static void handleAdminMenu(User currentUser) {
        new Thread(()->{
            StockAlertService alertService=new StockAlertService();
            ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);

            scheduler.scheduleAtFixedRate(alertService::checkStockAlert, 0, 5, TimeUnit.MINUTES);
        }).start();
        boolean running = true;
        while (running) {
            printInventoryMenu(true);
            int option = readInt();
            try {
                switch (option) {
                    case 1 -> addProduct();
                    case 2 -> removeProduct();
                    case 3 -> manager.displayInventory();
                    case 4 -> handleSearchMenu();
                    case 5 -> updateProductQuantity();
                    case 6 -> generateAndSendReport("Admin");
                    case 7 -> {
                        printInfo("Logging out and stopping stock alert monitoring...");
                        running = false;
                        
                    }
                    default -> printWarning("Invalid option! Try again.");
                }
            } catch (Exception e) {
                printError("Unexpected error: " + e.getMessage());
            }
        } 
        handleLoginOrRegisterLoop();
    }

    private static void handleUserMenu(User currentUser) {
        boolean running = true;
        while (running) {
            printInventoryMenu(false);
            int option = readInt();
            try {
                switch (option) {
                    case 1 -> manager.displayInventory();
                    case 2 -> handleSearchMenu();
                    case 3 -> generateAndSendReport("User");
                    case 4 -> running = false;
                    default -> printWarning("Invalid option! Try again.");
                }
            } catch (Exception e) {
                printError("Unexpected error: " + e.getMessage());
            }
        }
        
        handleLoginOrRegisterLoop();
    }


    // =========================================================
    // PRODUCT HELPERS
    // =========================================================
    private static void addProduct() throws DuplicateProductException, InvalidProductDataException {
        System.out.print(CYAN + " Enter product ID: " + RESET);
        int id = readInt();
        System.out.print(CYAN + " Enter name: " + RESET);
        String name = sc.nextLine();
        System.out.print(CYAN + " Enter category: " + RESET);
        String category = sc.nextLine();
        System.out.print(CYAN + " Enter quantity: " + RESET);
        int quantity = readInt();
        System.out.print(CYAN + " Enter price: " + RESET);
        double price = readDouble();
        System.out.print(CYAN + " Enter threshold: " + RESET);
        int threshold = readInt();

        manager.addProduct(new Product(id, name, category, quantity, price, threshold));
        printSuccess(BOLD + "Product added successfully!");
    }

    private static void removeProduct() throws ProductNotFoundException {
        System.out.print(CYAN + "Enter product ID to remove: " + RESET);
        int id = readInt();
        manager.removeProduct(id);
        printSuccess(BOLD + "Product removed successfully!");
    }

    private static void updateProductQuantity() throws ProductNotFoundException {
        System.out.print(CYAN + "Enter product ID: " + RESET);
        int id = readInt();
        System.out.print(CYAN + "Enter new quantity: " + RESET);
        int newQty = readInt();
        manager.updateProductQty(id, newQty);
        printSuccess(BOLD + "Product quantity updated successfully for product ID "+id);
    }

    private static void generateAndSendReport(String role) throws Exception {
        printInfo("Generating inventory report...");
        List<Product> products = manager.getAllProducts();
        String filePath = CSVHelper.writeProductsCSV(products, role);
        EmailService.sendReport(System.getenv("MY_EMAIL"), "Inventory Report - " + role,
                "Attached is your latest inventory report.", filePath);
        printSuccess("Report sent successfully!");
    }

    // =========================================================
    // SEARCH HELPERS
    // =========================================================
    private static void handleSearchMenu() throws SQLException {
        printSearchMenu();
        int choice = readInt();
        sc.nextLine();

        switch (choice) {
            case 1 -> searchById();
            case 2 -> searchByName();
            case 3 -> searchByCategory();
            case 4 -> searchByPriceRange();
            default -> printWarning("Invalid search option!");
        }
    }

    private static void searchById() throws SQLException {
        System.out.println(PURPLE + "=========== SEARCH BY ID ==========" + RESET);
        System.out.print(CYAN + "Enter product ID: ");
        int id = readInt();
        try {
            manager.searchProductById(id);
        } catch (ProductNotFoundException e) {
            printError(e.getMessage());
        }
    }

    private static void searchByName() throws SQLException {
        System.out.println(PURPLE + "=========== SEARCH BY NAME ==========" + RESET);
        System.out.print(CYAN + "Enter product name: " + RESET);
        String name = sc.nextLine();
        try {
            manager.searchProduct(name);
        } catch (ProductNotFoundException e) {
            printError(e.getMessage());
        }
    }

    private static void searchByCategory() throws SQLException {
        System.out.println(PURPLE + "=========== SEARCH BY CATEGORY ==========" + RESET);
        System.out.print(CYAN + "Enter category: " + RESET);
        String category = sc.nextLine();
        try {
            manager.searchProductByCategory(category);
        } catch (ProductNotFoundException e) {
            printError(e.getMessage());
        }
    }

    private static void searchByPriceRange() throws SQLException {
        System.out.println(PURPLE + "=========== SEARCH BY PRICE RANGE ==========" + RESET);
        System.out.print(CYAN + "Enter min price: " + RESET);
        double min = readDouble();
        System.out.print(CYAN + "Enter max price: " + RESET);
        double max = readDouble();
        try {
            manager.getProductsByPriceRange(min, max);
        } catch (ProductNotFoundException e) {
            printError(e.getMessage());
        }
    }

    // =========================================================
    // INPUT HELPERS
    // =========================================================
    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                printWarning("Please enter a valid number!");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                printWarning("Please enter a valid decimal value!");
            }
        }
    }

    // =========================================================
    // MESSAGE PRINTERS
    // =========================================================
    private static void printError(String message) {
        System.out.println(RED + "ERROR: " + message + RESET);
    }

    private static void printSuccess(String message) {
        System.out.println(GREEN + "SUCCESS: " + message + RESET);
    }

    private static void printWarning(String message) {
        System.out.println(YELLOW + "WARNING: " + message + RESET);
    }

    private static void printInfo(String message) {
        System.out.println(CYAN + "INFO: " + message + RESET);
    }
}
