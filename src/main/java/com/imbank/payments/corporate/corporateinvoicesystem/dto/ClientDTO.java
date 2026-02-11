package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientDTO {
    private Long clientId;


    @NotNull(message = "client type is required")
    private ClientType clientType;


    @NotBlank(message = "company name required")
    @Size(max = 255, message = "company cannot exceed 255 characters")
    private String companyName;


    @Size(max = 100, message = "Registration number must not exceed 100 characters")
    private String registrationNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "email should not exceed 255 characters")
    private String email;

    @Size(max = 20, message = "phone number should not exceed 20 characters")
    private String phone;

    @NotNull(message = "credit limit is required")
    @DecimalMin(value = "0.0", message = "credit limit must be positive")
    private BigDecimal creditLimit;

    @NotNull(message = "account status is required")
    private AccountStatus accountStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}






