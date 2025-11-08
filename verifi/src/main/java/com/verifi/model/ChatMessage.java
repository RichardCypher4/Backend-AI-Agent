package com.verifi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;
    private String disputeId;       // Link to dispute
    private String sender;          // "user" or "bot"
    private String message;
    private LocalDateTime createdAt;

    // Default constructor
    public ChatMessage() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with all fields except ID
    public ChatMessage(String disputeId, String sender, String message) {
        this.disputeId = disputeId;
        this.sender = sender;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // Full constructor including ID
    public ChatMessage(String id, String disputeId, String sender, String message, LocalDateTime createdAt) {
        this.id = id;
        this.disputeId = disputeId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        this.disputeId = disputeId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", disputeId='" + disputeId + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
