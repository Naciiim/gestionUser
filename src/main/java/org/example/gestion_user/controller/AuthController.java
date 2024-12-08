package org.example.gestion_user.controller;

import org.example.gestion_user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/loginAg")
    public ResponseEntity<Object> loginAg(@RequestParam String email, @RequestParam String password) {
        try {
            // Authentifie l'utilisateur via le service
            Map<String, String> response = authService.authenticateAgent(email, password);

            // Si une URL de redirection est renvoyée, c'est le premier login
            if (response != null && response.containsKey("redirectUrl")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .body(response);
            }

            // Authentification réussie sans redirection
            Map<String, String> successMessage = Map.of("message", "Connecté avec succès");
            return ResponseEntity.ok(successMessage);
        } catch (IllegalArgumentException e) {
            // En cas d'échec d'authentification, retourner un message d'erreur
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/loginCl")
    public ResponseEntity<Object> loginCli(@RequestParam String email, @RequestParam String password) {
        try {
            Map<String, String> response = authService.authenticateClient(email, password);

            if (response != null && response.containsKey("redirectUrl")) {
                return ResponseEntity.status(HttpStatus.FOUND).body(response);
            }

            Map<String, String> successMessage = Map.of("message", "Connecté avec succès");
            return ResponseEntity.ok(successMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Les mots de passe ne correspondent pas.");
            }

            authService.changePassword(email, newPassword);
            return ResponseEntity.ok("Mot de passe changé avec succès !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
