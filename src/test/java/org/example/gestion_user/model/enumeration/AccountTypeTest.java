package org.example.gestion_user.model.enumeration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTypeTest {

    @Test
    void testFromDescription_Valid() {
        // Act & Assert
        assertEquals(AccountType.COMPTE_200, AccountType.fromDescription("Compte 200"));
        assertEquals(AccountType.COMPTE_5000, AccountType.fromDescription("Compte 5000"));
        assertEquals(AccountType.COMPTE_20000, AccountType.fromDescription("Compte 20000"));
    }

    @Test
    void testFromDescription_Invalid() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AccountType.fromDescription("Invalid Description"));

        assertTrue(exception.getMessage().contains("No such value for CompteType description"));
    }

    @Test
    void testFromPlafond_Valid() {
        // Act & Assert
        assertEquals(AccountType.COMPTE_200, AccountType.fromPlafond(200));
        assertEquals(AccountType.COMPTE_5000, AccountType.fromPlafond(5000));
        assertEquals(AccountType.COMPTE_20000, AccountType.fromPlafond(20000));
    }

    @Test
    void testFromPlafond_Invalid() {
        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                AccountType.fromPlafond(9999));

        assertTrue(exception.getMessage().contains("No such value for CompteType plafond"));
    }
}
