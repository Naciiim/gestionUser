package org.example.gestion_user.model.entity;

import org.example.gestion_user.model.enumeration.Roles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testDefaultValues() {
        // Arrange
        Client client = new Client();

        // Act & Assert
        assertTrue(client.isFirstLogin(), "Default value for firstLogin should be true");
        assertNotNull(client.getRole(), "Role should not be null");
        assertEquals(Roles.ROLE_CLIENT, client.getRole(), "Default role should be ROLE_CLIENT");
    }
}
