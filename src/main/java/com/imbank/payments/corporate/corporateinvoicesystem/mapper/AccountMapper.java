package com.imbank.payments.corporate.corporateinvoicesystem.mapper;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountDTO;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import org.springframework.stereotype.Component;


@Component
public class AccountMapper {

    public AccountDTO toDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setClientId(account.getClient().getClientId());
        dto.setClientName(account.getClient().getCompanyName());
        return dto;
    }
}