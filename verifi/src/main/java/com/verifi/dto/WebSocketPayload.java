package com.verifi.dto;

import java.time.LocalDateTime;

public class WebSocketPayload {
    private String type; // e.g., "DISPUTE_CREATED", "AGENT_DECISION"
    private String disputeId;
    private Object data;
    private LocalDateTime timestamp;

    public WebSocketPayload() {}

    public WebSocketPayload(String type, String disputeId, Object data) {
        this.type = type;
        this.disputeId = disputeId;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // getters & setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDisputeId() { return disputeId; }
    public void setDisputeId(String disputeId) { this.disputeId = disputeId; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
