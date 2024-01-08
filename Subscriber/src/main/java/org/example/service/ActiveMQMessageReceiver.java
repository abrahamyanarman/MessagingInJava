package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQMessageReceiver.class);
    private static final String DESTINATION = "notifications.topic";
    private static final String SUBSCRIPTION = "notifications-subscription";

    @JmsListener(destination = DESTINATION, containerFactory = "jmsListenerContainerFactory_NotDurable")
    public void receiveNonDurableMessage(String message) {
        LOGGER.info("Non Durable subscription: Message Received = " + message);
    }

    @JmsListener(destination = DESTINATION, containerFactory = "jmsListenerContainerFactory_Durable", subscription = SUBSCRIPTION)
    public void receiveDurableMessage(String message) {
        LOGGER.info("Durable subscription: Message Received = " + message);
    }
}
