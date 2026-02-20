package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;

import java.util.List;

public interface ClientService {

    ClientResponse createClient(ClientRequest request);

    List<ClientResponse> createClientsBatch(List<ClientRequest> requests);

    List<ClientResponse> getAllClients(AccountStatus accountStatus, ClientType clientType,String companyName);

    ClientResponse getClientById(Long id);

    void deleteClient(Long id);

    List<ClientResponse> getActiveClients();

    ClientResponse updateClientStatus(Long id, AccountStatus status);

    ClientResponse updateClient(Long id, ClientRequest request);

    ClientResponse findByRegistrationNumber(String registrationNumber);
}