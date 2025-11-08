package com.verifi.client;

import com.verifi.dto.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class TransactionClient {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/api/transactions"; // Adjust if needed
    private final Logger log = LoggerFactory.getLogger(TransactionClient.class);

    public TransactionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 🟢 Fetch a transaction by ID
    public TransactionDto getTransactionById(String id) {
        try {
            String url = baseUrl + "/" + id;
            ResponseEntity<TransactionDto> resp = restTemplate.getForEntity(url, TransactionDto.class);
            log.info("✅ Fetched transaction with ID {}", id);
            return resp.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Transaction not found: {}", id);
            return null;
        } catch (Exception e) {
            log.error("❌ Error fetching transaction {}: {}", id, e.getMessage());
            return null;
        }
    }

    // 🟡 Update transaction status using PUT
    public TransactionDto updateTransactionStatus(String id, String newStatus) {
        try {
            String url = baseUrl + "/" + id;

            // Build a TransactionDto with only status updated
            TransactionDto updatedTransaction = new TransactionDto();
            updatedTransaction.setStatus(newStatus);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TransactionDto> entity = new HttpEntity<>(updatedTransaction, headers);

            // Use PUT to update the resource
            restTemplate.put(url, entity);

            log.info("✅ Transaction {} status updated to {}", id, newStatus);

            // Fetch the updated transaction
            return getTransactionById(id);
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Transaction to update not found: {}", id);
            return null;
        } catch (Exception e) {
            log.error("❌ Error updating transaction {}: {}", id, e.getMessage());
            return null;
        }
    }
}
