package org.example.gestion_user.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



import static org.mockito.Mockito.verify;

class RestAuthenticationEntryPointTest {

    @Test
    void testCommence() throws Exception {
        // Arrange
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Exception authException = new Exception("Unauthorized");

        // Act
        entryPoint.commence(request, response, null);

        // Assert
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
