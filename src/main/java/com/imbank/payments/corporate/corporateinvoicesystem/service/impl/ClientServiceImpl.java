package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.ClientMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll()
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
        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client", "id", id);
        }
        clientRepository.deleteById(id);
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