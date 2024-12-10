package org.example.gestion_user.repository;

import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.model.enumeration.AccountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Rollback(false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testFindByEmail() {
        // Création et initialisation du client de test
        Client client = new Client();
        client.setEmail("test.client11447@example.com");
        client.setFirstname("Alice");
        client.setLastname("Smith");
        client.setPhonenumber("123456789");
        client.setPassword(passwordEncoder.encode("plainPassword123")); // Mot de passe encodé
        client.setAccountType(AccountType.COMPTE_200); // Exemple de type de compte

        // Ajout de la gestion des fichiers CIN si nécessaire
        try {
            client.setCinRectoPath("path/to/cin/recto".getBytes());
            client.setCinVersoPath("path/to/cin/verso".getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error saving CIN files", e);
        }

        // Sauvegarde du client pour le test
        Client savedClient = clientRepository.save(client);

        // Vérification que le client a bien été sauvegardé
        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getEmail()).isEqualTo("test.client11447@example.com");

        // Vérification que la méthode findByEmail fonctionne correctement
        Optional<Client> foundClient = clientRepository.findByEmail("test.client11447@example.com");

        assertThat(foundClient).isPresent();
        assertThat(foundClient.get().getEmail()).isEqualTo("test.client11447@example.com");
        assertThat(foundClient.get().getFirstname()).isEqualTo("Alice");
        assertThat(foundClient.get().getLastname()).isEqualTo("Smith");
    }
}
