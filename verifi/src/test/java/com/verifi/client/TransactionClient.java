package com.verifi.client;

import com.verifi.dto.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TransactionClient transactionClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTransactionById_ShouldReturnTransaction_WhenExists() {
        TransactionDto dto = new TransactionDto();
        dto.setId("tx1");
        when(restTemplate.getForEntity(anyString(), eq(TransactionDto.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        TransactionDto result = transactionClient.getTransactionById("tx1");

        assertEquals("tx1", result.getId());
    }

    @Test
    void getTransactionById_ShouldReturnNull_WhenNotFound() {
        when(restTemplate.getForEntity(anyString(), eq(TransactionDto.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        TransactionDto result = transactionClient.getTransactionById("missing");

        assertNull(result);
    }

    @Test
    void updateTransactionStatus_ShouldCallPutAndReturnUpdated() {
        TransactionDto dto = new TransactionDto();
        dto.setId("tx1");
        when(restTemplate.getForEntity(anyString(), eq(TransactionDto.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        doNothing().when(restTemplate).put(anyString(), any(HttpEntity.class));

        TransactionDto result = transactionClient.updateTransactionStatus("tx1", "REVERSED");

        assertNotNull(result);
        verify(restTemplate).put(anyString(), any(HttpEntity.class));
    }

    @Test
    void updateTransactionStatus_ShouldReturnNull_WhenError() {
        doThrow(new RuntimeException("boom")).when(restTemplate).put(anyString(), any());

        TransactionDto result = transactionClient.updateTransactionStatus("tx1", "FAILED");

        assertNull(result);
    }
}
