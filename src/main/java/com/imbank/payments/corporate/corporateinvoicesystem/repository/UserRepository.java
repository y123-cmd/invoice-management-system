package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByEmail(String email);

}
