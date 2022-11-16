package com.middleware.controller;

import com.middleware.model.SMTPServer;
import com.middleware.services.SMTPServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Controller
public class ApplicationMailer {
    @Autowired
    SMTPServerService smtpServerService;
    public static void sendMail(String recipient, String subject, String content, SMTPServer smtpServer) {
        //SMTPServer smtpServer = smtpServerService.getBySectionID(sid);
        String from = smtpServer.getEmail();///"PLACE YOUR SENDER ADDRESS HERE";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtps.host", smtpServer.getHost());//  "PLACE YOUR SERVER DOMAIN e.g. smtps.example.com");
        properties.setProperty("mail.smtps.port",smtpServer.getPort() );// "465"
        properties.setProperty("mail.smtps.auth",smtpServer.getAuth() ); // "true"
        properties.setProperty("mail.smtps.starttls.enable", smtpServer.getSslenabled()); // "PLACE YOUR SMTP USERNAME"
        properties.setProperty("mail.user", smtpServer.getEmail()); // "PLACE YOUR SMTP USERNAME"
        properties.setProperty("mail.password", smtpServer.getPassword() ); //"PLACE YOUR SMTP PASSWORD"
        properties.setProperty("mail.debug", smtpServer.getDebug() ); //"true"
        // properties.setProperty("mail.debug", smtpServer.getDebug() ); //"true"
        // properties.setProperty("mail.debug", smtpServer.getDebug() ); //"true"
        // Get the default Session object.
        Session maliersession = Session.getDefaultInstance(properties);
        try {
            MimeMessage message = new MimeMessage(maliersession);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(recipient));

            message.setSubject(subject);
            message.setText(content);
            //message.setContent(message, "text/html");

            Transport trnsport;
            trnsport = maliersession.getTransport("smtps");
            trnsport.connect(null, properties.getProperty("mail.password"));
            message.saveChanges();
            trnsport.sendMessage(message, message.getAllRecipients());
            trnsport.close();
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
