package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequest {

    @NotNull(message = "Account Type is Required")
    private AccountType accountType;
    @NotNull(message = "Balance is required")
    private BigDecimal balance;
    @NotNull(message = "client id is required")
    private Long clientId;
}