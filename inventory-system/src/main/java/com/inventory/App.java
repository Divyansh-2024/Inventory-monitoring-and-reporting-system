package com.inventory;
import com.inventory.model.Product;
import com.inventory.services.InventoryManager;  
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
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter product ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();
                    System.out.print("Enter price: ");
                    double price = sc.nextDouble();
                    manager.addProduct(new Product(id, name, qty, price));
                    break;

                case 2:
                    System.out.print("Enter product ID to remove: ");
                    id = sc.nextInt();
                    manager.removeProduct(id);
                    break;

                case 3:
                    System.out.print("Enter product ID to update: ");
                    id = sc.nextInt();
                    System.out.print("Enter new quantity: ");
                    qty = sc.nextInt();
                    manager.updateProductQty(id, qty);
                    break;

                case 4:
                    sc.nextLine();
                    System.out.print("Enter product name to search: ");
                    name = sc.nextLine();
                    manager.searchProduct(name);
                    break;

                case 5:
                    manager.displayInventory();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }
}
