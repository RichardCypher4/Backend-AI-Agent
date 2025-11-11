package com.verifi.service;

import com.verifi.client.TransactionClient;
import com.verifi.model.Dispute;
import com.verifi.repository.DisputeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DisputeServiceTest {

    @Mock
    private DisputeRepository disputeRepository;

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private DisputeService disputeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------- createDispute -----------------
    @Test
    void createDispute_ShouldSetTimestampsAndSave() {
        Dispute dispute = new Dispute();
        when(disputeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Dispute created = disputeService.createDispute(dispute);

        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
        verify(disputeRepository, times(1)).save(dispute);
    }

    // ----------------- getAllDisputes -----------------
    @Test
    void getAllDisputes_ShouldReturnList() {
        List<Dispute> disputes = Arrays.asList(new Dispute(), new Dispute());
        when(disputeRepository.findAll()).thenReturn(disputes);

        List<Dispute> result = disputeService.getAllDisputes();

        assertEquals(2, result.size());
        verify(disputeRepository, times(1)).findAll();
    }

    // ----------------- getDisputeById -----------------
    @Test
    void getDisputeById_ShouldReturnDispute_WhenExists() {
        Dispute dispute = new Dispute();
        dispute.setId("1");
        when(disputeRepository.findById("1")).thenReturn(Optional.of(dispute));

        Dispute result = disputeService.getDisputeById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void getDisputeById_ShouldReturnNull_WhenNotFound() {
        when(disputeRepository.findById("999")).thenReturn(Optional.empty());

        Dispute result = disputeService.getDisputeById("999");

        assertNull(result);
    }

    // ----------------- updateDisputeStatus -----------------
    @Test
    void updateDisputeStatus_ShouldReturnNull_WhenNotFound() {
        when(disputeRepository.findById("999")).thenReturn(Optional.empty());

        Dispute result = disputeService.updateDisputeStatus("999", "RESOLVED");

        assertNull(result);
        verify(transactionClient, never()).updateTransactionStatus(anyString(), anyString());
        verify(disputeRepository, never()).save(any());
    }

    @Test
    void updateDisputeStatus_ShouldNotCallTransactionClient_WhenStatusNotResolved() {
        Dispute dispute = new Dispute();
        dispute.setId("1");
        dispute.setTransactionId("tx1");

        when(disputeRepository.findById("1")).thenReturn(Optional.of(dispute));
        when(disputeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        disputeService.updateDisputeStatus("1", "PENDING");

        verify(transactionClient, never()).updateTransactionStatus(anyString(), anyString());
        verify(disputeRepository, times(1)).save(dispute);
    }

    @Test
    void updateDisputeStatus_ShouldCallTransactionClient_WhenStatusResolved() {
        Dispute dispute = new Dispute();
        dispute.setId("1");
        dispute.setTransactionId("tx1");

        when(disputeRepository.findById("1")).thenReturn(Optional.of(dispute));
        when(disputeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Dispute updated = disputeService.updateDisputeStatus("1", "RESOLVED");

        assertNotNull(updated);
        assertEquals("RESOLVED", updated.getStatus());
        verify(transactionClient, times(1)).updateTransactionStatus("tx1", "REVERSED");
        verify(disputeRepository, times(1)).save(dispute);
    }

    // ----------------- updateDispute -----------------
    @Test
    void updateDispute_ShouldReturnNull_WhenNotFound() {
        when(disputeRepository.findById("missing")).thenReturn(Optional.empty());

        Dispute result = disputeService.updateDispute("missing", new Dispute());

        assertNull(result);
        verify(disputeRepository, never()).save(any());
    }

    @Test
    void updateDispute_ShouldUpdateAndSave_WhenFound() {
        Dispute existing = new Dispute();
        existing.setId("1");

        Dispute update = new Dispute();
        update.setStatus("CLOSED");
        update.setResolutionNote("Done");

        when(disputeRepository.findById("1")).thenReturn(Optional.of(existing));
        when(disputeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Dispute result = disputeService.updateDispute("1", update);

        assertEquals("CLOSED", result.getStatus());
        assertEquals("Done", result.getResolutionNote());
        assertNotNull(result.getUpdatedAt());
        verify(disputeRepository, times(1)).save(existing);
    }

    // ----------------- deleteDispute -----------------
    @Test
    void deleteDispute_ShouldReturnTrue_WhenExists() {
        when(disputeRepository.existsById("1")).thenReturn(true);
        doNothing().when(disputeRepository).deleteById("1");

        boolean result = disputeService.deleteDispute("1");

        assertTrue(result);
        verify(disputeRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteDispute_ShouldReturnFalse_WhenNotExists() {
        when(disputeRepository.existsById("999")).thenReturn(false);

        boolean result = disputeService.deleteDispute("999");

        assertFalse(result);
        verify(disputeRepository, never()).deleteById(any());
    }
}
