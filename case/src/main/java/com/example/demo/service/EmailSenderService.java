package com.example.demo.service;

public class EmailSenderService {

    public static void send(String recipientEmail, String subject, String body) {
        System.out.printf("\nSimulating sending email to [%s]:\n%n", recipientEmail);

        System.out.printf(" Subject: %s \n Body: %s %n\n", subject, body);
    }

}
