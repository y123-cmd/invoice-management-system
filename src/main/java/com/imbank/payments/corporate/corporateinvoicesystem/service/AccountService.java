package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(AccountRequest accountRequest);

    Page<AccountResponse>getAllAccounts(AccountType accountType,AccountStatus accountStatus,int page, int size);

    AccountResponse getAccountById(Long id);

    List<AccountResponse> getAccountsByClientId(Long clientId);

    AccountResponse updateAccount(Long id, AccountRequest accountRequest);

    AccountResponse updateAccountStatus(Long id, AccountStatus status);

    List<AccountResponse> createAccountsBatch(List<AccountRequest> accountRequests);

    void deleteAccount(Long id);
}