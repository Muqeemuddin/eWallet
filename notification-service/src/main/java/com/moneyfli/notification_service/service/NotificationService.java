package com.moneyfli.notification_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private SimpleMailMessage simpleMailMessage;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String serviceSenderEmailId;
    
    public void notify(String message) {
        String[] messageParts = message.split(",");
        String transactionId = messageParts[0].split(":")[1].trim();
        String senderEmailId = messageParts[1].split(":")[1].trim();
        String receiverEmailId = messageParts[2].split(":")[1].trim();
        String amount = messageParts[3].split(":")[1].trim();
        String description = messageParts[4].split(":")[1].trim();
        String status = messageParts[5].split(":")[1].trim();
        String senderWalletId = messageParts[6].split(":")[1].trim();
        String receiverWalletId = messageParts[7].split(":")[1].trim();
        String senderMesssage = getSenderMessage(transactionId, amount, description, senderWalletId, status, receiverWalletId);
        String receiverMessage = getReceiverMessage(amount, description, receiverWalletId, status, senderWalletId);

        if(Objects.equals(status, "COMPLETED")) {
            simpleMailMessage.setTo(senderEmailId);
            simpleMailMessage.setSubject("! You have done a transaction");
            simpleMailMessage.setFrom(serviceSenderEmailId);
            simpleMailMessage.setText(senderMesssage);
            javaMailSender.send(simpleMailMessage);

            simpleMailMessage.setTo(receiverEmailId);
            simpleMailMessage.setSubject("! You have received money");
            simpleMailMessage.setFrom(serviceSenderEmailId);
            simpleMailMessage.setText(receiverMessage);
            javaMailSender.send(simpleMailMessage);
        }else{
            simpleMailMessage.setTo(senderEmailId);
            simpleMailMessage.setSubject("! Your transaction has failed");
            simpleMailMessage.setFrom(serviceSenderEmailId);
            simpleMailMessage.setText(senderMesssage);
            javaMailSender.send(simpleMailMessage);
        }
    }

    private String getSenderMessage(String transactionId, String amount, String description, String senderWalletId, String status, String receiverWalletId) {
        if(Objects.equals(status, "COMPLETED")) {
            return "Dear User, \n" +
                    "USD. " + amount + " has been debited from your wallet **" +senderWalletId.substring(6,10)+" to wallet **" + receiverWalletId.substring(6,10) + "\n" +
                    "on " + LocalDate.now() + ".\n"+
                    "Your transaction reference number is " + transactionId + "\n\n" +
                    "Description: " + description + "\n\n" +
                    "If you have not done this transaction, please contact us immediately.\n\n" +
                    "Thank you for using our service.\n\n" +
                    "Regards, \n" +
                    "MoneyFli Team";
        } else {
            return "Dear User, \n" +
                    "Your transaction of USD. " + amount + " has failed.\n" +
                    "on " + LocalDate.now() + ".\n"+
                    "Your transaction reference number is " + transactionId + "\n\n" +
                    "Description: " + description + "\n\n" +
                    "If you have any queries, please contact us immediately.\n\n" +
                    "Thank you for using our service.\n\n" +
                    "Regards, \n" +
                    "MoneyFli Team";

        }
    }

    private String getReceiverMessage(String amount, String description, String receiverWalletId, String status, String senderWalletId) {
        if (Objects.equals(status, "COMPLETED")) {
            return "Dear User, \n" +
                    "USD. " + amount + " has been credited to your wallet **" + receiverWalletId.substring(6, 10) + " from wallet **" + senderWalletId.substring(6, 10) + "\n" +
                    "on " + LocalDate.now() + ".\n" +
                    "Your transaction reference number is \n\n" +
                    "Description: " + description + "\n\n" +
                    "If you have not done this transaction, please contact us immediately.\n\n" +
                    "Thank you for using our service.\n\n" +
                    "Regards, \n" +
                    "MoneyFli Team";
        }
        return "";
    }
}
