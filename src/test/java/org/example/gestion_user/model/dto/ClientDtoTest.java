package org.example.gestion_user.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientDtoTest {

    @Test
    void testClientDtoSettersAndGetters() {
        // Arrange
        ClientDto clientDto = new ClientDto();

        // Act
        clientDto.setId(2L);
        clientDto.setLastname("Doe");
        clientDto.setFirstname("Jane");
        clientDto.setEmail("jane.doe@example.com");

        // Assert
        assertEquals(2L, clientDto.getId());
        assertEquals("Doe", clientDto.getLastname());
        assertEquals("Jane", clientDto.getFirstname());
        assertEquals("jane.doe@example.com", clientDto.getEmail());
    }
}
