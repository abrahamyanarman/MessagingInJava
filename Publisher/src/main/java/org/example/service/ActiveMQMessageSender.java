package org.example.service;

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

}
