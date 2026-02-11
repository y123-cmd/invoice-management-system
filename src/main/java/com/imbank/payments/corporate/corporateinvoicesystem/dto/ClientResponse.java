package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    private Long clientId;
    private ClientType clientType;
    private String companyName;
    private String registrationNumber;
    private String email;
    private String phone;
    private BigDecimal creditLimit;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}