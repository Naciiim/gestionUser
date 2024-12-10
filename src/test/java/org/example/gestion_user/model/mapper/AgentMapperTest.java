package org.example.gestion_user.model.mapper;

import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.model.entity.Agent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentMapperTest {

    @Test
    void testToDto() {
        // Arrange
        Agent agent = new Agent();
        agent.setId(1L);
        agent.setFirstname("John");
        agent.setLastname("Smith");
        agent.setEmail("john.smith@example.com");
        agent.setPhonenumber("123456789");

        // Act
        AgentDto agentDto = AgentMapper.INSTANCE.toDto(agent);

        // Assert
        assertNotNull(agentDto, "AgentDto should not be null");
        assertEquals(agent.getId(), agentDto.getId());
        assertEquals(agent.getFirstname(), agentDto.getFirstname());
        assertEquals(agent.getLastname(), agentDto.getLastname());
        assertEquals(agent.getEmail(), agentDto.getEmail());
        assertEquals(agent.getPhonenumber(), agentDto.getPhonenumber());
    }

    @Test
    void testToEntity() {
        // Arrange
        AgentDto agentDto = new AgentDto();
        agentDto.setId(2L);
        agentDto.setFirstname("Jane");
        agentDto.setLastname("Doe");
        agentDto.setEmail("jane.doe@example.com");
        agentDto.setPhonenumber("987654321");

        // Act
        Agent agent = AgentMapper.INSTANCE.toEntity(agentDto);

        // Assert
        assertNotNull(agent, "Agent should not be null");
        assertEquals(agentDto.getId(), agent.getId());
        assertEquals(agentDto.getFirstname(), agent.getFirstname());
        assertEquals(agentDto.getLastname(), agent.getLastname());
        assertEquals(agentDto.getEmail(), agent.getEmail());
        assertEquals(agentDto.getPhonenumber(), agent.getPhonenumber());
    }
}
