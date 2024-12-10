package org.example.gestion_user.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendPasswordEmail() {
        // Act
        assertDoesNotThrow(() -> emailService.sendPasswordEmail("test@example.com", "password123"));
    }
}
