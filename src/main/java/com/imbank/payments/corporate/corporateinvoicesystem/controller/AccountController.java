package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountStatusUpdateRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedAccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@Tag(name = "Account Management", description = "APIs for managing corporate bank accounts")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create a new account", description = "Creates a new bank account for a corporate client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "409", description = "Duplicate account")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse created = accountService.createAccount(accountRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Create multiple accounts", description = "Creates multiple accounts in batch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Accounts created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/batch")
    public ResponseEntity<List<AccountResponse>> createAccountsBatch(
            @Valid @RequestBody List<AccountRequest> accountRequests) {

        List<AccountResponse> created = accountService.createAccountsBatch(accountRequests);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all accounts", description = "Retrieves paginated list of accounts with filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved accounts")
    })
    @GetMapping
    public ResponseEntity<PagedAccountResponse> getAllAccounts(
            @Parameter(description = "Filter by account type") @RequestParam(required = false) AccountType accountType,
            @Parameter(description = "Filter by status") @RequestParam(required = false) AccountStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        PagedAccountResponse pagedResponse = accountService.getAllAccounts(accountType, status, page, size);
        return ResponseEntity.ok(pagedResponse);
    }

    @Operation(summary = "Get account by ID", description = "Retrieves a single account by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @Parameter(description = "Account ID") @PathVariable Long id) {

        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get accounts by client", description = "Retrieves all accounts for a specific client")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClientId(
            @Parameter(description = "Client ID") @PathVariable Long clientId) {

        List<AccountResponse> accounts = accountService.getAccountsByClientId(clientId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Update account", description = "Updates an existing account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account ID") @PathVariable Long id,
            @Valid @RequestBody AccountRequest accountRequest) {

        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok(updatedAccount);
    }

    @Operation(summary = "Delete account", description = "Deletes an account (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted"),
            @ApiResponse(responseCode = "404", description = "Account not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID") @PathVariable Long id) {

        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update account status", description = "Updates only the account status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @Parameter(description = "Account ID") @PathVariable Long id,
            @RequestBody @Valid AccountStatusUpdateRequest request) {
        AccountResponse updated = accountService
                .updateAccountStatus(id, AccountStatus.valueOf(request.getStatus()));
        return ResponseEntity.ok(updated);
    }
}