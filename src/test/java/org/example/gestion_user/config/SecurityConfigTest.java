package org.example.gestion_user.config;

import org.example.gestion_user.config.SecurityConfig;
import org.example.gestion_user.jwt.JwtTokenProvider;
import org.example.gestion_user.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService customUserDetailsService;
    private SecurityConfig securityConfig;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);
        securityConfig = new SecurityConfig(jwtTokenProvider, customUserDetailsService);
        passwordEncoder = securityConfig.passwordEncoder();
    }

    @Test
    void testAuthenticationProvider() {
        // Mock a UserDetailsService response
        UserDetails user = User.builder()
                .username("test@example.com")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();
        when(customUserDetailsService.loadUserByUsername("test@example.com")).thenReturn(user);

        // Create the authentication provider
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();

        // Perform authentication
        Authentication authentication = provider.authenticate(
                new UsernamePasswordAuthenticationToken("test@example.com", "password")
        );

        // Verify the result
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getName()).isEqualTo("test@example.com");
        assertThat(authentication.getAuthorities()).hasSize(1);
    }
}