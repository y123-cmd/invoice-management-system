package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.LoginRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.RegisterRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AuthResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.UserRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private User buildUser(){
        User user = new User();
        user.setEmail("yvonne@imbank.com");
        user.setPassword("encodedPassword");
        user.setPhone("0712345678");
        user.setRole(Role.USER);
        return user;
    }
    @Test
    void shouldRegisterUserSuccessfully(){
        RegisterRequest request = new RegisterRequest(
                "yvonne@imbank.com","Password123",Role.USER,"0712345678"
        );
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(buildUser());
        when(jwtUtil.generateToken(anyString())).thenReturn("mocked-jwt-token");
        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("yvonne@imbank.com", response.getEmail());
        assertEquals(Role.USER, response.getRole());
    }
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "yvonne@imbank.com", "password123", Role.USER, "0712345678"
        );

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(buildUser()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Email Already Exists", exception.getMessage());
    }
    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("yvonne@imbank.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(buildUser()));
        when(jwtUtil.generateToken(anyString())).thenReturn("mocked-jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        assertEquals("yvonne@imbank.com", response.getEmail());
    }
    @Test
    void shouldThrowExceptionWhenUserNotFoundDuringLogin() {
        LoginRequest request = new LoginRequest("notfound@imbank.com", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("User Not Found", exception.getMessage());
    }

}
