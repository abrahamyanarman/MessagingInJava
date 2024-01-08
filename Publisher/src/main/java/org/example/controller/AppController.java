package org.example.controller;

import org.example.service.ActiveMQMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    private static final String DESTINATION = "notifications.topic";

    @Autowired
    private ActiveMQMessageSender activeMQMessageSender;

    @PostMapping("/sendMessage/{message}")
    public ResponseEntity<String> sendMessage(@PathVariable String message) {
        LOGGER.info("Sending message: " + message);
        try {
            activeMQMessageSender.sendMessage(DESTINATION, message);
            LOGGER.info("Message: " + message + "was sent to destination: " + DESTINATION);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Message: " + message + "was sent to destination: " + DESTINATION);
        } catch (Exception e) {
            LOGGER.error("Exception occurred during processing message: " + message + " to be sent to destination: " + DESTINATION);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("ERROR-1001: Error during processing message: " + message + " to be sent to destination: " + DESTINATION);
        }
    }
}
