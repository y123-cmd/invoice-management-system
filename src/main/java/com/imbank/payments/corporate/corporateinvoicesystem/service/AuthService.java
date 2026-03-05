package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.LoginRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.RegisterRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
