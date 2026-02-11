package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientDTO;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.ClientMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import org.springframework.stereotype.Service;

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
    public ClientDTO createClient(ClientDTO dto) {
        CorporateClient entity = clientMapper.toEntity(dto);
        CorporateClient savedEntity = clientRepository.save(entity);
        return clientMapper.toDTO(savedEntity);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return clientMapper.toDTO(entity);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setPhone(dto.getPhone());
        entity.setAccountStatus(dto.getAccountStatus());
        entity.setClientType(dto.getClientType());



        CorporateClient savedEntity = clientRepository.save(entity);
        return clientMapper.toDTO(savedEntity);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public List<ClientDTO> getActiveClients() {
        return clientRepository.findByAccountStatus(AccountStatus.ACTIVE)
                .stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO updateClientStatus(Long id, AccountStatus status) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        entity.setAccountStatus(status);
        CorporateClient savedEntity = clientRepository.save(entity);
        return clientMapper.toDTO(savedEntity);
    }

    @Override
    public ClientDTO findByRegistrationNumber(String registrationNumber) {
        CorporateClient entity = clientRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Client not found with registration number: " + registrationNumber));
        return clientMapper.toDTO(entity);
    }
}