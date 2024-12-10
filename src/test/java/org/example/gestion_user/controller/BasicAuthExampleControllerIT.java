package org.example.gestion_user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

public class BasicAuthExampleControllerIT {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Initialisation de MockMvc pour tester le contrôleur
        mockMvc = MockMvcBuilders.standaloneSetup(new BasicAuthExampleController()).build();
    }

    @Test
    public void testSecuredApi_withValidBasicAuth_shouldReturnOk() throws Exception {
        // Authentification Basic avec un utilisateur fictif
        String auth = "Basic " + java.util.Base64.getEncoder().encodeToString("user:password".getBytes());

        // Effectuer une requête GET avec l'en-tête Authorization valide
        mockMvc.perform(get("/public/secureAPI")
                        .header(HttpHeaders.AUTHORIZATION, auth))
                .andExpect(status().isOk())  // Statut attendu : 200 OK
                .andExpect(content().string("Authentication passed"));  // Contenu attendu
    }

    @Test
    public void testSecuredApi_withoutBasicAuth_shouldReturnUnauthorized() throws Exception {
        // Effectuer une requête GET sans l'en-tête Authorization
        mockMvc.perform(get("/public/secureAPI"))
                .andExpect(status().isUnauthorized())  // Statut attendu : 401 Unauthorized
                .andExpect(content().string("Unauthorized"));  // Contenu attendu
    }





}
