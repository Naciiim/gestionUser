package org.example.gestion_user.service;

import lombok.RequiredArgsConstructor;
import org.example.gestion_user.config.PasswordGenerator;
import org.example.gestion_user.model.dto.ClientDto;
import org.example.gestion_user.model.entity.Client;
import org.example.gestion_user.model.enumeration.AccountType;
import org.example.gestion_user.model.mapper.ClientMapper;
import org.example.gestion_user.repository.ClientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ClientDto createClient(String lastname, String firstname, String email, String emailConfirmation,
                                  String phonenumber, byte[] cinRectoPath, byte[] cinVersoPath,
                                  AccountType accountType) {
        // Vérification de la confirmation de l'email
        if (!email.equals(emailConfirmation)) {
            throw new IllegalArgumentException("Email does not match confirmation");
        }

        // Vérification du format du numéro de téléphone
        phonenumber = "+" + phonenumber;
        if (!phonenumber.matches("^\\+212[6-7][0-9]{8}$")) {
            throw new IllegalArgumentException("Phone number does not match the Moroccan form");
        }

        // Génération du mot de passe et encodage
        String password = PasswordGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(password);

        // Création de l'objet Client
        Client client = new Client();
        client.setLastname(lastname);
        client.setFirstname(firstname);
        client.setEmail(email);
        client.setPhonenumber(phonenumber);
        client.setPassword(encodedPassword);
        client.setAccountType(accountType);

        // Ajout des fichiers CIN
        try {
            client.setCinRectoPath(cinRectoPath);
            client.setCinVersoPath(cinVersoPath);
        } catch (Exception e) {
            throw new RuntimeException("Error saving CIN files", e);
        }

        // Sauvegarde en base de données
        Client savedClient = clientRepository.save(client);

        // Envoi de l'email avec le mot de passe
        emailService.sendPasswordEmail(email, password);

        // Conversion en DTO et retour
        return ClientMapper.INSTANCE.toDto(savedClient);
    }


    public List<ClientDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
    public Optional<ClientDto> getClientById(Long id)
    {
        return clientRepository.findById(id)
                .map(ClientMapper.INSTANCE::toDto);
    }
    public ClientDto updateClient(Long id, ClientDto updatedClientDto) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if (optionalClient.isPresent()) {
            Client client = optionalClient.get();

            // Mise à jour des attributs
            client.setLastname(updatedClientDto.getLastname());
            client.setFirstname(updatedClientDto.getFirstname());
            client.setEmail(updatedClientDto.getEmail());
            client.setPhonenumber(updatedClientDto.getPhonenumber());
            client.setCinRectoPath(updatedClientDto.getCinRectoPath());
            client.setCinVersoPath(updatedClientDto.getCinVersoPath());
            client.setFirstLogin(updatedClientDto.isFirstLogin());

            Client updatedClient = clientRepository.save(client);
            return ClientMapper.INSTANCE.toDto(updatedClient);
        } else {
            throw new IllegalArgumentException("Client not found");
        }
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
