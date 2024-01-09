package org.example.service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQMessageSender {
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }

    public String sendMessageWithReplyTo(String destination, String message) {
        String correlationId = java.util.UUID.randomUUID().toString();

        jmsTemplate.send(destination, session -> {
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setJMSCorrelationID(correlationId);
            textMessage.setJMSReplyTo(session.createTopic("TMP"));
            return textMessage;
        });

        TextMessage response = (TextMessage) jmsTemplate.receiveSelected("TMP", "JMSCorrelationID IS NOT NULL");

        String responseText;
        if (response != null) {
            try {
                responseText = response.getText();
            } catch (JMSException e) {
                responseText = StringUtils.EMPTY;
            }
        } else {
            responseText = "No response received";
        }

        return "Received response: " + responseText;
    }

    public void sendMessageToVirtualTopic(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
        jmsTemplate.convertAndSend(destination, message, messageProcessor -> {
            messageProcessor.setStringProperty("_type", String.class.getName());
            messageProcessor.setStringProperty("VirtualTopic", "myVirtualTopic");
            messageProcessor.setStringProperty("Consumer", "Consumer1");
            return messageProcessor;
        });
    }

}
