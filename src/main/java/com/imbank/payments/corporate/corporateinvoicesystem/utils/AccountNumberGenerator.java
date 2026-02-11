package com.imbank.payments.corporate.corporateinvoicesystem.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccountNumberGenerator {

    public static String generate() {

        String uuid = UUID.randomUUID().toString().replace("-", "");

        return "ACC" + uuid.substring(0, 12).toUpperCase();
    }
}