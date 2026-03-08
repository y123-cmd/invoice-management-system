package com.imbank.payments.corporate.corporateinvoicesystem.repository;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User buildUser() {
        User user = new User();
        user.setEmail("yvonne@imbank.com");
        user.setPassword("password123");
        user.setPhone("0712345678");
        user.setRole(Role.USER);
        return user;
    }

    @Test
    void shouldReturnUserWhenEmailExists() {
        userRepository.save(buildUser());

        Optional<User> result = userRepository.findByEmail("yvonne@imbank.com");

        assertTrue(result.isPresent());
        assertEquals("yvonne@imbank.com", result.get().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenEmailDoesNotExist() {
        Optional<User> result = userRepository.findByEmail("notfound@imbank.com");

        assertFalse(result.isPresent());
    }
}