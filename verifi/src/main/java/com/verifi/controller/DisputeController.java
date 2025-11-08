package com.verifi.controller;

import com.verifi.model.Dispute;
import com.verifi.service.DisputeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disputes")
@CrossOrigin(origins = "*")
public class DisputeController {

    private final DisputeService disputeService;

    public DisputeController(DisputeService disputeService) {
        this.disputeService = disputeService;
    }

    // 🟢 Create a new dispute
    @PostMapping
    public ResponseEntity<Dispute> createDispute(@RequestBody Dispute dispute) {
        return ResponseEntity.ok(disputeService.createDispute(dispute));
    }

    // 🟢 Get all disputes
    @GetMapping
    public ResponseEntity<List<Dispute>> getAllDisputes() {
        return ResponseEntity.ok(disputeService.getAllDisputes());
    }

    // 🟢 Get a single dispute by ID
    @GetMapping("/{id}")
    public ResponseEntity<Dispute> getDisputeById(@PathVariable String id) {
        Dispute dispute = disputeService.getDisputeById(id);
        return dispute != null ? ResponseEntity.ok(dispute) : ResponseEntity.notFound().build();
    }

    // 🟡 Update dispute status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Dispute> updateDisputeStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> request
    ) {
        String status = request.get("status");
        if (status == null || status.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Dispute updated = disputeService.updateDisputeStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // 🔴 Delete a dispute by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispute(@PathVariable String id) {
        boolean deleted = disputeService.deleteDispute(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
