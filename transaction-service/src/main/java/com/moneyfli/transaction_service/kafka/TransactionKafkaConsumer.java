package com.moneyfli.transaction_service.kafka;

import com.moneyfli.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaConsumer {

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = "wallet-transaction-completed", groupId = "transactionGroup")
    public void consume(String message) {
        transactionService.updateTransaction(message);
    }
}
