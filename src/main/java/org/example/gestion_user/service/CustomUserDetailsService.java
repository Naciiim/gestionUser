package org.example.gestion_user.service;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.repository.AgentRepository;
import org.example.gestion_user.repository.ClientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CustomUserDetailsService(AgentRepository agentRepository, ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if ("admin".equals(email)) {
            return new org.springframework.security.core.userdetails.User(
                    "admin",
                    passwordEncoder.encode("12345"), // BCrypt hash for "12345"
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }
        Optional<Agent> agent = agentRepository.findByEmail(email);
        if (agent.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    agent.get().getEmail(),
                    agent.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_AGENT"))
            );
        }

        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    client.get().getEmail(),
                    client.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}

