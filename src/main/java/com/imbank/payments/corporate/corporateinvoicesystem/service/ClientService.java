package com.imbank.payments.corporate.corporateinvoicesystem.service;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientDTO;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;

import java.util.List;

public interface ClientService {
    ClientDTO createClient(ClientDTO dto);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    void deleteClient(Long id);
    List<ClientDTO> getActiveClients();
    ClientDTO updateClientStatus(Long id, AccountStatus status);
    ClientDTO updateClient(Long id, ClientDTO dto);
    ClientDTO findByRegistrationNumber(String registrationNumber);


}
