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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void createClient_ShouldReturnClientResponse_WhenClientCreated() {

        // ARRANGE
        ClientRequest request = new ClientRequest();
        request.setCompanyName("Safaricom PLC");
        request.setEmail("corporate@safaricom.co.ke");
        request.setPhone("+254722000000");
        request.setCreditLimit(new BigDecimal("5000000.00"));
        request.setClientType(ClientType.CORPORATE);
        request.setRegistrationNumber("SAF-2026-001");

        CorporateClient mockEntity = new CorporateClient();
        mockEntity.setCompanyName("Safaricom PLC");
        mockEntity.setEmail("corporate@safaricom.co.ke");
        mockEntity.setAccountStatus(AccountStatus.ACTIVE);

        CorporateClient savedEntity = new CorporateClient();
        savedEntity.setClientId(1L);
        savedEntity.setCompanyName("Safaricom PLC");
        savedEntity.setEmail("corporate@safaricom.co.ke");
        savedEntity.setAccountStatus(AccountStatus.ACTIVE);

        ClientResponse mockResponse = new ClientResponse();
        mockResponse.setClientId(1L);
        mockResponse.setCompanyName("Safaricom PLC");
        mockResponse.setEmail("corporate@safaricom.co.ke");
        mockResponse.setAccountStatus(AccountStatus.ACTIVE);


        when(clientMapper.toEntity(request))
                .thenReturn(mockEntity);


        when(clientRepository.save(mockEntity))
                .thenReturn(savedEntity);


        when(clientMapper.toResponse(savedEntity))
                .thenReturn(mockResponse);


        ClientResponse result = clientService.createClient(request);


        assertNotNull(result);
        assertEquals(1L, result.getClientId());
        assertEquals("Safaricom PLC", result.getCompanyName());
        assertEquals(AccountStatus.ACTIVE, result.getAccountStatus());


        verify(clientMapper).toEntity(request);
        verify(clientRepository).save(mockEntity);
        verify(clientMapper).toResponse(savedEntity);
    }
        @Test
        void getClientById_ShouldReturnClientResponse_WhenClientExists() {
            Long clientId = 1L;

            CorporateClient mockEntity = new CorporateClient();
            mockEntity.setClientId(clientId);
            mockEntity.setCompanyName("Safaricom PLC");
            mockEntity.setEmail("corporate@safaricom.co.ke");
            mockEntity.setAccountStatus(AccountStatus.ACTIVE);

            ClientResponse mockResponse = new ClientResponse();
            mockResponse.setClientId(clientId);
            mockResponse.setCompanyName("Safaricom PLC");
            mockResponse.setEmail("corporate@safaricom.co.ke");


            when(clientRepository.findById(clientId))
                    .thenReturn(Optional.of(mockEntity));

            when(clientMapper.toResponse(mockEntity))
                    .thenReturn(mockResponse);

            ClientResponse result = clientService.getClientById(clientId);

            assertNotNull(result);
            assertEquals(clientId, result.getClientId());
            assertEquals("Safaricom PLC", result.getCompanyName());

            verify(clientRepository).findById(clientId);
            verify(clientMapper).toResponse(mockEntity);
        }
    @Test
    void deleteClient_ShouldSoftDeleteClient_WhenClientExists() {

        Long clientId = 1L;

        CorporateClient mockClient = new CorporateClient();
        mockClient.setClientId(clientId);
        mockClient.setCompanyName("Safaricom PLC");
        mockClient.setDeleted(false);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(mockClient));
        when(clientRepository.save(mockClient))
                .thenReturn(mockClient);


        clientService.deleteClient(clientId);

        assertTrue(mockClient.isDeleted());


        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(mockClient);
    }
    @Test
    void deleteClient_ShouldThrowException_WhenClientNotFound() {

        Long clientId = 999L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,
                () -> clientService.deleteClient(clientId));


        verify(clientRepository).findById(clientId);
        verify(clientRepository, never()).save(any());
    }
    @Test
    void getClientById_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.getClientById(999L));

        verify(clientRepository).findById(999L);
    }
    @Test
    void updateClient_ShouldReturnUpdatedResponse_WhenClientExists() {
        Long clientId = 1L;
        ClientRequest request = new ClientRequest();
        request.setCompanyName("Updated Safaricom");
        request.setEmail("updated@safaricom.co.ke");
        request.setPhone("+254722000000");
        request.setCreditLimit(new BigDecimal("6000000.00"));
        request.setClientType(ClientType.CORPORATE);
        request.setRegistrationNumber("SAF-2026-001");

        CorporateClient existingEntity = new CorporateClient();
        existingEntity.setClientId(clientId);
        existingEntity.setCompanyName("Safaricom PLC");

        ClientResponse mockResponse = new ClientResponse();
        mockResponse.setClientId(clientId);
        mockResponse.setCompanyName("Updated Safaricom");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingEntity));
        when(clientRepository.save(existingEntity)).thenReturn(existingEntity);
        when(clientMapper.toResponse(existingEntity)).thenReturn(mockResponse);

        ClientResponse result = clientService.updateClient(clientId, request);

        assertNotNull(result);
        assertEquals("Updated Safaricom", result.getCompanyName());
        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(existingEntity);
    }
    @Test
    void updateClient_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.updateClient(999L, new ClientRequest()));

        verify(clientRepository).findById(999L);
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getActiveClients_ShouldReturnActiveClients() {
        CorporateClient activeClient = new CorporateClient();
        activeClient.setClientId(1L);
        activeClient.setCompanyName("Safaricom PLC");
        activeClient.setAccountStatus(AccountStatus.ACTIVE);

        ClientResponse mockResponse = new ClientResponse();
        mockResponse.setClientId(1L);
        mockResponse.setCompanyName("Safaricom PLC");

        when(clientRepository.findByAccountStatus(AccountStatus.ACTIVE))
                .thenReturn(List.of(activeClient));
        when(clientMapper.toResponse(activeClient)).thenReturn(mockResponse);

        List<ClientResponse> result = clientService.getActiveClients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Safaricom PLC", result.get(0).getCompanyName());
        verify(clientRepository).findByAccountStatus(AccountStatus.ACTIVE);
    }
    @Test
    void updateClientStatus_ShouldReturnUpdatedResponse_WhenClientExists() {
        Long clientId = 1L;
        CorporateClient existingEntity = new CorporateClient();
        existingEntity.setClientId(clientId);
        existingEntity.setAccountStatus(AccountStatus.ACTIVE);

        ClientResponse mockResponse = new ClientResponse();
        mockResponse.setClientId(clientId);
        mockResponse.setAccountStatus(AccountStatus.SUSPENDED);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingEntity));
        when(clientRepository.save(existingEntity)).thenReturn(existingEntity);
        when(clientMapper.toResponse(existingEntity)).thenReturn(mockResponse);

        ClientResponse result = clientService.updateClientStatus(clientId, AccountStatus.SUSPENDED);

        assertNotNull(result);
        assertEquals(AccountStatus.SUSPENDED, result.getAccountStatus());
        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(existingEntity);
    }
    @Test
    void updateClientStatus_ShouldThrowException_WhenClientNotFound() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.updateClientStatus(999L, AccountStatus.SUSPENDED));

        verify(clientRepository).findById(999L);
        verify(clientRepository, never()).save(any());
    }
    @Test
    void findByRegistrationNumber_ShouldReturnClient_WhenExists() {
        CorporateClient entity = new CorporateClient();
        entity.setClientId(1L);
        entity.setRegistrationNumber("SAF-2026-001");

        ClientResponse mockResponse = new ClientResponse();
        mockResponse.setClientId(1L);
        mockResponse.setRegistrationNumber("SAF-2026-001");

        when(clientRepository.findByRegistrationNumber("SAF-2026-001"))
                .thenReturn(Optional.of(entity));
        when(clientMapper.toResponse(entity)).thenReturn(mockResponse);

        ClientResponse result = clientService.findByRegistrationNumber("SAF-2026-001");

        assertNotNull(result);
        assertEquals("SAF-2026-001", result.getRegistrationNumber());
        verify(clientRepository).findByRegistrationNumber("SAF-2026-001");
    }

    @Test
    void findByRegistrationNumber_ShouldThrowException_WhenNotFound() {
        when(clientRepository.findByRegistrationNumber("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientService.findByRegistrationNumber("UNKNOWN"));

        verify(clientRepository).findByRegistrationNumber("UNKNOWN");
    }
    @Test
    void getAllClients_ShouldThrowException_WhenPageIsLessThanOne() {
        assertThrows(IllegalArgumentException.class,
                () -> clientService.getAllClients(null, null, null, 0, 10));
    }
    @Test
    void createClientsBatch_ShouldThrowException_WhenEmailIsDuplicate() {
        ClientRequest request = new ClientRequest();
        request.setCompanyName("Safaricom PLC");
        request.setEmail("corporate@safaricom.co.ke");
        request.setRegistrationNumber("SAF-2026-001");

        when(clientRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> clientService.createClientsBatch(List.of(request)));
    }
    @Test
    void createClientsBatch_ShouldThrowException_WhenRegistrationNumberIsDuplicate() {
        ClientRequest request = new ClientRequest();
        request.setCompanyName("Safaricom PLC");
        request.setEmail("corporate@safaricom.co.ke");
        request.setRegistrationNumber("SAF-2026-001");

        when(clientRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(clientRepository.existsByRegistrationNumber(request.getRegistrationNumber()))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> clientService.createClientsBatch(List.of(request)));
    }
}
