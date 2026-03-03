package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.AccountServiceImpl;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.AccountMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.utils.AccountNumberGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void getAccountById_ShouldReturnAccount_WhenAccountExists() {


        Long accountId = 1L;

        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setAccountNumber("ACC123456");
        mockAccount.setAccountType(AccountType.SAVINGS);
        mockAccount.setBalance(new BigDecimal("50000.00"));
        mockAccount.setStatus(AccountStatus.ACTIVE);
        mockAccount.setDeleted(false);

        AccountResponse mockResponse = new AccountResponse();
        mockResponse.setAccountId(accountId);
        mockResponse.setAccountNumber("ACC123456");

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(mockAccount));

        when(accountMapper.toResponse(mockAccount))
                .thenReturn(mockResponse);


        AccountResponse result = accountService.getAccountById(accountId);


        assertNotNull(result);
        assertEquals("ACC123456", result.getAccountNumber());


        verify(accountRepository).findById(accountId);
        verify(accountMapper).toResponse(mockAccount);
    }

    @Test
    void getAccountById_ShouldThrowException_WhenAccountNotFound() {

        Long accountId = 999L;

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });

        verify(accountRepository).findById(accountId);
    }

    @Test
    void getAccountById_ShouldThrowException_WhenAccountIsDeleted() {

        Long accountId = 1L;

        Account deletedAccount = new Account();
        deletedAccount.setAccountId(accountId);
        deletedAccount.setDeleted(true);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(deletedAccount));

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });

        verify(accountRepository).findById(accountId);
    }

    @Test
    void createAccount_ShouldReturnAccountResponse_WhenClientExists() {
        long clientId = 1L;
        AccountRequest request = new AccountRequest();
        request.setClientId(clientId);
        request.setAccountType(AccountType.SAVINGS);
        request.setBalance(new BigDecimal("50000.00"));

        CorporateClient mockClient = new CorporateClient();
        mockClient.setClientId(clientId);
        mockClient.setCompanyName("Safaricom PLC");

        Account mockAccount = new Account();
        mockAccount.setAccountId(1L);
        mockAccount.setAccountNumber("ACC123456");
        mockAccount.setAccountType(AccountType.SAVINGS);
        mockAccount.setBalance(new BigDecimal("50000.00"));
        mockAccount.setStatus(AccountStatus.ACTIVE);
        mockAccount.setClient(mockClient);

        AccountResponse mockResponse = new AccountResponse();
        mockResponse.setAccountId(1L);
        mockResponse.setAccountNumber("ACC123456");
        mockResponse.setAccountType(AccountType.SAVINGS);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(mockClient));

        when(accountRepository.save(any(Account.class)))
                .thenReturn(mockAccount);

        when(accountMapper.toResponse(mockAccount))
                .thenReturn(mockResponse);

        AccountResponse result = accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("ACC123456", result.getAccountNumber());
        assertEquals(AccountType.SAVINGS, result.getAccountType());

        verify(clientRepository).findById(clientId);
        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).toResponse(mockAccount);
    }

    @Test
    void createAccount_ShouldThrowException_WhenClientNotFound() {

        Long clientId = 999L;

        AccountRequest request = new AccountRequest();
        request.setClientId(clientId);
        request.setAccountType(AccountType.CURRENT);
        request.setBalance(new BigDecimal("50000.00"));

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.createAccount(request);
        });


        verify(clientRepository).findById(clientId);
        verify(accountRepository, never()).save(any());
    }
    @Test
    void createAccount_ShouldThrowException_WhenDuplicateCurrentAccount() {


        Long clientId = 1L;

        AccountRequest request = new AccountRequest();
        request.setClientId(clientId);
        request.setAccountType(AccountType.CURRENT);
        request.setBalance(new BigDecimal("50000.00"));

        CorporateClient mockClient = new CorporateClient();
        mockClient.setClientId(clientId);
        mockClient.setCompanyName("Safaricom PLC");

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(mockClient));

        when(accountRepository.existsByClient_ClientIdAndAccountType(
                clientId, AccountType.CURRENT))
                .thenReturn(true);


        assertThrows(DuplicateResourceException.class, () -> {
            accountService.createAccount(request);
        });


        verify(clientRepository).findById(clientId);
        verify(accountRepository, never()).save(any());
    }
    @Test
    void deleteAccount_ShouldSoftDelete_WhenAccountExists() {


        Long accountId = 1L;

        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        mockAccount.setAccountNumber("ACC123456");
        mockAccount.setDeleted(false);

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(mockAccount));

        accountService.deleteAccount(accountId);

        assertEquals(true, mockAccount.isDeleted());

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(mockAccount);
    }

    @Test
    void deleteAccount_ShouldThrowException_WhenAccountNotFound() {

        Long accountId = 999L;

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.deleteAccount(accountId);
        });

        verify(accountRepository).findById(accountId);
        verify(accountRepository, never()).save(any()); // ← never saved!
    }

}


