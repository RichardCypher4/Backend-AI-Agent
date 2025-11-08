package com.verifi.service;

import com.verifi.model.Dispute;
import com.verifi.repository.DisputeRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AIAgentService {

    private final DisputeRepository disputeRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public AIAgentService(DisputeRepository disputeRepository, SimpMessagingTemplate messagingTemplate) {
        this.disputeRepository = disputeRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void analyzeAndProcessDispute(Dispute dispute) {
        String decision;
        String status;

        // Simple rule-based AI logic (expandable later)
        if (dispute.getAmount() != null && dispute.getAmount() < 1000) {
            decision = "Auto-resolved: transaction amount below 1000.";
            status = "RESOLVED";
        } else if (dispute.getReason() != null && dispute.getReason().toLowerCase().contains("fraud")) {
            decision = "Flagged as potential fraud – escalate to human review.";
            status = "ESCALATED";
        } else {
            decision = "Pending human review.";
            status = "PENDING";
        }

        // Update dispute
        dispute.setResolutionNote(decision);
        dispute.setStatus(status);

        // Save update to MongoDB
        Dispute updated = disputeRepository.save(dispute);

        // Send WebSocket broadcast
        messagingTemplate.convertAndSend("/topic/updates", updated);
    }
}
