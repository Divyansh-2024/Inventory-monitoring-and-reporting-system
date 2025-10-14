
package com.inventory.services;

import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailService {
    public static void sendReport(String toEmail, String subject, String body, String attachmentPath) {

        final String fromEmail = System.getenv("MAIL_USER"); // your email

        final String password = System.getenv("MAIL_PASS"); // your app password

        if (fromEmail == null || password == null) {

            throw new RuntimeException("X Email credentials not set in environment variables!");
        }

        // SMTP configuration
    Properties props = new Properties();
    props.put("mail.smtp.host","smtp.gmail.com");
    props.put("mail.smtp.port","587");
    props.put("mail.smtp.auth","true");
    props.put("mail.smtp.starttls.enable","true");

    Session session = Session.getInstance(props, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication (from Email, password); }
        });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toEmail));
        message.setSubject(subject);

        // Email body
        MimeBodyPart textPart = new MimeBodyPart();textPart.setText(body);

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(new File(attachmentPath));
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);

        System.out.println("✓ Report sent successfully to "+toEmail);
    }
    catch(Exception e){
        System.out.println("X Error sending email: " + e.getMessage());
    }
    }

    
}