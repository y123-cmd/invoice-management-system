package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.SignatoryMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.SignatoryRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SignatoryServiceImpl implements SignatoryService {

    private final SignatoryRepository signatoryRepository;
    private final AccountRepository accountRepository;
    private final SignatoryMapper signatoryMapper;

    @Override
    @Transactional
    public SignatoryResponse createSignatory(SignatoryRequest request) {


        if (signatoryRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Signatory with email '" + request.getEmail() + "' already exists"
            );
        }


        if (request.getIdNumber() != null &&
                signatoryRepository.existsByIdNumber(request.getIdNumber())) {
            throw new DuplicateResourceException(
                    "Signatory with ID number '" + request.getIdNumber() + "' already exists"
            );
        }


        Signatory signatory = signatoryMapper.toEntity(request);

        Signatory savedSignatory = signatoryRepository.save(signatory);


        return signatoryMapper.toResponse(savedSignatory);
    }

    @Override
    public List<SignatoryResponse> getAllSignatories() {
        return signatoryRepository.findAll()
                .stream()
                .map(signatoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SignatoryResponse getSignatoryById(Long id) {
        Signatory signatory = signatoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signatory", "id", id
                ));
        return signatoryMapper.toResponse(signatory);
    }

    @Override
    @Transactional
    public SignatoryResponse updateSignatory(Long id, SignatoryRequest request) {

        Signatory signatory = signatoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signatory", "id", id
                ));


        signatory.setName(request.getName());
        signatory.setPosition(request.getPosition());
        signatory.setEmail(request.getEmail());
        signatory.setPhone(request.getPhone());
        signatory.setIdNumber(request.getIdNumber());

        Signatory updatedSignatory = signatoryRepository.save(signatory);


        return signatoryMapper.toResponse(updatedSignatory);
    }

    @Override
    @Transactional
    public void deleteSignatory(Long id) {
        if (!signatoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Signatory", "id", id);
        }
        signatoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addSignatoryToAccount(Long signatoryId, Long accountId) {


        Signatory signatory = signatoryRepository.findById(signatoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signatory", "id", signatoryId
                ));


        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "id", accountId
                ));


        if (account.getSignatories().contains(signatory)) {
            throw new DuplicateResourceException(
                    "Signatory is already authorized on this account"
            );
        }

        account.getSignatories().add(signatory);


        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void removeSignatoryFromAccount(Long signatoryId, Long accountId) {

        Signatory signatory = signatoryRepository.findById(signatoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Signatory", "id", signatoryId
                ));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "id", accountId
                ));

        account.getSignatories().remove(signatory);

        accountRepository.save(account);
    }

    @Override
    public List<SignatoryResponse> getSignatoriesByAccount(Long accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Account", "id", accountId
                ));

        return account.getSignatories()
                .stream()
                .map(signatoryMapper::toResponse)
                .collect(Collectors.toList());
    }
}