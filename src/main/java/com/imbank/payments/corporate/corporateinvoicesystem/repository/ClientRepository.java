package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<CorporateClient, Long>,
        JpaSpecificationExecutor<CorporateClient> {

    @Query("SELECT c FROM CorporateClient c WHERE c.deleted = false")
    List<CorporateClient> findAll();

    @Query("SELECT c FROM CorporateClient c WHERE c.clientId = :id AND c.deleted = false")
    Optional<CorporateClient> findById(@Param("id") Long id);


    @Query("SELECT c FROM CorporateClient c WHERE c.deleted = false AND c.accountStatus = :accountStatus")
    List<CorporateClient> findByAccountStatus(@Param("accountStatus") AccountStatus accountStatus);

    @Query("SELECT c FROM CorporateClient c WHERE c.deleted = false AND c.registrationNumber = :registrationNumber")
    Optional<CorporateClient> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CorporateClient c WHERE c.deleted = false AND c.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CorporateClient c WHERE c.deleted = false AND c.registrationNumber = :registrationNumber")
    boolean existsByRegistrationNumber(@Param("registrationNumber") String registrationNumber);
}