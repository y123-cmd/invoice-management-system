package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<CorporateClient, Long>,
        JpaSpecificationExecutor<CorporateClient> {

    Optional<CorporateClient> findByRegistrationNumber(String registrationNumber);
    List<CorporateClient> findByAccountStatus(AccountStatus status);
}