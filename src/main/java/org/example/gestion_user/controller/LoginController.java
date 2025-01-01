package org.example.gestion_user.controller;

import org.example.gestion_user.jwt.JwtTokenProvider;
import org.example.gestion_user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        try {
            // Authenticate the user (Agent or Client)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Check if the user is logging in for the first time
            Map<String, String> response = authService.checkFirstLogin(email);

            if (response != null && response.containsKey("redirectUrl")) {
                // Renvoie une réponse avec un statut OK (200) et l'URL de redirection
                return ResponseEntity.ok(response);
            }


            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(Map.of("token", token, "message", "Login successful"));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Les mots de passe ne correspondent pas."));
            }

            // Appelle la méthode de changement de mot de passe et récupère la redirection
            Map<String, String> redirectResponse = authService.changePassword(email, newPassword);
            return ResponseEntity.ok(redirectResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
