package org.example.gestion_user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



import static org.mockito.Mockito.verify;

class CustomFilterTest {

    @Test
    void testDoFilter() throws Exception {
        // Arrange
        CustomFilter customFilter = new CustomFilter();
        ServletRequest servletRequest = Mockito.mock(ServletRequest.class);
        ServletResponse servletResponse = Mockito.mock(ServletResponse.class);
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        // Act
        customFilter.doFilter(servletRequest, servletResponse, filterChain);

        // Assert
        verify(filterChain).doFilter(servletRequest, servletResponse);
    }
}
