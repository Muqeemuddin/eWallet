package com.moneyfli.user_service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerKafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendCustomerCreatedMessage(String message) {
        LOGGER.info(String.format("Sending message to kafka topic: customer-created %s", message));
        kafkaTemplate.send("customer-created", message);
    }
}
