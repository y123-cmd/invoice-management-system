package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class AccountRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AccountRepository accountRepository;

    private CorporateClient savedClient;

    @BeforeEach
    void setUp() {
        CorporateClient client = new CorporateClient();
        client.setCompanyName("Test Company");
        client.setEmail("test@gmail.com");
        client.setRegistrationNumber("SAF-2026-001");
        client.setCreditLimit(new BigDecimal("1000000.00"));
        client.setAccountStatus(AccountStatus.ACTIVE);
        client.setClientType(ClientType.CORPORATE);
        client.setDeleted(false);
        savedClient = testEntityManager.persist(client);
        testEntityManager.flush();
    }

    @Test
    void findById_ShouldReturnAccount_WhenAccountExists() {
        Account account = new Account();
        account.setAccountNumber("ACC12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(savedClient);
        account.setDeleted(false);
        testEntityManager.persist(account);
        testEntityManager.flush();

        Optional<Account> found = accountRepository.findById(account.getAccountId());

        assertTrue(found.isPresent());
        assertEquals("ACC12345", found.get().getAccountNumber());
        assertEquals(AccountType.SAVINGS, found.get().getAccountType());
    }

    @Test
    void findByClient_ClientId_ShouldReturnAccounts_WhenAccountExists() {
        Account account = new Account();
        account.setAccountNumber("ACC12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(savedClient);
        account.setDeleted(false);
        testEntityManager.persist(account);

        Account account1 = new Account();
        account1.setAccountNumber("ACC9085623");
        account1.setAccountType(AccountType.FIXED_DEPOSIT);
        account1.setBalance(new BigDecimal("50000.00"));
        account1.setStatus(AccountStatus.ACTIVE);
        account1.setClient(savedClient);
        account1.setDeleted(false);
        testEntityManager.persist(account1);
        testEntityManager.flush();

        List<Account> accounts = accountRepository.findByClient_ClientId(savedClient.getClientId());

        assertNotNull(accounts);
        assertEquals(2, accounts.size());

        List<String> accountNumbers = accounts.stream()
                .map(Account::getAccountNumber)
                .toList();
        assertTrue(accountNumbers.contains("ACC12345"));
        assertTrue(accountNumbers.contains("ACC9085623"));
    }

    @Test
    void findByClient_ClientId_ShouldNotReturnDeletedAccounts() {
        Account account = new Account();
        account.setAccountNumber("ACC745690");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(savedClient);
        account.setDeleted(true);
        testEntityManager.persist(account);
        testEntityManager.flush();

        List<Account> accounts = accountRepository.findByClient_ClientId(savedClient.getClientId());

        assertNotNull(accounts);
        assertEquals(0, accounts.size());
    }

    @Test
    void existsByClient_ClientIdAndAccountType_ShouldReturnTrue_WhenAccountExists() {
        Account account = new Account();
        account.setAccountNumber("ACC12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(savedClient);
        account.setDeleted(false);
        testEntityManager.persist(account);
        testEntityManager.flush();

        boolean exists = accountRepository.existsByClient_ClientIdAndAccountType(
                savedClient.getClientId(), AccountType.SAVINGS);
        assertTrue(exists);
    }

    @Test
    void findByClient_ClientIdAndAccountType_ShouldReturnEmpty_WhenAccountTypeDoesNotExist() {
        Account account = new Account();
        account.setAccountNumber("ACC12345");
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setClient(savedClient);
        account.setDeleted(false);
        testEntityManager.persist(account);
        testEntityManager.flush();

        Optional<Account> found = accountRepository.findByClient_ClientIdAndAccountType(
                savedClient.getClientId(), AccountType.CURRENT);
        assertFalse(found.isPresent());
    }
}