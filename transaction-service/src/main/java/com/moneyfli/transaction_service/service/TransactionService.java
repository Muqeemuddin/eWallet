package com.moneyfli.transaction_service.service;

import com.moneyfli.transaction_service.dao.TransactionDao;
import com.moneyfli.transaction_service.dto.CreateTransactionRequest;
import com.moneyfli.transaction_service.kafka.TransactionKafkaProducer;
import com.moneyfli.transaction_service.model.Transaction;
import com.moneyfli.transaction_service.model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionKafkaProducer transactionKafkaProducer;

    public void initiateTransaction(String username,  CreateTransactionRequest createTransactionRequest) {

        String transactionId = "TRX" + UUID.randomUUID().toString().substring(0, 6);
        // initiate transaction
        Transaction transaction = Transaction.builder()
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
            transactionKafkaProducer.sendTransactionMessage(String.format("Transaction ID : %s, SenderWalletID: %s, ReceiverWalletID: %s, Amount: %s",
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
