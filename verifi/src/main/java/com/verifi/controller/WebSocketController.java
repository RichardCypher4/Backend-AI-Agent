package com.verifi.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/notify")
    @SendTo("/topic/updates")
    public String notifyAll(String message) {
        return message;
    }
}
