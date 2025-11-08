package com.verifi.service;

import com.verifi.model.ChatMessage;
import com.verifi.repository.ChatMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // Save message and send via WebSocket
    public ChatMessage saveMessage(ChatMessage message) {
        ChatMessage saved = chatMessageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/disputes/" + message.getDisputeId(), saved);
        return saved;
    }

    public List<ChatMessage> getMessagesByDispute(String disputeId) {
        return chatMessageRepository.findByDisputeIdOrderByCreatedAtAsc(disputeId);
    }
}
