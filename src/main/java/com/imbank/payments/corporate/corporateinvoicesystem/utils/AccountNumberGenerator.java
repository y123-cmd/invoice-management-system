package com.imbank.payments.corporate.corporateinvoicesystem.utils;

import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private final AccountRepository accountRepository;

    public static String generate() {
        String accountNumber;
        do {
            accountNumber = "ACC" + String.format("%06d", new Random().nextInt(999999));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}