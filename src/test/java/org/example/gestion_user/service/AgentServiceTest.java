package org.example.gestion_user.service;

import org.example.gestion_user.config.PasswordGenerator;
import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.repository.AgentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AgentService agentService;

    @Test
    void testCreateAgent_SuccessfulCreation() {
        // Arrange
        String lastname = "Doe";
        String firstname = "John";
        String email = "john.doe@example.com";
        String emailConfirmation = "john.doe@example.com";
        String numCin = "A123456";
        String address = "123 Main St";
        String phonenumber = "212612345678";
        String description = "Test agent";
        String birthdate = "1990-01-01";
        Long numLicence = 12345L;
        Long numRegCom = 54321L;
        byte[] cinRectoPath = "CinRecto".getBytes();
        byte[] cinVersoPath = "CinVerso".getBytes();

        // Utilisez un mot de passe spécifique ici pour correspondre à l'appel réel
        String generatedPassword = "sfVqbZmlM@W5";
        String encodedPassword = "encodedPassword123";

        // Simule la génération du mot de passe
        PasswordGenerator.generatePassword = () -> generatedPassword;

        // Simule l'encodage du mot de passe pour n'importe quelle chaîne de caractères
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        // Simule la sauvegarde de l'agent
        Agent agent = new Agent();
        agent.setEmail(email);
        agent.setPassword(encodedPassword);
        Mockito.when(agentRepository.save(Mockito.any(Agent.class))).thenAnswer(invocation -> {
            Agent savedAgent = invocation.getArgument(0);
            savedAgent.setId(1L); // Simule l'assignation d'un ID
            return savedAgent;
        });

        // Act
        AgentDto result = agentService.createAgent(
                lastname, firstname, email, emailConfirmation,
                numCin, address, phonenumber, description,
                birthdate, numLicence, numRegCom,
                cinRectoPath, cinVersoPath
        );

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals(lastname, result.getLastname());
        assertEquals(firstname, result.getFirstname());

        // Vérifie que les dépendances ont été appelées
        Mockito.verify(agentRepository).save(Mockito.any(Agent.class));
        Mockito.verify(emailService).sendPasswordEmail(Mockito.eq(email), Mockito.anyString());
        Mockito.verify(passwordEncoder).encode(Mockito.anyString()); // Vérifie l'encodage avec n'importe quelle chaîne
    }


    @Test
    void testCreateAgent_EmailMismatch_ThrowsException() {
        // Arrange
        String email = "john.doe@example.com";
        String emailConfirmation = "jane.doe@example.com";

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            agentService.createAgent(
                    "Doe", "John", email, emailConfirmation,
                    "A123456", "123 Main St", "612345678", "Test agent",
                    "1990-01-01", 12345L, 54321L,
                    "CinRecto".getBytes(), "CinVerso".getBytes()
            );
        });

        assertEquals("Email does not match confirmation", exception.getMessage());
    }

    @Test
    void testCreateAgent_InvalidPhoneNumber_ThrowsException() {
        // Arrange
        String phonenumber = "512345678"; // Numéro invalide

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            agentService.createAgent(
                    "Doe", "John", "john.doe@example.com", "john.doe@example.com",
                    "A123456", "123 Main St", phonenumber, "Test agent",
                    "1990-01-01", 12345L, 54321L,
                    "CinRecto".getBytes(),  "CinVerso".getBytes()
            );
        });

        assertEquals("Phone number does not match the moroccan form", exception.getMessage());
    }
}
