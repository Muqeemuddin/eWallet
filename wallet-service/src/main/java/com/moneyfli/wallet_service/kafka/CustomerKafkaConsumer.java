package com.moneyfli.wallet_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerKafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKafkaConsumer.class);

    @KafkaListener(topics = "customer-created", groupId = "walletGroup")
    public void consume(String message) {
        LOGGER.info("Consumed message: " + message);
    }
}
