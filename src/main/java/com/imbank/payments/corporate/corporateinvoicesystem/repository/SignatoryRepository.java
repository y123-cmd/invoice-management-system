package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SignatoryRepository extends JpaRepository<Signatory, Long> {


    @Query("SELECT s FROM Signatory s WHERE s.deleted = false")
    Page<Signatory> findAll(Pageable pageable);

    @Query("SELECT s FROM Signatory s WHERE s.deleted = false")
    List<Signatory> findAll();

    @Query("SELECT s FROM Signatory s WHERE s.signatoryId = :id AND s.deleted = false")
    Optional<Signatory> findById(@Param("id") Long id);

    @Query("SELECT s FROM Signatory s WHERE s.deleted = false AND s.email = :email")
    Optional<Signatory> findByEmail(@Param("email") String email);

    @Query("SELECT s FROM Signatory s WHERE s.deleted = false AND s.idNumber = :idNumber")
    Optional<Signatory> findByIdNumber(@Param("idNumber") String idNumber);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Signatory s WHERE s.deleted = false AND s.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Signatory s WHERE s.deleted = false AND s.idNumber = :idNumber")
    boolean existsByIdNumber(@Param("idNumber") String idNumber);
}