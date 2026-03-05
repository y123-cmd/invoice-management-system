package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email is Required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is Required")
    private String password;

    @NotNull(message = "Role is Required")
    private Role role;

    private String phone;
}
