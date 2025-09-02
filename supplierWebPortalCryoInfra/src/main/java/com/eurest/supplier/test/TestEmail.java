package com.eurest.supplier.test;

import java.util.Properties;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TestEmail {

	public static void main(String[] args) {
        // Mention the Recipient's email address
		//String to = "javila@smartech.com.mx";
        String to = "jsaavedra@smartech.com.mx";
        // Mention the Sender's email address
        //String from = "portal-proveedores@smartechcloud-apps.com";
        String from = "portal-proveedores@saavienergia.com";
        // Mention the SMTP server address. Below Gmail's SMTP server is being used to send email
        //String host = "email-smtp.us-east-1.amazonaws.com";
        String host = "saavienergia-com.mail.protection.outlook.com";
        
        String password = "BCv1XoVLjVFdB7iyKZNl6LzHaNhMZtDG4R3hh3aIQWsU";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");        
        properties.put("mail.smtp.ssl.enable", "false");//AWS "false"
        properties.put("mail.smtp.auth", "false");//AWS "true"
        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties);
        //AWS Session
        /*Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("AKIAU2M5UYMADMMHPI54", password);
            }
        });
        */
        // Used to debug SMTP issues
        session.setDebug(true);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("This is the Subject Line!");
            // Now set the actual message
            message.setText("This is actual message from AWS TEST");
            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
