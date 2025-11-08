package com.verify.controller;

import com.verify.model.Transaction;
import com.verify.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Create a transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction tx) {
        return ResponseEntity.ok(transactionService.createTransaction(tx));
    }

    // Get all transactions
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // Get transactions by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(transactionService.getTransactionsByUserId(userId));
    }

    // Get transaction by id
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable String id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction tx) {
        Transaction updated = transactionService.updateTransaction(id, tx);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }


    // Patch: update status only
    @PatchMapping("/{id}/status")
    public ResponseEntity<Transaction> updateStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        if (status == null || status.isEmpty()) return ResponseEntity.badRequest().build();
        Transaction updated = transactionService.updateTransactionStatus(id, status);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Delete transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        boolean deleted = transactionService.deleteTransaction(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
