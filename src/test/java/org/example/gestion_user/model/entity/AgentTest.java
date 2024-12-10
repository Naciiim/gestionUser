package org.example.gestion_user.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {

    @Test
    void testGenerateUid() {
        // Arrange
        Agent agent = new Agent();

        // Act
        String uid1 = agent.generateUid();
        String uid2 = agent.generateUid(); // Deuxième appel pour vérifier que le même UID n'est pas régénéré

        // Assert
        assertNotNull(uid1, "UID should not be null");
        assertTrue(uid1.startsWith("AG"), "UID should start with 'AG'");
        assertEquals(uid1, uid2, "UID should not change after first generation");
    }
}
