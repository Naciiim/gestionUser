package org.example.gestion_user.service;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.repository.AgentRepository;
import org.example.gestion_user.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckFirstLoginForAgent() {
        // Given
        String email = "agent@example.com";
        Agent agent = new Agent();
        agent.setEmail(email);
        agent.setFirstLogin(true);

        when(agentRepository.findByEmail(email)).thenReturn(Optional.of(agent));

        // When
        Map<String, String> result = authService.checkFirstLogin(email);

        // Then
        assertNotNull(result);
        assertEquals("/api/auth/change-password", result.get("redirectUrl"));
        assertFalse(agent.isFirstLogin());
        verify(agentRepository, times(1)).save(agent);
    }

    @Test
    public void testCheckFirstLoginForClient() {
        // Given
        String email = "client@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setFirstLogin(true);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));

        // When
        Map<String, String> result = authService.checkFirstLogin(email);

        // Then
        assertNotNull(result);
        assertEquals("/api/auth/change-password", result.get("redirectUrl"));
        assertFalse(client.isFirstLogin());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testCheckFirstLoginNoFirstLogin() {
        // Given
        String email = "noFirstLogin@example.com";
        Agent agent = new Agent();
        agent.setEmail(email);
        agent.setFirstLogin(false);

        when(agentRepository.findByEmail(email)).thenReturn(Optional.of(agent));

        // When
        Map<String, String> result = authService.checkFirstLogin(email);

        // Then
        assertNull(result);
        verify(agentRepository, never()).save(agent);
    }

    @Test
    public void testChangePasswordForAgent() {
        // Given
        String email = "agent@example.com";
        String newPassword = "newPassword123";
        Agent agent = new Agent();
        agent.setEmail(email);
        agent.setFirstLogin(true);

        when(agentRepository.findByEmail(email)).thenReturn(Optional.of(agent));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // When
        authService.changePassword(email, newPassword);

        // Then
        assertEquals("encodedPassword", agent.getPassword());
        assertFalse(agent.isFirstLogin());
        verify(agentRepository, times(1)).save(agent);
    }

    @Test
    public void testChangePasswordForClient() {
        // Given
        String email = "client@example.com";
        String newPassword = "newPassword123";
        Client client = new Client();
        client.setEmail(email);
        client.setFirstLogin(true);

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // When
        authService.changePassword(email, newPassword);

        // Then
        assertEquals("encodedPassword", client.getPassword());
        assertFalse(client.isFirstLogin());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public void testChangePasswordUserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        String newPassword = "newPassword123";

        when(agentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.changePassword(email, newPassword);
        });

        assertEquals("Utilisateur introuvable", exception.getMessage());
        verify(agentRepository, never()).save(any());
        verify(clientRepository, never()).save(any());
    }
}
