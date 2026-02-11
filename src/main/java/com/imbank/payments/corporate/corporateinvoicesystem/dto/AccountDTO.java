package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;  // ← Add this
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Long clientId;
    private String clientName;
}