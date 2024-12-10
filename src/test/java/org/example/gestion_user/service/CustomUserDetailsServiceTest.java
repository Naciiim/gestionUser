package org.example.gestion_user.service;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.repository.AgentRepository;
import org.example.gestion_user.repository.ClientRepository;
import org.example.gestion_user.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testLoadUserByUsername_AgentExists() {
        // Arrange
        String email = "agent@example.com";
        Agent agent = new Agent();
        agent.setEmail(email);
        agent.setPassword("password");

        when(agentRepository.findByEmail(email)).thenReturn(Optional.of(agent));
        // Pas besoin de stub pour clientRepository si ce n'est pas utilisé dans ce test

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_AGENT")));
    }

    @Test
    public void testLoadUserByUsername_ClientExists() {
        // Arrange
        String email = "client@example.com";
        Client client = new Client();
        client.setEmail(email);
        client.setPassword("password");

        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(client));
        when(agentRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT")));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        when(agentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(email));
    }
}
