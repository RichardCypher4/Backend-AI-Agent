package com.verifi.service;

import com.verifi.model.Dispute;
import com.verifi.repository.DisputeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoResolveWorker {

    private final DisputeRepository disputeRepository;

    public AutoResolveWorker(DisputeRepository disputeRepository) {
        this.disputeRepository = disputeRepository;
    }

    // Runs every 60 seconds
    @Scheduled(fixedRate = 60000)
    public void autoResolveDisputes() {
        List<Dispute> pendingDisputes = disputeRepository.findByStatus("PENDING");

        for (Dispute dispute : pendingDisputes) {
            // Here we assume you already have a transaction linked with amount
            double transactionAmount = 3000; // You can replace this with real lookup if integrated

            if (transactionAmount <= 5000) {
                dispute.setStatus("AUTO_RESOLVED");
                disputeRepository.save(dispute);
                System.out.println("✅ Dispute auto-resolved: " + dispute.getId());
            }
        }
    }
}
