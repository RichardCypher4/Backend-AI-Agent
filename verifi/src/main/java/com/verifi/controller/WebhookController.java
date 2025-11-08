package com.verifi.controller;

import com.verifi.model.Dispute;
import com.verifi.service.DisputeService;
import com.verifi.service.AIAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@CrossOrigin(origins = "*")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    private final DisputeService disputeService;
    private final AIAgentService aiAgentService;

    public WebhookController(DisputeService disputeService, AIAgentService aiAgentService) {
        this.disputeService = disputeService;
        this.aiAgentService = aiAgentService;
    }

    @PostMapping("/whatsapp")
    public ResponseEntity<?> handleWhatsAppWebhook(@RequestBody Map<String, Object> data) {
        try {
            String sender = (String) data.get("sender");
            String message = (String) data.get("message");
            Double amount = data.get("amount") != null ? Double.valueOf(data.get("amount").toString()) : 0.0;

            if (sender == null || message == null) {
                return ResponseEntity.badRequest().body("Missing sender or message field");
            }

            Dispute dispute = new Dispute();
            dispute.setTransactionId("AUTO-" + System.currentTimeMillis());
            dispute.setReason("Dispute from WhatsApp user " + sender + ": " + message);
            dispute.setStatus("PENDING");
            dispute.setAmount(amount);
            dispute.setCreatedAt(LocalDateTime.now());
            dispute.setUpdatedAt(LocalDateTime.now());

            Dispute savedDispute = disputeService.createDispute(dispute);

            // 🔥 Trigger AI analysis + WebSocket notification
            aiAgentService.analyzeAndProcessDispute(savedDispute);

            log.info("✅ Dispute created and analyzed for WhatsApp message from {}", sender);

            return ResponseEntity.ok(savedDispute);

        } catch (Exception e) {
            log.error("❌ Error processing WhatsApp webhook: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error creating dispute");
        }
    }
}
