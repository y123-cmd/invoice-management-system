package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.LoginRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.RegisterRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ApiResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AuthResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for registration and login")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(
                ApiResponse.created("User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }
}