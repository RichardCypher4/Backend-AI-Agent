package com.verifi.controller;

import com.verifi.model.Dispute;
import com.verifi.service.DisputeService;
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

    public WebhookController(DisputeService disputeService) {
        this.disputeService = disputeService;
    }

    @PostMapping("/whatsapp")
    public ResponseEntity<?> handleWhatsAppWebhook(@RequestBody Map<String, Object> data) {
        try {
            String sender = (String) data.get("sender");
            String message = (String) data.get("message");

            if (sender == null || message == null) {
                return ResponseEntity.badRequest().body("Missing sender or message field");
            }

            // Create a new dispute
            Dispute dispute = new Dispute();
            dispute.setTransactionId("AUTO-" + System.currentTimeMillis());
            dispute.setReason("New dispute from WhatsApp: " + message);
            dispute.setStatus("pending");
            dispute.setCreatedAt(LocalDateTime.now());
            dispute.setUpdatedAt(LocalDateTime.now());

            // Save the dispute
            Dispute savedDispute = disputeService.createDispute(dispute);

            log.info("✅ Dispute created successfully for WhatsApp message from {}", sender);

            // Return the saved dispute object as JSON
            return ResponseEntity.ok(savedDispute);

        } catch (Exception e) {
            log.error("❌ Error processing WhatsApp webhook: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error creating dispute");
        }
    }
}