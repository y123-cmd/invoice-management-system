package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.LoginRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.RegisterRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AuthResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import com.imbank.payments.corporate.corporateinvoicesystem.security.*;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.doAnswer(invocation -> {
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }
    @Test
    void register_ShouldReturn201_WhenUserIsRegistered()throws Exception{
        RegisterRequest request = new RegisterRequest(
                "yvonne@imbank.co.ke",
                "Password123",
                Role.USER,
                "0712345678"
        );
        AuthResponse authResponse = new AuthResponse(
                "mocked-jwt-token",
                "yvonne@imbank.com",
                Role.USER
        );
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse);
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.token").value("mocked-jwt-token"))
                .andExpect(jsonPath("$.data.email").value("yvonne@imbank.com"));
    }
    @Test
    void register_ShouldReturn400_WhenRequestIsInvalid() throws Exception{
        RegisterRequest request = new RegisterRequest(null,null,null,null);
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void login_ShouldReturn200_WhenCredentialsAreValid() throws Exception{
        LoginRequest loginRequest = new LoginRequest("yvonne@imbank.co.ke", "Password123");
        AuthResponse authResponse = new AuthResponse(
                "mocked-jwt-token",
                "yvonne@imbank.co.ke",
                Role.USER
        );
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(authResponse);
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("mocked-jwt-token"));
    }
    @Test
    void login_ShouldReturn400_WhenRequestIsInvalid() throws Exception{
        LoginRequest loginRequest = new LoginRequest(null,null);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
