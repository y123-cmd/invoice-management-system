package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest accountRequest) {

        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(
            @RequestParam(required = false) AccountType accountType,
            @RequestParam(required = false) AccountStatus status) {

        List<AccountResponse> accounts = accountService.getAllAccounts(accountType, status);
        return ResponseEntity.ok(accounts);
    }



    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClientId(
            @PathVariable Long clientId) {

        List<AccountResponse> accounts = accountService.getAccountsByClientId(clientId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest accountRequest) {

        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok(updatedAccount);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}