package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    private AccountType accountType;
    private BigDecimal balance;
    private Long clientId;
}