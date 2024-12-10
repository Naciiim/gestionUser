package org.example.gestion_user.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentDtoTest {

    @Test
    void testAgentDtoSettersAndGetters() {
        // Arrange
        AgentDto agentDto = new AgentDto();

        // Act
        agentDto.setId(1L);
        agentDto.setLastname("Smith");
        agentDto.setFirstname("John");
        agentDto.setEmail("john.smith@example.com");

        // Assert
        assertEquals(1L, agentDto.getId());
        assertEquals("Smith", agentDto.getLastname());
        assertEquals("John", agentDto.getFirstname());
        assertEquals("john.smith@example.com", agentDto.getEmail());
    }
}

