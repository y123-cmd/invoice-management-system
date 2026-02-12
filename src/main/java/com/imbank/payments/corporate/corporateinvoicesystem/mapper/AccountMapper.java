package com.imbank.payments.corporate.corporateinvoicesystem.mapper;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {


    public AccountResponse toResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus());
        response.setClientId(account.getClient().getClientId());
        response.setClientName(account.getClient().getCompanyName());
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}