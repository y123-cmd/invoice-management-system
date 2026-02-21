package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;

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
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Client Management", description = "APIs for managing corporate clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Create a new client", description = "Creates a new corporate client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Duplicate client")
    })
    @PostMapping
    public ResponseEntity<ClientResponse> createClients(
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse created = clientService.createClient(clientRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Create clients in batch", description = "Creates multiple clients at once")
    @PostMapping("/batch")
    public ResponseEntity<ClientResponse> createClientsBatch(
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse created = clientService.createClient(clientRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all clients", description = "Retrieves all clients with optional filters")
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients(
            @Parameter(description = "Filter by status") @RequestParam(required = false) AccountStatus status,
            @Parameter(description = "Filter by client type") @RequestParam(required = false) ClientType clientType,
            @Parameter(description = "Filter by company name") @RequestParam(required = false) String companyName) {

        List<ClientResponse> clients = clientService.getAllClients(status, clientType, companyName);
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Get client by ID", description = "Retrieves a single client by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(
            @Parameter(description = "Client ID") @PathVariable Long id) {

        ClientResponse client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Update client", description = "Updates an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @Parameter(description = "Client ID") @PathVariable Long id,
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse updated = clientService.updateClient(id, clientRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete client", description = "Deletes a client (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "Client ID") @PathVariable Long id) {

        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get active clients", description = "Retrieves only active clients")
    @GetMapping("/active")
    public ResponseEntity<List<ClientResponse>> getActiveClients() {
        List<ClientResponse> activeClients = clientService.getActiveClients();
        return ResponseEntity.ok(activeClients);
    }

    @Operation(summary = "Update client status", description = "Updates only the client status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ClientResponse> updateClientStatus(
            @Parameter(description = "Client ID") @PathVariable Long id,
            @RequestBody AccountStatus status) {

        ClientResponse updated = clientService.updateClientStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Find by registration number", description = "Finds a client by their registration number")
    @GetMapping("/registration/{regNumber}")
    public ResponseEntity<ClientResponse> findByRegistrationNumber(
            @Parameter(description = "Registration number") @PathVariable String regNumber) {

        ClientResponse client = clientService.findByRegistrationNumber(regNumber);
        return ResponseEntity.ok(client);
    }
}