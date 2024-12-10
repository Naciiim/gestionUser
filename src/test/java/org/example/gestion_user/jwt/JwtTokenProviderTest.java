package org.example.gestion_user.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = "90473ca1a782706fc113a2427cc758d782c35df81aedd68d78caea78a749838df9aeee06befe0ec1e9251feafc060a417ab12709f7910acddc77897921bf7751";
        jwtTokenProvider.jwtExpirationMs = 3600000; // 1 hour
    }

    @Test
    void testResolveToken() {
        // Mock the HttpServletRequest
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        // Set up the mock to return a Bearer token
        when(request.getHeader("Authorization")).thenReturn("Bearer test.jwt.token");

        // Call the resolveToken method
        String resolvedToken = jwtTokenProvider.resolveToken(request);

        // Verify the result
        assertEquals("test.jwt.token", resolvedToken, "Resolved token should match the original token without 'Bearer '");

        // Verify the getHeader method was called
        verify(request, times(1)).getHeader("Authorization");
    }
}