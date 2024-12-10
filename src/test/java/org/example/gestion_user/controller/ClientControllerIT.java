package org.example.gestion_user.controller;

import org.example.gestion_user.model.dto.ClientDto;
import org.example.gestion_user.model.enumeration.AccountType;
import org.example.gestion_user.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientControllerIT {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    @WithMockUser(roles = "AGENT")
    public void testCreateClient() throws Exception {
        String lastname = "Doe";
        String firstname = "John";
        String email = "john.doe@example.com";
        String emailConfirmation = "john.doe@example.com";
        String phonenumber = "212650123456"; // Format corrigé
        AccountType accountType=AccountType.COMPTE_200;


        byte[] cinRectoBytes = "rectoContent".getBytes();
        byte[] cinVersoBytes = "versoContent".getBytes();

        ClientDto clientDto = new ClientDto();
        clientDto.setLastname(lastname);
        clientDto.setFirstname(firstname);
        clientDto.setEmail(email);

        clientDto.setPhonenumber("212650123456");


        when(clientService.createClient(
                eq(lastname), eq(firstname), eq(email), eq(emailConfirmation),
                 eq("212650123456"),
                any(byte[].class), any(byte[].class),eq(accountType)
        )).thenReturn(clientDto);

        mockMvc.perform(multipart("/api/clients/create")
                        .file("cinRecto", cinRectoBytes)
                        .file("cinVerso", cinVersoBytes)
                        .param("lastname", lastname)
                        .param("firstname", firstname)
                        .param("email", email)
                        .param("emailConfirmation", emailConfirmation)
                        .param("phonenumber", phonenumber)
                       .param("accountType", accountType.name())// Format corrigé
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                         // Veillez à envoyer l'énumération sous forme de String

                )
                .andExpect(status().isOk())
                .andExpect(content().string("Client ajouté avec succès"));

        verify(clientService, times(1)).createClient(
                eq(lastname), eq(firstname), eq(email), eq(emailConfirmation),
                eq("212650123456"),
                any(byte[].class), any(byte[].class),eq(accountType)
        );
    }



    @Test
    @WithMockUser(roles = "AGENT")
    public void testGetClientById() throws Exception {
        ClientDto clientDto = new ClientDto();

        clientDto.setLastname("Doe");

        when(clientService.getClientById(1L)).thenReturn(Optional.of(clientDto));

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Doe"));

        verify(clientService, times(1)).getClientById(1L);
    }

    @Test
    @WithMockUser(roles = "AGENT")
    public void testGetAllClients() throws Exception {
        ClientDto clientDto = new ClientDto();

        clientDto.setLastname("Doe");

        when(clientService.getAllClients()).thenReturn(Collections.singletonList(clientDto));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].lastname").value("Doe"));

        verify(clientService, times(1)).getAllClients();
    }

    @Test
    @WithMockUser(roles = "AGENT")
    public void testDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());

        verify(clientService, times(1)).deleteClient(1L);
    }

    @Test
    @WithMockUser(roles = "AGENT")
    public void testUpdateClient() throws Exception {
        ClientDto clientDto = new ClientDto();

        clientDto.setLastname("Doe");

        when(clientService.updateClient(1L, clientDto)).thenReturn(clientDto);

        mockMvc.perform(put("/api/clients/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uid\":1,\"lastname\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Doe"));

        verify(clientService, times(1)).updateClient(1L, clientDto);
    }
}