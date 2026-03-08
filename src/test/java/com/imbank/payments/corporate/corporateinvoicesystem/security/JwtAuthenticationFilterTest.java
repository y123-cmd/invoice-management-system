package com.imbank.payments.corporate.corporateinvoicesystem.security;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import io.jsonwebtoken.security.Request;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
    @Test
    void shouldSkipFilterWhenAuthorizationHeaderIsNull()
            throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldSkipFilterWhenAuthorizationHeaderIsInvalid()
        throws ServletException,IOException{
        when(request.getHeader("Authorization")).thenReturn("Basic sometoken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldAuthenticateUserWhenTokenIsValid()
            throws ServletException, IOException{
        User user = new User();
        user.setEmail("priston@gmail.com");
        user.setPassword("Priston@88");
        user.setRole(Role.USER);
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken123");

        when(jwtUtil.extractEmail("validtoken123")).thenReturn("priston@gmail.com");

        when(customUserDetailsService.loadUserByUsername("priston@gmail.com")).thenReturn(user);
        when(jwtUtil.isTokenValid("validtoken123",user)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request,response);


    }
    @Test
    void shouldNotAuthenticateWhenUserAlreadyAuthenticated() throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("priston@gmail.com", null, List.of())
        );
        when(request.getHeader("Authorization")).thenReturn("Bearer validtoken123");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(customUserDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }


}
