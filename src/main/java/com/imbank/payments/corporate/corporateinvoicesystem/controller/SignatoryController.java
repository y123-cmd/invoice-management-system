package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/signatories")
@RequiredArgsConstructor
public class SignatoryController {

    private final SignatoryService signatoryService;


    @PostMapping
    public ResponseEntity<SignatoryResponse> createSignatory(
            @RequestBody SignatoryRequest request) {
        SignatoryResponse response = signatoryService.createSignatory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/batch")
    public ResponseEntity<List<SignatoryResponse>> createSignatoriesBatch(
            @Valid @RequestBody List<SignatoryRequest> requests) {
        List<SignatoryResponse> responses = signatoryService.createSignatoriesBatch(requests);
        return new ResponseEntity<>(responses, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SignatoryResponse>> getAllSignatories() {
        List<SignatoryResponse> signatories = signatoryService.getAllSignatories();
        return ResponseEntity.ok(signatories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignatoryResponse> getSignatoryById(@PathVariable Long id) {
        SignatoryResponse response = signatoryService.getSignatoryById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignatoryResponse> updateSignatory(
            @PathVariable Long id,
            @RequestBody SignatoryRequest request) {
        SignatoryResponse response = signatoryService.updateSignatory(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignatory(@PathVariable Long id) {
        signatoryService.deleteSignatory(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{signatoryId}/accounts/{accountId}")
    public ResponseEntity<Void> addSignatoryToAccount(
            @PathVariable Long signatoryId,
            @PathVariable Long accountId) {
        signatoryService.addSignatoryToAccount(signatoryId, accountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{signatoryId}/accounts/{accountId}")
    public ResponseEntity<Void> removeSignatoryFromAccount(
            @PathVariable Long signatoryId,
            @PathVariable Long accountId) {
        signatoryService.removeSignatoryFromAccount(signatoryId, accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<SignatoryResponse>> getSignatoriesByAccount(
            @PathVariable Long accountId) {
         List<SignatoryResponse> signatories = signatoryService.getSignatoriesByAccount(accountId);
        return ResponseEntity.ok(signatories);
    }
}