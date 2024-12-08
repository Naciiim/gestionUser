package org.example.gestion_user.service;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.repository.AgentRepository;
import org.example.gestion_user.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Map<String, String> authenticateAgent(String email, String password) {
        // Authentifie l'agent
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email n'existe pas"));

        if (!passwordEncoder.matches(password, agent.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        if (agent.isFirstLogin()) {
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "http://localhost:8080/api/auth/change-password");
            return response;
        }

        return null; // Authentification réussie, pas de redirection nécessaire
    }

    public Map<String, String> authenticateClient(String email, String password) {
        // Authentifie le client
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email n'existe pas"));

        if (!passwordEncoder.matches(password, client.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        if (client.isFirstLogin()) {
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "http://localhost:8080/api/auth/change-password");
            return response;
        }

        return null; // Authentification réussie, pas de redirection nécessaire
    }
    public void changePassword(String email, String newPassword) {
        // Récupérez l'agent par son identifiant
        Agent agent = agentRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable"));

        // Encodez le nouveau mot de passe et mettez à jour l'état de connexion
        agent.setPassword(passwordEncoder.encode(newPassword));
        agent.setFirstLogin(false); // Marquer que le premier login est terminé
        agentRepository.save(agent);
    }
    
}
