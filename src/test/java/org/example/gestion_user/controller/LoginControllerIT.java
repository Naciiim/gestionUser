package org.example.gestion_user.controller;

import org.example.gestion_user.jwt.JwtTokenProvider;

import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class LoginControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Clear the database and add test data
        agentRepository.deleteAll();

        // Add a test agent
        Agent agent = new Agent();
        agent.setEmail("test@example.com");
        agent.setPassword(passwordEncoder.encode("password"));
        agent.setFirstLogin(true);
        agentRepository.save(agent);
    }

    @Test
    void testLoginFirstTimeRedirect() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.redirectUrl").value("/api/auth/change-password"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Set first login to false
        Agent agent = agentRepository.findByEmail("test@example.com").orElseThrow();
        agent.setFirstLogin(false);
        agentRepository.save(agent);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void testChangePasswordSuccess() throws Exception {
        mockMvc.perform(post("/api/auth/change-password")
                        .param("email", "test@example.com")
                        .param("newPassword", "newPassword123")
                        .param("confirmPassword", "newPassword123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Mot de passe changé avec succès !"));
    }

    @Test
    void testChangePasswordMismatch() throws Exception {
        mockMvc.perform(post("/api/auth/change-password")
                        .param("email", "test@example.com")
                        .param("newPassword", "newPassword123")
                        .param("confirmPassword", "wrongPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Les mots de passe ne correspondent pas."));
    }
}