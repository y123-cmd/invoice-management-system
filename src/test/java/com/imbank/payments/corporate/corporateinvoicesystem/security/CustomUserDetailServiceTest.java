package com.imbank.payments.corporate.corporateinvoicesystem.security;

import com.imbank.payments.corporate.corporateinvoicesystem.entity.Role;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.User;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturnUsersWhenLoadUsersByUsername(){
        User user = new User();
        user.setEmail("yvonnebosire@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        when(userRepository.findByEmail("yvonnebosire@gmail.com")).thenReturn(Optional.of(user));

       UserDetails result = customUserDetailsService.loadUserByUsername("yvonnebosire@gmail.com");

       assertNotNull(result);
        assertEquals("yvonnebosire@gmail.com",result.getUsername());
    }
    @Test
    void shouldThrowExceptionWhenUserNotFound(){

        when(userRepository.findByEmail("yvonnebosire@gmail.com")).thenReturn
                (Optional.empty());
        assertThrows(UsernameNotFoundException.class,()->{

            customUserDetailsService.loadUserByUsername("yvonnebosire@gmail.com");
        });
    }
}
