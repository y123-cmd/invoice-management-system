package com.imbank.payments.corporate.corporateinvoicesystem.dto.response;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private Role role;
}
