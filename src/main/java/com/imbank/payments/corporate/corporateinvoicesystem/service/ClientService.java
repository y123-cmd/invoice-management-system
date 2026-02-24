package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {

    ClientResponse createClient(ClientRequest request);

    List<ClientResponse> createClientsBatch(List<ClientRequest> requests);

    PagedClientResponse getAllClients(AccountStatus accountStatus, ClientType clientType, String companyName, int page, int size);

    ClientResponse getClientById(Long id);

    void deleteClient(Long id);

    List<ClientResponse> getActiveClients();

    ClientResponse updateClientStatus(Long id, AccountStatus status);

    ClientResponse updateClient(Long id, ClientRequest request);

    ClientResponse findByRegistrationNumber(String registrationNumber);
}