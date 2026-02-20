package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>,
        JpaSpecificationExecutor<Account> {

    @Query("SELECT a FROM Account a WHERE a.deleted = false AND a.client.clientId = :clientId")
    List<Account> findByClient_ClientId(@Param("clientId") Long clientId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.deleted = false AND a.client.clientId = :clientId AND a.accountType = :accountType")
    boolean existsByClient_ClientIdAndAccountType(
            @Param("clientId") Long clientId,
            @Param("accountType") AccountType accountType
    );

    @Query("SELECT a FROM Account a WHERE a.deleted = false AND a.client.clientId = :clientId AND a.accountType = :accountType")
    Optional<Account> findByClient_ClientIdAndAccountType(
            @Param("clientId") Long clientId,
            @Param("accountType") AccountType accountType
    );
}