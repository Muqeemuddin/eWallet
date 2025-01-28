package com.moneyfli.wallet_service.kafka;

import com.moneyfli.wallet_service.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerKafkaConsumer {

    @Autowired
    private WalletService walletService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerKafkaConsumer.class);

    @KafkaListener(topics = "customer-created", groupId = "customerWalletGroup")
    public void consume(String message) {
        LOGGER.info("Consumed message: {}", message);
        walletService.createWallet(message);

    }

    @KafkaListener(topics = "transaction-initiated", groupId = "transactionWalletGroup")
    public void consumeTransaction(String message) {
        //LOGGER.info("Consumed message: {}", message);
        walletService.updateWallet(message);
    }
}
