package com.verifi.service;

import com.verifi.client.TransactionClient;
import com.verifi.model.Dispute;
import com.verifi.repository.DisputeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisputeService {

    private final DisputeRepository disputeRepository;
    private final TransactionClient transactionClient;

    public DisputeService(DisputeRepository disputeRepository, TransactionClient transactionClient) {
        this.disputeRepository = disputeRepository;
        this.transactionClient = transactionClient;
    }

    // Create new dispute
    public Dispute createDispute(Dispute dispute) {
        dispute.setCreatedAt(LocalDateTime.now());
        dispute.setUpdatedAt(LocalDateTime.now());
        return disputeRepository.save(dispute);
    }

    // Get all disputes
    public List<Dispute> getAllDisputes() {
        return disputeRepository.findAll();
    }

    // Get dispute by ID
    public Dispute getDisputeById(String id) {
        return disputeRepository.findById(id).orElse(null);
    }

    // Update status
    public Dispute updateDisputeStatus(String id, String status) {
        Dispute dispute = disputeRepository.findById(id).orElse(null);
        if (dispute != null) {
            dispute.setStatus(status);
            dispute.setUpdatedAt(LocalDateTime.now());
            if ("RESOLVED".equalsIgnoreCase(status) && dispute.getTransactionId() != null) {
                transactionClient.updateTransactionStatus(dispute.getTransactionId(), "REVERSED");
            }
            return disputeRepository.save(dispute);
        }
        return null;
    }

    // Update full dispute
    public Dispute updateDispute(String id, Dispute updatedDispute) {
        return disputeRepository.findById(id)
                .map(existing -> {
                    existing.setStatus(updatedDispute.getStatus());
                    existing.setResolutionNote(updatedDispute.getResolutionNote());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return disputeRepository.save(existing);
                })
                .orElse(null);
    }

    // Delete dispute
    public boolean deleteDispute(String id) {
        if (disputeRepository.existsById(id)) {
            disputeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
