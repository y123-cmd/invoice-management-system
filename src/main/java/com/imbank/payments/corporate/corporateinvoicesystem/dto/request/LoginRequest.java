package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email is Required")
    @NotBlank(message = "Email should be valid")
    private String email;

    @NotBlank(message = "password is Required")
    private String password;

}
