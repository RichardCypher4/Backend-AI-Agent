package com.verifi.controller;

import com.verifi.model.ChatMessage;
import com.verifi.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Send message (user or bot)
    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage message) {
        return chatService.saveMessage(message);
    }

    // Get all messages for a dispute
    @GetMapping("/{disputeId}")
    public List<ChatMessage> getMessages(@PathVariable String disputeId) {
        return chatService.getMessagesByDispute(disputeId);
    }
}

