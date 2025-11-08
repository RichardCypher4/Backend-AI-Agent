// src/main/java/com/verifi/dto/TransactionDto.java
package com.verifi.dto;

import java.time.LocalDateTime;

public class TransactionDto {
    private String id;
    private String userId;
    private String toAccount;
    private double amount;
    private String status; // PENDING, SUCCESS, FAILED, REVERSED etc.
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors, getters & setters

    public TransactionDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
