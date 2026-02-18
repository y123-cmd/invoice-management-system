package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.AccountMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.specification.AccountSpecification;
import com.imbank.payments.corporate.corporateinvoicesystem.utils.AccountNumberGenerator;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client", "id", accountRequest.getClientId()
                ));


        validateNoDuplicateAccount(accountRequest.getClientId(), accountRequest.getAccountType());


        String accountNumber = AccountNumberGenerator.generate();
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(client);

        Account savedAccount = accountRepository.save(account); //save to database

        return accountMapper.toResponse(savedAccount);
    }

    private void validateNoDuplicateAccount(Long clientId, AccountType accountType) {

        if (accountType == AccountType.CURRENT) {
            boolean accountExists = accountRepository.existsByClient_ClientIdAndAccountType(
                    clientId,
                    accountType
            );

            if (accountExists) {
                throw new DuplicateResourceException(
                        String.format("Client with ID %d already has a %s account. " +
                                        "Only one %s account is allowed per client.",
                                clientId,
                                accountType,
                                accountType)
                );
            }
        }

    }

    @Override
    public Page<AccountResponse> getAllAccounts(AccountType accountType, AccountStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size);

        Specification<Account> spec = Specification.where(
                AccountSpecification.hasAccountType(accountType)
        ).and(AccountSpecification.hasStatus(status));

        Page<Account> accountPage = accountRepository.findAll(spec, pageable);

        return accountPage.map(accountMapper::toResponse);
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "id", id
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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "id", id
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
            throw new ResourceNotFoundException("Account", "id", id);
        }
        accountRepository.deleteById(id);
    }
    @Override
    @Transactional
    public AccountResponse updateAccountStatus(Long id, AccountStatus status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

        account.setStatus(status);
        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toResponse(updatedAccount);
    }
}