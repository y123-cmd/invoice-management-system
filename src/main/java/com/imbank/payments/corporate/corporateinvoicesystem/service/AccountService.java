package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long id);

    List<AccountResponse> getAccountsByClientId(Long clientId);

    AccountResponse updateAccount(Long id, AccountRequest accountRequest);

    void deleteAccount(Long id);
}