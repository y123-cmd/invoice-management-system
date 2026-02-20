package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotNull(message = "Client type is required")
    private ClientType clientType;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String companyName;

    @Size(max = 100, message = "Registration number must not exceed 100 characters")
    private String registrationNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email should not exceed 255 characters")
    private String email;

    @Size(max = 20, message = "Phone number should not exceed 20 characters")
    private String phone;

    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.0", message = "Credit limit must be positive")
    private BigDecimal creditLimit;
}