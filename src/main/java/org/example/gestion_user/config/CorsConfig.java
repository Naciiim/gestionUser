package org.example.gestion_user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Cette règle s'applique à tous les chemins commençant par /api/
                .allowedOrigins("http://localhost:4200")  // Remplacez par l'URL de votre frontend Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Autoriser les méthodes HTTP que vous souhaitez
                .allowedHeaders("*")  // Autoriser tous les en-têtes
                .allowCredentials(true);  // Permet l'envoi des cookies et des informations d'authentification
    }
}