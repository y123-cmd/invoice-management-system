package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountDTO;
import java.util.List;

public interface AccountService {  // ← Make sure it says "interface" not "class"

    AccountDTO createAccount(AccountDTO accountDTO);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    List<AccountDTO> getAccountsByClientId(Long clientId);

    AccountDTO updateAccount(Long id, AccountDTO accountDTO);

    void deleteAccount(Long id);
}