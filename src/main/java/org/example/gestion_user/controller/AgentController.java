package org.example.gestion_user.controller;

import lombok.RequiredArgsConstructor;
import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.model.enumeration.Roles;
import org.example.gestion_user.service.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor //pour private final
@PreAuthorize("hasRole('ADMIN')")
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAgent(@RequestParam String lastname,
                                         @RequestParam String firstname,
                                         @RequestParam String email,
                                         @RequestParam String emailConfirmation,
                                         @RequestParam String phonenumber,
                                         @RequestParam("cinRecto") MultipartFile cinRecto,
                                         @RequestParam("cinVerso") MultipartFile cinVerso,
                                         @RequestParam String numCin,
                                         @RequestParam String address,
                                         @RequestParam String description,
                                         @RequestParam String birthdate,
                                         @RequestParam Long numLicence,
                                         @RequestParam Long numRegCom) {

        try {
            byte[] cinRectoBytes = cinRecto.getBytes();
            byte[] cinVersoBytes = cinVerso.getBytes();
            AgentDto agentDto = agentService.createAgent(lastname, firstname, email, emailConfirmation,
                    numCin, address, phonenumber, description, birthdate, numLicence, numRegCom,
                    cinRectoBytes, cinVersoBytes);
            if (agentDto.getRole() == null) {
                agentDto.setRole(Roles.ROLE_AGENT); // Valeur par défaut
            }
            // Retourner l'objet client avec un message de succès
            return ResponseEntity.ok().body(Map.of(
                    "message", "Agent ajouté avec succès",
                    "agent", agentDto
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
    public ResponseEntity<AgentDto> getAgentById(@PathVariable Long id) {
        return agentService.getAgentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<List<AgentDto>> getAllAgents() {
        return ResponseEntity.ok(agentService.getAllAgents());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable Long id) {
        try {
            agentService.deleteAgent(id);
            return ResponseEntity.ok("Agent supprimé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAgent(
            @PathVariable Long id,
            @RequestBody AgentDto updatedAgentDto) {
        try {
            // Mise à jour de l'agent via le service
            AgentDto updatedAgent = agentService.updateAgent(id, updatedAgentDto);
            return ResponseEntity.ok(updatedAgent);
        } catch (IllegalArgumentException e) {
            // Retourne une erreur si l'agent n'est pas trouvé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Gestion d'autres exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue");
        }
    }
}
