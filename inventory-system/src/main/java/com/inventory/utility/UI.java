package com.inventory.utility;

import com.inventory.model.Product;
import java.util.List;

public class UI {

    // ===== ANSI Colors =====
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    // ===== HEADER =====
    public static void printHeader() {
        System.out.println(BLUE + "╔═══════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║" + CYAN + BOLD + "    WELCOME TO INVENTORY MANAGEMENT SYSTEM     " + BLUE + "║" + RESET);
        System.out.println(BLUE + "╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    // ===== LOGIN MENU =====
    public static void printLoginMenu() {
        System.out.println(PURPLE + "╔════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║" + YELLOW + "     LOGIN / REGISTER MENU      " + PURPLE + "║" + RESET);
        System.out.println(PURPLE + "╚════════════════════════════════╝" + RESET);
        System.out.println(CYAN + "1. Login ");
        System.out.println("2. Register ");
        System.out.println("3. Exit " + RESET);
        System.out.print(YELLOW + "Enter choice: " + RESET);
    }

    // ===== INVENTORY MENU =====
    public static void printInventoryMenu(boolean isAdmin) {
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
        System.out.print( YELLOW + "Choose a option (1-7): ");
    } else {
        System.out.println(GREEN + "╔════╔══════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║ 1. ║ View All Products            ║" + RESET);
        System.out.println(GREEN + "║ 2. ║ Search Product               ║" + RESET);
        System.out.println(GREEN + "║ 3. ║ Generate Report              ║" + RESET);
        System.out.println(GREEN + "║ 4. ║ Logout                       ║" + RESET);
        System.out.println(GREEN + "╚════╚══════════════════════════════╝" + RESET);
        System.out.print( YELLOW + "Choose a option (1-4): ");
    }
    
}


    // ===== SEARCH MENU =====
    public static void printSearchMenu() {
        System.out.println(PURPLE + "\n╔═════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║" + CYAN + "  PRODUCT SEARCH OPTIONS     " + PURPLE + "║" + RESET);
        System.out.println(PURPLE + "╚═════════════════════════════╝" + RESET);
        System.out.println(CYAN + "1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Category");
        System.out.println("4. Search by Price Range" + RESET);
        System.out.print(YELLOW + " Enter your choice: " + RESET);
    }

    // ===== PRODUCT TABLE =====
    public static void printProductTable(List<Product> products) {
        if (products == null || products.isEmpty()) {
            System.out.println(RED + "⚠️ No products found!" + RESET);
            return;
        }

        System.out.println(BLUE + "\n╔══════════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║" + CYAN + "                     INVENTORY PRODUCTS LIST                        " + BLUE + "║" + RESET);
        System.out.println(BLUE + "╚══════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.printf(BOLD + WHITE + "%-6s %-18s %-18s %-10s %-10s\n" + RESET,
                "ID", "NAME", "CATEGORY", "QTY", "PRICE");
        System.out.println(BLUE + "────────────────────────────────────────────────────────────────────────" + RESET);

        int row = 0;
        for (Product p : products) {
            String color = (row % 2 == 0) ? CYAN : GREEN;
            System.out.printf(color + "%-6d %-18s %-18s %-10d ₹%-10.2f" + RESET + "\n",
                    p.getId(), p.getName(), p.getCategory(), p.getQuantity(), p.getPrice());
            row++;
        }
        System.out.println(BLUE + "────────────────────────────────────────────────────────────────────────" + RESET);
    }

   

    // ===== MESSAGE PRINTERS =====
    public static void printError(String message) {
        System.out.println(RED + "ERROR: " + message + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + "Congratulations " + message + RESET);
    }

    public static void printWarning(String message) {
        System.out.println(YELLOW + "⚠️ WARNING: " + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(CYAN + " INFO: " + message);
    }
}
