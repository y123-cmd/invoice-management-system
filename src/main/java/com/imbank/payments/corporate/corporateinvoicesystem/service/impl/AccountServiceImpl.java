package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.AccountMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.utils.AccountNumberGenerator;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        CorporateClient client = clientRepository.findById(accountRequest.getClientId())
                .orElseThrow(() -> new RuntimeException(
                        "Client not found with ID: " + accountRequest.getClientId()
                ));


        String accountNumber = AccountNumberGenerator.generate();

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(client);


        Account savedAccount = accountRepository.save(account);


        return accountMapper.toResponse(savedAccount);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Account not found with ID: " + id
                ));
        return accountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClient_ClientId(clientId)
                .stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest accountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Account not found with ID: " + id
                ));


        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());


        Account updatedAccount = accountRepository.save(account);
        return accountMapper.toResponse(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Account not found with ID: " + id);
        }
        accountRepository.deleteById(id);
    }
}
