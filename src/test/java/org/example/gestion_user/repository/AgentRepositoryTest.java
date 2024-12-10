package org.example.gestion_user.repository;

import org.example.gestion_user.model.entity.Agent;
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
public class AgentRepositoryTest {

    @Autowired
    private AgentRepository agentRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testFindByEmail() {
        // Création et initialisation de l'agent de test
        Agent agent = new Agent();
        agent.setUid(agent.generateUid());
        agent.setPassword(passwordEncoder.encode("plainPassword123")); // Mot de passe encodé
        agent.setLastname("Doe");
        agent.setFirstname("John");
        agent.setEmail("test2555@example.com");
        agent.setNumCin("12345678");
        agent.setAddress("123 Rue de Test");
        agent.setPhonenumber("0123456789");
        agent.setDescription("Test agent description");
        agent.setBirthdate("1/04/02");
        agent.setNumLicence(2345L);
        agent.setNumRegCom(12345L);

        // Sauvegarde de l'agent pour le test
        Agent savedAgent = agentRepository.save(agent);

        // Vérification que l'agent a bien été sauvegardé
        assertThat(savedAgent).isNotNull();
        assertThat(savedAgent.getEmail()).isEqualTo("test2555@example.com");

        // Vérification que la méthode findByEmail fonctionne correctement
        Optional<Agent> foundAgent = agentRepository.findByEmail("test2555@example.com");

        assertThat(foundAgent).isPresent();
        assertThat(foundAgent.get().getEmail()).isEqualTo("test2555@example.com");
        assertThat(foundAgent.get().getLastname()).isEqualTo("Doe");
        assertThat(foundAgent.get().getFirstname()).isEqualTo("John");
    }
}
