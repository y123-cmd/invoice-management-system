package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.PagedAccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.Pagination;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Creating account - ClientId: {}, Type: {}",
                    accountRequest.getClientId(),
                    accountRequest.getAccountType());

            // Step 1: Check client exists
            CorporateClient client = clientRepository.findById(accountRequest.getClientId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Client", "id", accountRequest.getClientId()
                    ));

            log.debug("Client found: {}", client.getCompanyName());

            // Step 2: Validate no duplicate
            validateNoDuplicateAccount(accountRequest.getClientId(), accountRequest.getAccountType());

            log.debug("Duplicate validation passed");

            // Step 3: Generate account number
            String accountNumber = AccountNumberGenerator.generate();

            log.debug("Generated account number: {}", accountNumber);

            // Step 4: Build account
            Account account = new Account();
            account.setAccountNumber(accountNumber);
            account.setAccountType(accountRequest.getAccountType());
            account.setBalance(accountRequest.getBalance());
            account.setStatus(AccountStatus.ACTIVE);
            account.setClient(client);

            // Step 5: Save to database
            Account savedAccount = accountRepository.save(account);

            log.info("Account saved to database - ID: {}, Number: {}",
                    savedAccount.getAccountId(),
                    savedAccount.getAccountNumber());

            // Step 6: Convert to response
            AccountResponse response = accountMapper.toResponse(savedAccount);

            log.info("SUCCESS: Account created - Number: {}, Client: {}",
                    response.getAccountNumber(),
                    response.getClientName());

            return response;

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Client not found - ClientId: {}",
                    accountRequest.getClientId());
            throw e;

        } catch (DuplicateResourceException e) {
            log.error("FAILED: Duplicate account - ClientId: {}, Type: {}, Reason: {}",
                    accountRequest.getClientId(),
                    accountRequest.getAccountType(),
                    e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Unexpected error creating account - ClientId: {}, Error: {}",
                    accountRequest.getClientId(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Unexpected error creating account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Account creation attempt completed in {}ms for ClientId: {}",
                    duration,
                    accountRequest.getClientId());
        }
    }

    private void validateNoDuplicateAccount(Long clientId, AccountType accountType) {

        try {
            log.debug("Validating duplicate account - ClientId: {}, Type: {}", clientId, accountType);

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

            log.debug("No duplicate found for ClientId: {}, Type: {}", clientId, accountType);

        } catch (DuplicateResourceException e) {
            log.error("Duplicate validation failed - ClientId: {}, Type: {}", clientId, accountType);
            throw e;
        }
    }

    @Override
    public PagedAccountResponse getAllAccounts(AccountType accountType, AccountStatus status, int page, int size) {

        Pageable pageable = PageRequest.of(page -1, size);


        Specification<Account> spec = Specification.where(
                AccountSpecification.hasAccountType(accountType)
        ).and(AccountSpecification.hasStatus(status));

        Page<Account> accountPage = accountRepository.findAll(spec, pageable);


        List<AccountResponse> accountResponses = accountPage.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                accountPage.getSize(),
                accountPage.getNumber() + 1,
                (int) accountPage.getTotalElements(),
                accountPage.getTotalPages()
        );

        return new PagedAccountResponse(
                200,
                "OK",
                accountResponses,
                pagination
        );
    }

    @Override
    public AccountResponse getAccountById(Long id) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Fetching account by ID - ID: {}", id);

            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Account", "id", id
                    ));
            if (account.isDeleted()) {
                throw new ResourceNotFoundException(
                        "Account", "id", id
                );
            }

            log.info("SUCCESS: Account found - ID: {}, Number: {}",
                    account.getAccountId(),
                    account.getAccountNumber());

            return accountMapper.toResponse(account);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Account not found - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error fetching account - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Account fetch by ID completed in {}ms for ID: {}", duration, id);
        }
    }

    @Override
    public List<AccountResponse> getAccountsByClientId(Long clientId) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Fetching accounts for client - ClientId: {}", clientId);

            List<Account> accounts = accountRepository.findByClient_ClientId(clientId);

            log.info("SUCCESS: Found {} accounts for ClientId: {}", accounts.size(), clientId);

            return accounts.stream()
                    .map(accountMapper::toResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("FAILED: Error fetching accounts for client - ClientId: {}, Error: {}",
                    clientId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch accounts for client", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Fetch accounts by ClientId completed in {}ms for ClientId: {}",
                    duration, clientId);
        }
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest accountRequest) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Updating account - ID: {}", id);

            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Account", "id", id
                    ));

            log.debug("Account found for update - Number: {}", account.getAccountNumber());

            // Update fields
            account.setAccountType(accountRequest.getAccountType());
            account.setBalance(accountRequest.getBalance());

            // Save changes
            Account updatedAccount = accountRepository.save(account);

            log.info("SUCCESS: Account updated - ID: {}, Number: {}",
                    updatedAccount.getAccountId(),
                    updatedAccount.getAccountNumber());

            return accountMapper.toResponse(updatedAccount);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Account not found for update - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error updating account - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to update account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Account update completed in {}ms for ID: {}", duration, id);
        }
    }
    @Override
    @Transactional
    public void deleteAccount(Long id) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Soft deleting account - ID: {}", id);

            // Find account
            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Account not found for deletion - ID: {}", id);
                        return new ResourceNotFoundException("Account", "id", id);
                    });

            // Soft delete - just mark as deleted!
            account.setDeleted(true);
            accountRepository.save(account);

            log.info("SUCCESS: Account soft deleted - ID: {}, Account Number: {}",
                    id, account.getAccountNumber());

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Account not found for deletion - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error deleting account - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Account deletion completed in {}ms for ID: {}", duration, id);
        }
    }

    @Override
    @Transactional
    public AccountResponse updateAccountStatus(Long id, AccountStatus status) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Updating account status - ID: {}, New Status: {}", id, status);

            Account account = accountRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));

            log.debug("Account found - Number: {}, Old Status: {}",
                    account.getAccountNumber(),
                    account.getStatus());

            account.setStatus(status);
            Account updatedAccount = accountRepository.save(account);

            log.info("SUCCESS: Account status updated - ID: {}, Number: {}, New Status: {}",
                    updatedAccount.getAccountId(),
                    updatedAccount.getAccountNumber(),
                    updatedAccount.getStatus());

            return accountMapper.toResponse(updatedAccount);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Account not found for status update - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error updating account status - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to update account status", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Account status update completed in {}ms for ID: {}", duration, id);
        }
    }
    @Override
    @Transactional
    public List<AccountResponse> createAccountsBatch(List<AccountRequest> accountRequests) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Creating {} accounts in batch", accountRequests.size());

            List<AccountResponse> responses = new ArrayList<>();

            for (AccountRequest request : accountRequests) {
                try {
                    AccountResponse created = createAccount(request);
                    responses.add(created);

                } catch (ResourceNotFoundException | DuplicateResourceException e) {
                    log.error("FAILED: Batch creation - Account for ClientId: {} failed - {}",
                            request.getClientId(),
                            e.getMessage());
                }
            }

            log.info("SUCCESS: Batch created {} out of {} accounts",
                    responses.size(),
                    accountRequests.size());

            return responses;

        } catch (Exception e) {
            log.error("FAILED: Batch creation error - {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create accounts in batch", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Batch account creation completed in {}ms", duration);
        }
    }
}