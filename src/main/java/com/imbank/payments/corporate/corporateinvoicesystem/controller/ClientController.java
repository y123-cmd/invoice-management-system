package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/client")
    public ResponseEntity<ClientResponse> createClient(
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse created = clientService.createClient(clientRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @PostMapping("/clients")
    public ResponseEntity<ClientResponse> createClients(
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse created = clientService.createClient(clientRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }


    @GetMapping("/clients")
    public ResponseEntity<List<ClientResponse>> getAllClients(
            @RequestParam(required = false) AccountStatus status,
            @RequestParam(required = false) ClientType clientType,
            @RequestParam(required = false) String companyName) {

        List<ClientResponse> clients = clientService.getAllClients(status, clientType, companyName);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        ClientResponse client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/client/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequest clientRequest) {

        ClientResponse updated = clientService.updateClient(id, clientRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/clients/active")
    public ResponseEntity<List<ClientResponse>> getActiveClients() {
        List<ClientResponse> activeClients = clientService.getActiveClients();
        return ResponseEntity.ok(activeClients);
    }

    @PatchMapping("client/{id}/status")
    public ResponseEntity<ClientResponse> updateClientStatus(
            @PathVariable Long id,
            @RequestBody AccountStatus status) {

        ClientResponse updated = clientService.updateClientStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/client/registration/{regNumber}")
    public ResponseEntity<ClientResponse> findByRegistrationNumber(
            @PathVariable String regNumber) {

        ClientResponse client = clientService.findByRegistrationNumber(regNumber);
        return ResponseEntity.ok(client);
    }
}