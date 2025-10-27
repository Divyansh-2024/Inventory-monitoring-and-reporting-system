package com.inventory.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailService {
   

    public static void sendReport(String toEmail, String subject, String body, String attachmentPath) {

        final String fromEmail = System.getenv("MY_EMAIL"); // email address
        final String password = System.getenv("APP_PASSWORD"); // App password

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        // Gmail SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // session creation
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Compose message
             Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Body part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(attachmentPath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // Sending mail
            Transport.send(message);

            System.out.println("Report sent successfully to " + toEmail);

        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }

    }

    public static void sendAlert(String toEmail, String sub, String msg) {

        final String fromEmail = System.getenv("MY_EMAIL"); // email address
        final String password = System.getenv("APP_PASSWORD"); // App password

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        // Gmail SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // session creation
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(sub);
            message.setText(msg);
            Transport.send(message);

        

        } catch (Exception e) {
            System.out.println("Error sending alert: " + e.getMessage());
        }

    }

     // Send OTP to the user's email
    public static void sendOTP(String toEmail, String otp) {

        final String fromEmail = System.getenv("MY_EMAIL");  // your email address
        final String password = System.getenv("APP_PASSWORD"); // app password or SMTP password

        if (fromEmail == null || password == null) {
            throw new RuntimeException("Email credentials not set in environment variables!");
        }

        // Gmail SMTP configuration
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Compose message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Inventory System - Email Verification");
            message.setText("Your OTP for email verification is: " + otp + 
                            "\n\nPlease verify your account before login.");

            // Send message
            Transport.send(message);
        } catch (MessagingException e) {
            System.out.println("ERROR: Failed to send OTP - " + e.getMessage());
        }
    }


}