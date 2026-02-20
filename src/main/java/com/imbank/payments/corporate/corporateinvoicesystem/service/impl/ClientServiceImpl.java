package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.ClientMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import com.imbank.payments.corporate.corporateinvoicesystem.specification.ClientSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional
    public ClientResponse createClient(ClientRequest request) {

        CorporateClient entity = clientMapper.toEntity(request);


        entity.setAccountStatus(AccountStatus.ACTIVE);


        CorporateClient savedEntity = clientRepository.save(entity);


        return clientMapper.toResponse(savedEntity);
    }
    @Override
    @Transactional
    public List<ClientResponse> createClientsBatch(List<ClientRequest> requests) {

        log.info("Starting batch client creation for {} clients", requests.size());

        List<ClientResponse> responses = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (ClientRequest request : requests) {
            try {
                log.debug("Creating client: {}", request.getCompanyName());

                if (clientRepository.existsByEmail(request.getEmail())) {
                    log.warn("Duplicate email detected: {}", request.getEmail());
                    throw new DuplicateResourceException(
                            "Client with email '" + request.getEmail() + "' already exists"
                    );
                }

                if (clientRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
                    log.warn("Duplicate registration number detected: {}", request.getRegistrationNumber());
                    throw new DuplicateResourceException(
                            "Client with registration number '" + request.getRegistrationNumber() + "' already exists"
                    );
                }

                CorporateClient entity = clientMapper.toEntity(request);

                entity.setAccountStatus(AccountStatus.ACTIVE);

                CorporateClient savedEntity = clientRepository.save(entity);

                responses.add(clientMapper.toResponse(savedEntity));

                successCount++;
                log.info("Client created successfully: {} (Registration: {})",
                        savedEntity.getCompanyName(),
                        savedEntity.getRegistrationNumber()
                );

            } catch (DuplicateResourceException e) {
                failureCount++;
                log.error("Duplicate resource error for client '{}': {}",
                        request.getCompanyName(),
                        e.getMessage()
                );
                throw e;

            } catch (Exception e) {
                failureCount++;
                log.error("Unexpected error creating client '{}': {}",
                        request.getCompanyName(),
                        e.getMessage(),
                        e
                );
                throw new RuntimeException("Failed to create client: " + request.getCompanyName(), e);
            }
        }

        log.info("Batch client creation completed. Success: {}, Failures: {}", successCount, failureCount);

        return responses;
    }

    @Override
    public List<ClientResponse> getAllClients(AccountStatus accountStatus, ClientType clientType, String companyName) {

        Specification<CorporateClient> spec = Specification.where(
                        ClientSpecification.hasAccountStatus(accountStatus))
                .and(ClientSpecification.hasClientType(clientType))
                .and(ClientSpecification.hasCompanyNameContaining(companyName));

        return clientRepository.findAll(spec)
                .stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClientResponse getClientById(Long id) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client", "id", id
                ));
        return clientMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ClientResponse updateClient(Long id, ClientRequest request) {

        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client", "id", id
                ));


        entity.setClientType(request.getClientType());
        entity.setCompanyName(request.getCompanyName());
        entity.setRegistrationNumber(request.getRegistrationNumber());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setCreditLimit(request.getCreditLimit());


        CorporateClient savedEntity = clientRepository.save(entity);


        return clientMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Soft deleting client - ID: {}", id);

            // Find client
            CorporateClient client = clientRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Client not found for deletion - ID: {}", id);
                        return new ResourceNotFoundException("Client", "id", id);
                    });

            // Soft delete - just mark as deleted!
            client.setDeleted(true);
            clientRepository.save(client);

            log.info("SUCCESS: Client soft deleted - ID: {}, Company: {}",
                    id, client.getCompanyName());

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Client not found for deletion - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error deleting client - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete client", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Client deletion completed in {}ms for ID: {}", duration, id);
        }
    }

    @Override
    public List<ClientResponse> getActiveClients() {
        return clientRepository.findByAccountStatus(AccountStatus.ACTIVE)
                .stream()
                .map(clientMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientResponse updateClientStatus(Long id, AccountStatus status) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client", "id", id
                ));

        entity.setAccountStatus(status);
        CorporateClient savedEntity = clientRepository.save(entity);
        return clientMapper.toResponse(savedEntity);
    }

    @Override
    public ClientResponse findByRegistrationNumber(String registrationNumber) {
        CorporateClient entity = clientRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client", "registrationNumber", registrationNumber
                ));
        return clientMapper.toResponse(entity);
    }
}