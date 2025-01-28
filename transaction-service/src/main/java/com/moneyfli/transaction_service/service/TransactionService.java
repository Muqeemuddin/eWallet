package com.moneyfli.transaction_service.service;

import com.moneyfli.transaction_service.dao.TransactionDao;
import com.moneyfli.transaction_service.dto.CreateTransactionRequest;
import com.moneyfli.transaction_service.kafka.TransactionKafkaProducer;
import com.moneyfli.transaction_service.model.Transaction;
import com.moneyfli.transaction_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionKafkaProducer transactionKafkaProducer;


    private Transaction transaction;

    public void initiateTransaction(String username,  CreateTransactionRequest createTransactionRequest) {

        String transactionId = "TRX" + UUID.randomUUID().toString().substring(0, 6);
        // initiate transaction
         transaction = Transaction.builder()
                .senderWalletId(username)
                .receiverWalletId(createTransactionRequest.getReceiverWalletId())
                .amount(createTransactionRequest.getAmount())
                .description(createTransactionRequest.getDescription())
                .transactionStatus(TransactionStatus.valueOf("PENDING"))
                .transactionId(transactionId)
                .build();

        try{
            // save to database
            transactionDao.save(transaction);
            // send message to kafka
            transactionKafkaProducer.sendTransactionInitiatedMessage(String.format("Transaction ID : %s, SenderWalletID: %s, ReceiverWalletID: %s, Amount: %s",
                    transactionId,username, createTransactionRequest.getReceiverWalletId(), createTransactionRequest.getAmount()));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while initiating transaction");
        }

    }


    public void updateTransaction(String message) {
        try{
            if(message.contains("200")){
                String[] messageParts = message.split(":");
                String transactionId = messageParts[1].substring(0, 10).trim();
                Transaction transaction = transactionDao.findByTransactionId(transactionId);
                transaction.setTransactionStatus(TransactionStatus.valueOf("COMPLETED"));
                transactionDao.save(transaction);

                RestTemplate restTemplate = new RestTemplate();
                // Fetch the email addresses of the sender and receiver leveraging the user-service
                ResponseEntity<String> senderEmail = restTemplate.exchange(
                        "http://localhost:8080//moneyfli/v1/customer/getEmail/" + transaction.getSenderWalletId(),
                        HttpMethod.GET,
                        null,
                        String.class
                );
                ResponseEntity<String> receiverEmail = restTemplate.exchange(
                        "http://localhost:8080//moneyfli/v1/customer/getEmail/" + transaction.getReceiverWalletId(),
                        HttpMethod.GET,
                        null,
                        String.class
                );


                transactionKafkaProducer.sendTransactionCompletedMessage(
                        String.format(
                                "Transaction ID : %s, senderEmail: %s, receiverEmail: %s, Amount: %s," +
                                        " Description: %s, Status: %s, SenderWalletID: %s, ReceiverWalletID: %s",
                        transactionId,senderEmail.getBody(), receiverEmail.getBody(), transaction.getAmount(), transaction.getDescription(),
                                transaction.getTransactionStatus(), transaction.getSenderWalletId(), transaction.getReceiverWalletId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
