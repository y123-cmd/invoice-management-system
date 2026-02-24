package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatoryRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Position is required")
    @Size(max = 100, message = "Position cannot exceed 100 characters")
    private String position;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone number should not exceed 20 characters")
    private String phone;

    @NotBlank(message = "ID number is required")
    @Size(max = 50, message = "ID number cannot exceed 50 characters")
    private String idNumber;
}