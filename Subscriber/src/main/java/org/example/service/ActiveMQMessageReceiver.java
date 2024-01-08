package org.example.service;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQMessageReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveMQMessageReceiver.class);
    private static final String DESTINATION = "notifications.topic";
    private static final String TMP_DESTINATION = "tmp.notifications.topic";
    private static final String SUBSCRIPTION = "notifications-subscription";

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = DESTINATION, containerFactory = "jmsListenerContainerFactory_NotDurable")
    public void receiveNonDurableMessage(String message) {
        LOGGER.info("Non Durable subscription: Message Received = " + message);
    }

    @JmsListener(destination = DESTINATION, containerFactory = "jmsListenerContainerFactory_Durable", subscription = SUBSCRIPTION)
    public void receiveDurableMessage(String message) {
        LOGGER.info("Durable subscription: Message Received = " + message);
    }

    @JmsListener(destination = TMP_DESTINATION, containerFactory = "jmsListenerContainerFactory_NotDurable")
    public void receiveWithReplyToAndResponse(TextMessage textMessage) throws JMSException {
        String response = textMessage.getText() + ", and correlation ID is " + textMessage.getJMSCorrelationID();

        jmsTemplate.send(textMessage.getJMSReplyTo(), session -> {
            LOGGER.info("Sending message: " + response + "to destination: " + textMessage.getJMSReplyTo());
            TextMessage responseMessage = session.createTextMessage(response);
            responseMessage.setJMSDestination(textMessage.getJMSReplyTo());
            responseMessage.setJMSCorrelationID(textMessage.getJMSCorrelationID());
            return responseMessage;
        });
    }
}
