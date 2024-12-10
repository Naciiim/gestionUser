package org.example.gestion_user.model.mapper;

import org.example.gestion_user.model.dto.ClientDto;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.model.mapper.ClientMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

    @Test
    void testToDto() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        client.setFirstname("Alice");
        client.setLastname("Brown");
        client.setEmail("alice.brown@example.com");
        client.setPhonenumber("1122334455");

        // Act
        ClientDto clientDto = ClientMapper.INSTANCE.toDto(client);

        // Assert
        assertNotNull(clientDto, "ClientDto should not be null");
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getFirstname(), clientDto.getFirstname());
        assertEquals(client.getLastname(), clientDto.getLastname());
        assertEquals(client.getEmail(), clientDto.getEmail());
        assertEquals(client.getPhonenumber(), clientDto.getPhonenumber());
    }

    @Test
    void testToEntity() {
        // Arrange
        ClientDto clientDto = new ClientDto();
        clientDto.setId(2L);
        clientDto.setFirstname("Bob");
        clientDto.setLastname("White");
        clientDto.setEmail("bob.white@example.com");
        clientDto.setPhonenumber("5566778899");

        // Act
        Client client = ClientMapper.INSTANCE.toEntity(clientDto);

        // Assert
        assertNotNull(client, "Client should not be null");
        assertEquals(clientDto.getId(), client.getId());
        assertEquals(clientDto.getFirstname(), client.getFirstname());
        assertEquals(clientDto.getLastname(), client.getLastname());
        assertEquals(clientDto.getEmail(), client.getEmail());
        assertEquals(clientDto.getPhonenumber(), client.getPhonenumber());
    }
}
