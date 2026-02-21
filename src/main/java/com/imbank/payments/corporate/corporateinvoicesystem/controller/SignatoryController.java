package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;

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

@RestController
@RequestMapping("/api/v1/signatories")
@RequiredArgsConstructor
@Tag(name = "Signatory Management", description = "APIs for managing account signatories")
public class SignatoryController {

    private final SignatoryService signatoryService;

    @Operation(summary = "Create signatory", description = "Creates a new account signatory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signatory created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<SignatoryResponse> createSignatory(
            @RequestBody SignatoryRequest request) {

        SignatoryResponse response = signatoryService.createSignatory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Create signatories in batch", description = "Creates multiple signatories at once")
    @PostMapping("/batch")
    public ResponseEntity<List<SignatoryResponse>> createSignatoriesBatch(
            @Valid @RequestBody List<SignatoryRequest> requests) {

        List<SignatoryResponse> responses = signatoryService.createSignatoriesBatch(requests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all signatories", description = "Retrieves all signatories")
    @GetMapping
    public ResponseEntity<List<SignatoryResponse>> getAllSignatories() {
        List<SignatoryResponse> signatories = signatoryService.getAllSignatories();
        return ResponseEntity.ok(signatories);
    }

    @Operation(summary = "Get signatory by ID", description = "Retrieves a single signatory by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signatory found"),
            @ApiResponse(responseCode = "404", description = "Signatory not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SignatoryResponse> getSignatoryById(
            @Parameter(description = "Signatory ID") @PathVariable Long id) {

        SignatoryResponse response = signatoryService.getSignatoryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update signatory", description = "Updates an existing signatory")
    @PutMapping("/{id}")
    public ResponseEntity<SignatoryResponse> updateSignatory(
            @Parameter(description = "Signatory ID") @PathVariable Long id,
            @RequestBody SignatoryRequest request) {

        SignatoryResponse response = signatoryService.updateSignatory(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete signatory", description = "Deletes a signatory")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignatory(
            @Parameter(description = "Signatory ID") @PathVariable Long id) {

        signatoryService.deleteSignatory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add signatory to account", description = "Authorizes a signatory on an account")
    @PostMapping("/{signatoryId}/accounts/{accountId}")
    public ResponseEntity<Void> addSignatoryToAccount(
            @Parameter(description = "Signatory ID") @PathVariable Long signatoryId,
            @Parameter(description = "Account ID") @PathVariable Long accountId) {

        signatoryService.addSignatoryToAccount(signatoryId, accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Remove signatory from account", description = "Removes signatory authorization from an account")
    @DeleteMapping("/{signatoryId}/accounts/{accountId}")
    public ResponseEntity<Void> removeSignatoryFromAccount(
            @Parameter(description = "Signatory ID") @PathVariable Long signatoryId,
            @Parameter(description = "Account ID") @PathVariable Long accountId) {

        signatoryService.removeSignatoryFromAccount(signatoryId, accountId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get signatories by account", description = "Retrieves all signatories for a specific account")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<SignatoryResponse>> getSignatoriesByAccount(
            @Parameter(description = "Account ID") @PathVariable Long accountId) {

        List<SignatoryResponse> signatories = signatoryService.getSignatoriesByAccount(accountId);
        return ResponseEntity.ok(signatories);
    }
}