package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.SignatoryMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.SignatoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignatoryServiceImplTest {
    @Mock
    private SignatoryRepository signatoryRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private SignatoryMapper signatoryMapper;
    @InjectMocks
    private SignatoryServiceImpl signatoryService;

    private SignatoryRequest buildRequest() {
        return new SignatoryRequest(
                "John Doe",
                "CEO",
                "john@safaricom.co.ke",
                "+254722000000",
                "ID001"
        );
    }
        private Signatory buildSignatory() {
            Signatory signatory = new Signatory();
            signatory.setSignatoryId(1L);
            signatory.setName("John Doe");
            signatory.setEmail("john@safaricom.co.ke");
            signatory.setIdNumber("ID001");
            return signatory;
        }
    private SignatoryResponse buildResponse() {
        SignatoryResponse response = new SignatoryResponse();
        response.setSignatoryId(1L);
        response.setName("John Doe");
        response.setEmail("john@safaricom.co.ke");
        return response;
    }
    @Test
    void createSignatory_ShouldReturnResponse_WhenCreatedSuccessfully() {
        SignatoryRequest request = buildRequest();
        Signatory signatory = buildSignatory();
        SignatoryResponse response = buildResponse();

        when(signatoryRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(signatoryRepository.existsByIdNumber(request.getIdNumber())).thenReturn(false);
        when(signatoryMapper.toEntity(request)).thenReturn(signatory);
        when(signatoryRepository.save(signatory)).thenReturn(signatory);
        when(signatoryMapper.toResponse(signatory)).thenReturn(response);

        SignatoryResponse result = signatoryService.createSignatory(request);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(signatoryRepository).save(signatory);
    }
    @Test
    void createSignatory_ShouldThrowException_WhenEmailIsDuplicate() {
        SignatoryRequest request = buildRequest();

        when(signatoryRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> signatoryService.createSignatory(request));

        verify(signatoryRepository, never()).save(any());
    }
    @Test
    void getSignatoryById_ShouldReturnResponse_WhenFound() {
        Signatory signatory = buildSignatory();
        SignatoryResponse response = buildResponse();

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(signatoryMapper.toResponse(signatory)).thenReturn(response);

        SignatoryResponse result = signatoryService.getSignatoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getSignatoryId());
        verify(signatoryRepository).findById(1L);
    }
    @Test
    void getSignatoryById_ShouldThrowException_WhenNotFound() {
        when(signatoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signatoryService.getSignatoryById(999L));

        verify(signatoryRepository).findById(999L);
    }
    @Test
    void updateSignatory_ShouldReturnUpdatedResponse_WhenSuccessful() {
        SignatoryRequest request = buildRequest();
        Signatory signatory = buildSignatory();
        SignatoryResponse response = buildResponse();

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(signatoryRepository.save(signatory)).thenReturn(signatory);
        when(signatoryMapper.toResponse(signatory)).thenReturn(response);

        SignatoryResponse result = signatoryService.updateSignatory(1L, request);

        assertNotNull(result);
        verify(signatoryRepository).save(signatory);
    }
    @Test
    void updateSignatory_ShouldThrowException_WhenNotFound() {
        when(signatoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signatoryService.updateSignatory(999L, buildRequest()));

        verify(signatoryRepository, never()).save(any());
    }
    @Test
    void deleteSignatory_ShouldSoftDelete_WhenFound() {
        Signatory signatory = buildSignatory();
        signatory.setDeleted(false);

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(signatoryRepository.save(signatory)).thenReturn(signatory);

        signatoryService.deleteSignatory(1L);

        assertTrue(signatory.isDeleted());
        verify(signatoryRepository).save(signatory);
    }
    @Test
    void deleteSignatory_ShouldThrowException_WhenNotFound() {
        when(signatoryRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signatoryService.deleteSignatory(999L));

        verify(signatoryRepository, never()).save(any());
    }
    @Test
    void addSignatoryToAccount_ShouldAddSuccessfully_WhenNotAlreadyAdded() {
        Signatory signatory = buildSignatory();
        Account account = new Account();
        account.setAccountId(1L);
        account.setSignatories(new ArrayList<>());

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        signatoryService.addSignatoryToAccount(1L, 1L);

        assertTrue(account.getSignatories().contains(signatory));
        verify(accountRepository).save(account);
    }
    @Test
    void addSignatoryToAccount_ShouldThrowException_WhenAlreadyAdded() {
        Signatory signatory = buildSignatory();
        Account account = new Account();
        account.setAccountId(1L);
        account.setSignatories(new ArrayList<>(List.of(signatory)));

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(DuplicateResourceException.class,
                () -> signatoryService.addSignatoryToAccount(1L, 1L));

        verify(accountRepository, never()).save(any());
    }
    @Test
    void removeSignatoryFromAccount_ShouldRemoveSuccessfully_WhenAssociated() {
        Signatory signatory = buildSignatory();
        Account account = new Account();
        account.setAccountId(1L);
        account.setSignatories(new ArrayList<>(List.of(signatory)));

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        signatoryService.removeSignatoryFromAccount(1L, 1L);

        assertFalse(account.getSignatories().contains(signatory));
        verify(accountRepository).save(account);
    }
    @Test
    void removeSignatoryFromAccount_ShouldThrowException_WhenNotAssociated() {
        Signatory signatory = buildSignatory();
        Account account = new Account();
        account.setAccountId(1L);
        account.setSignatories(new ArrayList<>());

        when(signatoryRepository.findById(1L)).thenReturn(Optional.of(signatory));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(ResourceNotFoundException.class,
                () -> signatoryService.removeSignatoryFromAccount(1L, 1L));

        verify(accountRepository, never()).save(any());
    }
    @Test
    void getSignatoriesByAccount_ShouldReturnList_WhenAccountExists() {
        Signatory signatory = buildSignatory();
        SignatoryResponse response = buildResponse();
        Account account = new Account();
        account.setAccountId(1L);
        account.setSignatories(List.of(signatory));

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(signatoryMapper.toResponse(signatory)).thenReturn(response);

        List<SignatoryResponse> result = signatoryService.getSignatoriesByAccount(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(accountRepository).findById(1L);
    }
    @Test
    void getSignatoriesByAccount_ShouldThrowException_WhenAccountNotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> signatoryService.getSignatoriesByAccount(999L));

        verify(accountRepository).findById(999L);
    }

    }

