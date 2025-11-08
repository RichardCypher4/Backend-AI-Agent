package com.verifi.controller;

import com.verifi.dto.WebSocketPayload;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Example: clients can send to /app/echo and server will broadcast to /topic/messages
    @MessageMapping("/echo")
    @SendTo("/topic/messages")
    public String echo(String message) {
        return message;
    }

    // Programmatic send
    public void sendUpdate(WebSocketPayload payload) {
        messagingTemplate.convertAndSend("/topic/updates", payload);
    }
}
