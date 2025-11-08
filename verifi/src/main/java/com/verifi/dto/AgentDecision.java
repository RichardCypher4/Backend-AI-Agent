package com.verifi.dto;

public class AgentDecision {
    private String decision;    // e.g., "AUTO_RESOLVE", "REQUIRE_MANUAL_REVIEW"
    private String reason;      // short human-readable reason
    private double confidence;  // 0.0 - 1.0

    public AgentDecision() {}

    public AgentDecision(String decision, String reason, double confidence) {
        this.decision = decision;
        this.reason = reason;
        this.confidence = confidence;
    }

    // getters & setters
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
}
