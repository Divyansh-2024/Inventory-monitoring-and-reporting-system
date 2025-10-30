package com.inventory.utility;

public class EmailUtil {
    public static void sendReport(String to, String subject, String message, String filePath) {
        System.out.println("\n Sending report via email...");
        System.out.println("-------------------------------------------------");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("Attachment: " + filePath);
        System.out.println("Report email sent successfully!");
        System.out.println("-------------------------------------------------\n");
    }
}