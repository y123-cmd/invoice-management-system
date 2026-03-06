package com.imbank.payments.corporate.corporateinvoicesystem.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }
        @Test
    void shouldGenerateToken(){
        String email = "yvonnebosire24@gmail.com";

        String token = jwtUtil.generateToken(email);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        }
        @Test
    void ShouldExtractEmail() {
            String email = "yvonne@gmail.com";
            String token = jwtUtil.generateToken(email);
            String extractedEmail = jwtUtil.extractEmail(token);
            assertEquals(email, extractedEmail);
        }
    @Test
    void shouldReturnTrueWhenTokenIsValid() {
        String email = "yvonne@gmail.com";
        String token = jwtUtil.generateToken(email);

        UserDetails userDetails = new User(email, "password", Collections.emptyList());

        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }
    @Test
    void shouldReturnFalseWhenTokenBelongsToDifferentUser(){
        String token = jwtUtil.generateToken("userA@gmail.com");

        UserDetails userB = new User("userB@gmail.com","password",Collections.emptyList());

        assertFalse(jwtUtil.isTokenValid(token, userB));
    }
    @Test
    void shouldReturnFalseWhenTokenIsExpired(){
        JwtUtil expiredjwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(expiredjwtUtil,"secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(expiredjwtUtil,"expiration",-1000L);

        String email = "yvonnebosire24@gmail.com";
        String token = expiredjwtUtil.generateToken(email);

        UserDetails userDetails = new User(email,"password",Collections.emptyList());

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            expiredjwtUtil.isTokenValid(token, userDetails);
        });
    }
    @Test
    void shouldThrowAnExceptionWhenTokenIsTampered(){
       String email = "yvonnebosirea@gmail.com";
       String token = jwtUtil.generateToken(email);
       String tamperedToken = token + "tampered";

       Exception exception = assertThrows(Exception.class, ()->
               jwtUtil.extractEmail(tamperedToken));

        System.out.println("Actual exception: " + exception.getClass().getName());
        System.out.println("Message: " + exception.getMessage());
    }

}
