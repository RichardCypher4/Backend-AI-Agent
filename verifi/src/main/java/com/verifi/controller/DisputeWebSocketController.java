package com.verifi.controller;

import com.verifi.model.Dispute;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ws")
public class DisputeWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public DisputeWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint to test WebSocket
    @PostMapping("/send")
    public String sendDisputeUpdate(@RequestBody Dispute dispute) {
        messagingTemplate.convertAndSend("/topic/disputes", dispute);
        return "Sent!";
    }
}
