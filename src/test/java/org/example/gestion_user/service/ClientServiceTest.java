package org.example.gestion_user.service;

import org.example.gestion_user.model.dto.ClientDto;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.model.enumeration.AccountType;
import org.example.gestion_user.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient_Success() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(new Client());

        // Act
        ClientDto result = clientService.createClient(
                "Doe", "Jane", "jane.doe@example.com", "jane.doe@example.com",
                "212612345678", new byte[]{}, new byte[]{}, AccountType.COMPTE_200
        );

        // Assert
        assertNotNull(result);
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(emailService, times(1)).sendPasswordEmail(eq("jane.doe@example.com"), anyString());
    }
}
