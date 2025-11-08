// src/main/java/com/verifi/service/AutoResolveService.java
package com.verifi.service;

import com.verifi.client.TransactionClient;
import com.verifi.model.Dispute;
import com.verifi.repository.DisputeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutoResolveService {
    private final DisputeRepository disputeRepository;
    private final TransactionClient transactionClient;
    private final DisputeService disputeService; // to update dispute status or other operations
    private final Logger log = LoggerFactory.getLogger(AutoResolveService.class);

    // AUTO-RESOLVE CONFIG
    private static final double AUTO_RESOLVE_THRESHOLD = 5000.0; // ₦5,000

    public AutoResolveService(DisputeRepository disputeRepository,
                              TransactionClient transactionClient,
                              DisputeService disputeService) {
        this.disputeRepository = disputeRepository;
        this.transactionClient = transactionClient;
        this.disputeService = disputeService;
    }

    /**
     * Scheduled worker runs every 60 seconds. Adjust to suit hackathon/demo needs.
     * You can also call runOnce() via the admin endpoint for manual testing.
     */
    @Scheduled(fixedDelayString = "${autoresolve.fixedDelayMs:60000}")
    public void scheduledRun() {
        try {
            log.info("AutoResolve worker running (scheduled)...");
            runOnce();
        } catch (Exception e) {
            log.error("AutoResolve scheduled run failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Single run: checks OPEN/PENDING disputes and auto-resolves those <= threshold.
     */
    @Transactional
    public void runOnce() {
        List<Dispute> pending = disputeRepository.findByStatusIn(List.of("PENDING", "OPEN"));
        log.info("Found {} pending/open disputes", pending.size());

        for (Dispute d : pending) {
            try {
                // fetch transaction from Transaction service
                var tx = transactionClient.getTransactionById(d.getTransactionId());
                if (tx == null) {
                    log.info("Transaction {} not found for dispute {}", d.getTransactionId(), d.getId());
                    continue;
                }

                // only attempt auto-resolve if dispute is not already resolved
                if (!"PENDING".equalsIgnoreCase(d.getStatus()) && !"OPEN".equalsIgnoreCase(d.getStatus())) {
                    log.info("Skipping dispute {} with status {}", d.getId(), d.getStatus());
                    continue;
                }

                // RULE: if amount <= threshold, auto-reverse
                if (tx.getAmount() <= AUTO_RESOLVE_THRESHOLD) {
                    log.info("Auto-resolving dispute {} (tx {} amount {})", d.getId(), tx.getId(), tx.getAmount());

                    // 1) Update transaction status to REVERSED using transaction service
                    var updatedTx = transactionClient.updateTransactionStatus(tx.getId(), "REVERSED");
                    if (updatedTx == null) {
                        log.warn("Failed to update transaction status for tx {}", tx.getId());
                        continue;
                    }

                    // 2) Update dispute status to RESOLVED locally
                    disputeService.updateDisputeStatus(d.getId(), "RESOLVED");

                    // 3) Optionally: log, send webhook, notify via chatbot, or write audit entry
                    log.info("Dispute {} auto-resolved and transaction {} reversed", d.getId(), tx.getId());
                } else {
                    log.info("Dispute {} amount {} greater than threshold; skipping auto-resolve", d.getId(), tx.getAmount());
                }
            } catch (Exception ex) {
                log.error("Error processing dispute {}: {}", d.getId(), ex.getMessage(), ex);
            }
        }
    }
}
