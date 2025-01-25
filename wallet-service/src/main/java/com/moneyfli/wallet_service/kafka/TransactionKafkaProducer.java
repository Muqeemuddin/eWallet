package com.moneyfli.wallet_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransactionCompleteMessage(String message) {
        kafkaTemplate.send("transaction-completed", message);
    }
}
