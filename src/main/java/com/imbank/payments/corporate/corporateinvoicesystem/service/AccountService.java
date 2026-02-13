package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    List<AccountResponse> getAllAccounts(AccountType accountType, AccountStatus accountStatus);

    AccountResponse getAccountById(Long id);

    List<AccountResponse> getAccountsByClientId(Long clientId);

    AccountResponse updateAccount(Long id, AccountRequest accountRequest);

    AccountResponse updateAccountStatus(Long id, AccountStatus status);

    void deleteAccount(Long id);
}