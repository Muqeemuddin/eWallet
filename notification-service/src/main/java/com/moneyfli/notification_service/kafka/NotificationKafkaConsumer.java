package com.moneyfli.notification_service.kafka;

import com.moneyfli.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationKafkaConsumer {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "transaction-completed", groupId = "notificationGroup")
    public void consume(String message) {
        notificationService.notify(message);
    }
}
