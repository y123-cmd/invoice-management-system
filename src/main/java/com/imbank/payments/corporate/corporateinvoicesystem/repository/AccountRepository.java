package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByClient_ClientId(Long clientId);

    List<Account> findByAccountType(AccountType accountType);

    boolean existsByAccountNumber(String accountNumber);
}