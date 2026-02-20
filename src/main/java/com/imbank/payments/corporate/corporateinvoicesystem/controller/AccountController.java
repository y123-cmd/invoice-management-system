package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.PagedAccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse created = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PostMapping("/batch")
    public ResponseEntity<List<AccountResponse>> createAccountsBatch(
            @Valid @RequestBody List<AccountRequest> accountRequests) {

        List<AccountResponse> created = accountService.createAccountsBatch(accountRequests);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    // NEW
    @GetMapping
    public ResponseEntity<PagedAccountResponse> getAllAccounts(
            @RequestParam(required = false) AccountType accountType,
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PagedAccountResponse pagedResponse = accountService.getAllAccounts(
                accountType,
                status,
                page,
                size
        );

        return ResponseEntity.ok(pagedResponse);
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
            @Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok(updatedAccount);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        AccountResponse updated = accountService.updateAccountStatus(id, AccountStatus.valueOf(status));
        return ResponseEntity.ok(updated);
    }
}