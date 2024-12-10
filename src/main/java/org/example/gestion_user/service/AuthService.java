package org.example.gestion_user.service;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.repository.AgentRepository;
import org.example.gestion_user.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AgentRepository agentRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> checkFirstLogin(String email) {
        // Check if the email belongs to an agent
        Agent agent = agentRepository.findByEmail(email).orElse(null);
        if (agent != null && agent.isFirstLogin()) {
            agent.setFirstLogin(false); // Mark as not first login
            agentRepository.save(agent);
            return Map.of("redirectUrl", "/api/auth/change-password");
        }

        // Check if the email belongs to a client
        Client client = clientRepository.findByEmail(email).orElse(null);
        if (client != null && client.isFirstLogin()) {
            client.setFirstLogin(false); // Mark as not first login
            clientRepository.save(client);
            return Map.of("redirectUrl", "/api/auth/change-password");
        }

        return null; // Not a first login
    }

    public void changePassword(String email, String newPassword) {
        // Try to retrieve the user as an Agent
        Agent agent = agentRepository.findByEmail(email).orElse(null);

        if (agent != null) {
            // Update password and mark first login as complete for Agent
            agent.setPassword(passwordEncoder.encode(newPassword));
            agent.setFirstLogin(false);
            agentRepository.save(agent);
            return;
        }

        // Try to retrieve the user as a Client if not found as Agent
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Update password and mark first login as complete for Client
        client.setPassword(passwordEncoder.encode(newPassword));
        client.setFirstLogin(false);
        clientRepository.save(client);
    }

}
