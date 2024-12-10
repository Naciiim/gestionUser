package org.example.gestion_user.controller;

import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.service.AgentService;
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

public class AgentControllerIT {

    private MockMvc mockMvc;

    @Mock
    private AgentService agentService;

    @InjectMocks
    private AgentController agentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(agentController).build();
    }

    @Test
    public void testCreateAgent() throws Exception {
        String lastname = "Doe";
        String firstname = "John";
        String email = "john.doe@example.com";
        String emailConfirmation = "john.doe@example.com";
        String phonenumber = "212650123456"; // Format corrigé
        String numCin = "A12345678";
        String address = "123 Main St";
        String description = "Test agent";
        String birthdate = "1990-01-01";
        Long numLicence = 123456789L;
        Long numRegCom = 987654321L;

        byte[] cinRectoBytes = "rectoContent".getBytes();
        byte[] cinVersoBytes = "versoContent".getBytes();

        AgentDto agentDto = new AgentDto();
        agentDto.setLastname(lastname);
        agentDto.setFirstname(firstname);
        agentDto.setEmail(email);
        agentDto.setNumCin(numCin);
        agentDto.setAddress(address);
        agentDto.setPhonenumber("212650123456");
        agentDto.setDescription(description);
        agentDto.setBirthdate(birthdate);
        agentDto.setNumLicence(numLicence);
        agentDto.setNumRegCom(numRegCom);

        when(agentService.createAgent(
                eq(lastname), eq(firstname), eq(email), eq(emailConfirmation),
                eq(numCin), eq(address), eq("212650123456"), eq(description),
                eq(birthdate), eq(numLicence), eq(numRegCom),
                any(byte[].class), any(byte[].class)
        )).thenReturn(agentDto);

        mockMvc.perform(multipart("/api/agents/create")
                        .file("cinRecto", cinRectoBytes)
                        .file("cinVerso", cinVersoBytes)
                        .param("lastname", lastname)
                        .param("firstname", firstname)
                        .param("email", email)
                        .param("emailConfirmation", emailConfirmation)
                        .param("phonenumber", phonenumber) // Format corrigé
                        .param("numCin", numCin)
                        .param("address", address)
                        .param("description", description)
                        .param("birthdate", birthdate)
                        .param("numLicence", String.valueOf(numLicence))
                        .param("numRegCom", String.valueOf(numRegCom))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Agent ajouté avec succès"));

        verify(agentService, times(1)).createAgent(
                eq(lastname), eq(firstname), eq(email), eq(emailConfirmation),
                eq(numCin), eq(address), eq("212650123456"), eq(description),
                eq(birthdate), eq(numLicence), eq(numRegCom),
                any(byte[].class), any(byte[].class)
        );
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAgentById() throws Exception {
        AgentDto agentDto = new AgentDto();
        agentDto.setUid(String.valueOf(1L));
        agentDto.setLastname("Doe");

        when(agentService.getAgentById(1L)).thenReturn(Optional.of(agentDto));

        mockMvc.perform(get("/api/agents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Doe"));

        verify(agentService, times(1)).getAgentById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllAgents() throws Exception {
        AgentDto agentDto = new AgentDto();
        agentDto.setUid(String.valueOf(1L));
        agentDto.setLastname("Doe");

        when(agentService.getAllAgents()).thenReturn(Collections.singletonList(agentDto));

        mockMvc.perform(get("/api/agents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].lastname").value("Doe"));

        verify(agentService, times(1)).getAllAgents();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteAgent() throws Exception {
        doNothing().when(agentService).deleteAgent(1L);

        mockMvc.perform(delete("/api/agents/1"))
                .andExpect(status().isOk());

        verify(agentService, times(1)).deleteAgent(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateAgent() throws Exception {
        AgentDto agentDto = new AgentDto();
        agentDto.setUid(String.valueOf(1L));
        agentDto.setLastname("Doe");

        when(agentService.updateAgent(1L, agentDto)).thenReturn(agentDto);

        mockMvc.perform(put("/api/agents/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uid\":1,\"lastname\":\"Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Doe"));

        verify(agentService, times(1)).updateAgent(1L, agentDto);
    }
}