package org.example.gestion_user.config;

import org.example.gestion_user.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    private final AgentService agentService;

    @Autowired
    public SecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint, AgentService agentService) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.agentService = agentService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**", "/api/auth/**").permitAll()
                        .requestMatchers("/api/agents/**").hasRole("ADMIN")

                        // Restreindre l'accès des agents
                        .requestMatchers("/api/clients/**").hasRole( "AGENT")

                        // Authentifier tout le reste
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint)
                )
                .httpBasic(withDefaults()); // Utiliser l'authentification Basic pour l'exemple

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("adminpassword"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
