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


    public Map<String, String> changePassword(String email, String newPassword) {
        // Recherche de l'utilisateur en tant qu'Agent
        Agent agent = agentRepository.findByEmail(email).orElse(null);

        if (agent != null) {
            // Met à jour le mot de passe et marque la première connexion comme terminée pour l'Agent
            agent.setPassword(passwordEncoder.encode(newPassword));
            agent.setFirstLogin(false);
            agentRepository.save(agent);
            // Retourne l'URL de redirection après un changement réussi
            return Map.of("redirectUrl", "/api/auth/login");
        }

        // Recherche de l'utilisateur en tant que Client si ce n'est pas un Agent
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Met à jour le mot de passe et marque la première connexion comme terminée pour le Client
        client.setPassword(passwordEncoder.encode(newPassword));
        client.setFirstLogin(false);
        clientRepository.save(client);

        // Retourne l'URL de redirection après un changement réussi
        return Map.of("redirectUrl", "/api/auth/login");
    }

}
