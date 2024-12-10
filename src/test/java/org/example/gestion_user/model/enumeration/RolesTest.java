package org.example.gestion_user.model.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RolesTest {

    @Test
    void testRolesExistence() {
        // Act & Assert
        assertNotNull(Roles.ROLE_ADMIN);
        assertNotNull(Roles.ROLE_AGENT);
        assertNotNull(Roles.ROLE_CLIENT);
    }
}
