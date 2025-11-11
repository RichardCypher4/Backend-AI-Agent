package com.verifi.controller;

import com.verifi.model.Dispute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DisputeWebSocketControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private DisputeWebSocketController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendDisputeUpdate_ShouldSendMessageAndReturnSent() {
        Dispute dispute = new Dispute();
        dispute.setId("123");
        dispute.setStatus("PENDING");

        String response = controller.sendDisputeUpdate(dispute);

        assertEquals("Sent!", response);
        verify(messagingTemplate, times(1)).convertAndSend("/topic/disputes", dispute);
    }

    @Test
    void sendDisputeUpdate_ShouldHandleNullDispute() {
        Dispute dispute = null;

        String response = controller.sendDisputeUpdate(dispute);

        assertEquals("Sent!", response);
        verify(messagingTemplate, times(1)).convertAndSend("/topic/disputes", dispute);
    }
}
