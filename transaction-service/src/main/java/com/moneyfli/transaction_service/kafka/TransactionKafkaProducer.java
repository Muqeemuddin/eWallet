package com.moneyfli.transaction_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransactionInitiatedMessage(String message) {
        kafkaTemplate.send("transaction-initiated",message);
    }

    public void sendTransactionCompletedMessage(String message) {
        kafkaTemplate.send("transaction-completed", message);
    }
}
