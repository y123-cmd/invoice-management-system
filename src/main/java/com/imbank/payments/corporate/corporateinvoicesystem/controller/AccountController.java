package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;//calling interface

    @PostMapping("/account")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountRequest accountRequest) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccounts(
            @RequestBody List<AccountRequest> accountRequest) {
        return new ResponseEntity<>("Accounts Created", HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<Page<AccountResponse>> getAllAccounts(
            @RequestParam(required = false) AccountType accountType,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AccountResponse> accounts = accountService.getAllAccounts(
                accountType,
                status,
                page,
                size
        );

        return ResponseEntity.ok(accounts);
    }



    @GetMapping("/account/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }


    @GetMapping("/accounts/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClientId(
            @PathVariable Long clientId) {

        List<AccountResponse> accounts = accountService.getAccountsByClientId(clientId);
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/account/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequest accountRequest) {

        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok(updatedAccount);
    }


    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/account/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        AccountResponse updated = accountService.updateAccountStatus(id, AccountStatus.valueOf(status));
        return ResponseEntity.ok(updated);
    }
}