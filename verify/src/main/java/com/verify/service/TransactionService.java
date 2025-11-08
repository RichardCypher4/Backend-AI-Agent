package com.verify.service;

import com.verify.model.Transaction;
import com.verify.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Create
    public Transaction createTransaction(Transaction tx) {
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(tx);
    }

    // Get all
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Get by userId
    public List<Transaction> getTransactionsByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Get by ID
    public Optional<Transaction> getTransactionById(String id) {
        return transactionRepository.findById(id);
    }

    // Full update
    public Transaction updateTransaction(String id, Transaction tx) {
        Optional<Transaction> optionalTx = transactionRepository.findById(id);
        if (optionalTx.isPresent()) {
            Transaction existingTx = optionalTx.get();
            existingTx.setUserId(tx.getUserId());
            existingTx.setToAccount(tx.getToAccount());
            existingTx.setAmount(tx.getAmount());
            existingTx.setStatus(tx.getStatus());
            existingTx.setUpdatedAt(LocalDateTime.now());
            return transactionRepository.save(existingTx);
        }
        return null;
    }

    // Update only status (patch)
    public Transaction updateTransactionStatus(String id, String status) {
        Optional<Transaction> optionalTx = transactionRepository.findById(id);
        if (optionalTx.isPresent()) {
            Transaction existingTx = optionalTx.get();
            existingTx.setStatus(status);
            existingTx.setUpdatedAt(LocalDateTime.now());
            return transactionRepository.save(existingTx);
        }
        return null;
    }

    // Delete
    public boolean deleteTransaction(String id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
