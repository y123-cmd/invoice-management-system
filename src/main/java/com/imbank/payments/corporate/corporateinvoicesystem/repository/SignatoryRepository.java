package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignatoryRepository extends JpaRepository<Signatory, Long> {


    Optional<Signatory> findByEmail(String email);

    Optional<Signatory> findByIdNumber(String idNumber);

    boolean existsByEmail(String email);

    boolean existsByIdNumber(String idNumber);
}