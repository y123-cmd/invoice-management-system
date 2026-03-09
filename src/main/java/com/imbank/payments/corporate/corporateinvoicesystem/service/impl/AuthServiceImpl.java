package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.LoginRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.RegisterRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AuthResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.UserRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already Exists");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User Not Found"));

    String token = jwtUtil.generateToken(user.getEmail());

    return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}
