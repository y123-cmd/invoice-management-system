package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientDTO;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.ClientRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientDTO createClient(ClientDTO dto) {
        //converts DTO to entity
        CorporateClient entity = new CorporateClient();
        entity.setCompanyName(dto.getCompanyName());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setAccountStatus(dto.getAccountStatus());

        //save entity
        CorporateClient savedEntity = clientRepository.save(entity);

        //converts saved entity back to DTO and return
        return convertToDTO(savedEntity);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return convertToDTO(entity);
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO dto) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        entity.setCompanyName(dto.getCompanyName());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setAccountStatus(dto.getAccountStatus());

        return convertToDTO(clientRepository.save(entity));
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
        return clientRepository.findAll().stream()
                .filter(client -> client.getAccountStatus() == AccountStatus.ACTIVE)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public ClientDTO updateClientStatus(Long id, AccountStatus status) {
        CorporateClient entity = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        entity.setAccountStatus(status);
        return convertToDTO(clientRepository.save(entity));
    }

    @Override
    public ClientDTO findByRegistrationNumber(String registrationNumber) {
        CorporateClient entity = clientRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Client not found with registration number: " + registrationNumber));
        return convertToDTO(entity);
    }
    private ClientDTO convertToDTO(CorporateClient entity) {
        ClientDTO dto = new ClientDTO();
        dto.setClientId(entity.getClientId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setAccountStatus(entity.getAccountStatus());
        dto.setCreatedAt(entity.getCreated_at());
        dto.setUpdatedAt(entity.getUpdated_at());
        return dto;
    }

    }

