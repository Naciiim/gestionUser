package org.example.gestion_user.controller;

import lombok.RequiredArgsConstructor;
import org.example.gestion_user.model.dto.ClientDto;

import org.example.gestion_user.model.enumeration.AccountType;
import org.example.gestion_user.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT')")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<?> createClient(@RequestParam String lastname,
                                          @RequestParam String firstname,
                                          @RequestParam String email,
                                          @RequestParam String emailConfirmation,
                                          @RequestParam String phonenumber,
                                          @RequestParam("cinRecto") MultipartFile cinRecto,
                                          @RequestParam("cinVerso") MultipartFile cinVerso,
                                          @RequestParam String accountType) {

        try {
            // Conversion du type de compte
            AccountType accountTypeEnum = AccountType.fromDescription(accountType);

            // Récupération des fichiers CIN
            byte[] cinRectoBytes = cinRecto.getBytes();
            byte[] cinVersoBytes = cinVerso.getBytes();

            // Création du client
            ClientDto clientDto = clientService.createClient(
                    lastname, firstname, email, emailConfirmation, phonenumber,
                    cinRectoBytes, cinVersoBytes, accountTypeEnum
            );

            // Retourner l'objet client avec un message de succès
            return ResponseEntity.ok().body(Map.of(
                    "message", "Client ajouté avec succès",
                    "client", clientDto
            ));
        } catch (IllegalArgumentException e) {
            // Retourner une erreur de validation avec un message clair
            return ResponseEntity.status(400).body(Map.of(
                    "message", "Erreur de validation des données",
                    "error", e.getMessage()
            ));
        } catch (IOException e) {
            // Erreur de lecture des fichiers
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Erreur lors de la lecture des fichiers CIN",
                    "error", e.getMessage()
            ));
        } catch (RuntimeException e) {
            // Autre erreur interne
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Erreur interne du serveur",
                    "error", e.getMessage()
            ));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok("Client supprimé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateClient(
            @PathVariable Long id,
            @RequestBody ClientDto updatedClientDto) {
        try {
            // Mise à jour du client via le service
            ClientDto updatedClient = clientService.updateClient(id, updatedClientDto);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            // Retourne une erreur si le client n'est pas trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Gestion d'autres exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
        }
    }
}
