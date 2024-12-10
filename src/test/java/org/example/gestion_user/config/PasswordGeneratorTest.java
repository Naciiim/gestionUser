package org.example.gestion_user.config;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    @Test
    void testGeneratePasswordLength() {
        String password = PasswordGenerator.generatePassword();
        assertNotNull(password, "The generated password should not be null.");
        assertEquals(12, password.length(), "The password length should be 12 characters.");
    }

    @Test
    void testGeneratePasswordCharacters() {
        String password = PasswordGenerator.generatePassword();
        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";

        for (char c : password.toCharArray()) {
            assertTrue(validCharacters.indexOf(c) >= 0, "The password contains invalid characters.");
        }
    }

    @Test
    void testGeneratePasswordUniqueness() {
        Set<String> generatedPasswords = new HashSet<>();
        int numberOfPasswordsToGenerate = 1000;

        for (int i = 0; i < numberOfPasswordsToGenerate; i++) {
            String password = PasswordGenerator.generatePassword();
            assertFalse(generatedPasswords.contains(password), "Duplicate password found.");
            generatedPasswords.add(password);
        }
    }
}
