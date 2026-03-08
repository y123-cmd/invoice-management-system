package com.imbank.payments.corporate.corporateinvoicesystem.service.impl;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.Pagination;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedSignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Account;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.DuplicateResourceException;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.mapper.SignatoryMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.AccountRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.repository.SignatoryRepository;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignatoryServiceImpl implements SignatoryService {

    private final SignatoryRepository signatoryRepository;
    private final AccountRepository accountRepository;
    private final SignatoryMapper signatoryMapper;

    @Override
    @Transactional
    public SignatoryResponse createSignatory(SignatoryRequest request) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Creating signatory - Name: {}, Email: {}",
                    request.getName(),
                    request.getEmail());

            validateDuplicateEmail(request.getEmail());
            validateDuplicateIdNumber(request.getIdNumber());

            Signatory signatory = signatoryMapper.toEntity(request);
            Signatory saved = signatoryRepository.save(signatory);

            log.info("SUCCESS: Signatory created - ID: {}, Name: {}",
                    saved.getSignatoryId(),
                    saved.getName());

            return signatoryMapper.toResponse(saved);

        } catch (DuplicateResourceException e) {
            log.error("FAILED: Duplicate signatory - Email: {}, Reason: {}",
                    request.getEmail(),
                    e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Unexpected error creating signatory - Name: {}, Error: {}",
                    request.getName(),
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to create signatory", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Signatory creation completed in {}ms for {}",
                    duration,
                    request.getName());
        }
    }

    @Override
    @Transactional
    public List<SignatoryResponse> createSignatoriesBatch(List<SignatoryRequest> requests) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Creating {} signatories in batch", requests.size());

            List<Signatory> signatories = requests.stream()
                    .map(request -> {
                        validateDuplicateEmail(request.getEmail());
                        validateDuplicateIdNumber(request.getIdNumber());
                        return signatoryMapper.toEntity(request);
                    })
                    .collect(Collectors.toList());

            List<Signatory> savedSignatories = signatoryRepository.saveAll(signatories);

            log.info("SUCCESS: Batch created {} signatories", savedSignatories.size());

            return savedSignatories.stream()
                    .map(signatoryMapper::toResponse)
                    .collect(Collectors.toList());

        } catch (DuplicateResourceException e) {
            log.error("FAILED: Batch signatory creation - Duplicate found: {}", e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Batch signatory creation error - {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create signatories in batch", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Batch signatory creation completed in {}ms", duration);
        }
    }

    @Override
    public PagedSignatoryResponse getAllSignatories(int page, int size) {

        if (page < 1) {
            throw new IllegalArgumentException("Page number must be 1 or greater");
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Signatory> signatoryPage = signatoryRepository.findAll(pageable);

        List<SignatoryResponse> signatories = signatoryPage.getContent()
                .stream()
                .map(signatoryMapper::toResponse)
                .collect(Collectors.toList());

        Pagination pagination = new Pagination(
                size,
                page,
                (int) signatoryPage.getTotalElements(),
                signatoryPage.getTotalPages()
        );

        return new PagedSignatoryResponse(200, "OK", signatories, pagination);
    }

    @Override
    public SignatoryResponse getSignatoryById(Long id) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Fetching signatory - ID: {}", id);

            Signatory signatory = findSignatoryOrThrow(id);

            log.info("SUCCESS: Signatory found - ID: {}, Name: {}",
                    signatory.getSignatoryId(),
                    signatory.getName());

            return signatoryMapper.toResponse(signatory);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Signatory not found - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error fetching signatory - ID: {}, Error: {}",
                    id,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to fetch signatory", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Fetch signatory completed in {}ms for ID: {}", duration, id);
        }
    }

    @Override
    @Transactional
    public SignatoryResponse updateSignatory(Long id, SignatoryRequest request) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Updating signatory - ID: {}", id);

            Signatory signatory = findSignatoryOrThrow(id);

            if (!signatory.getEmail().equals(request.getEmail())) {
                validateDuplicateEmail(request.getEmail());
            }

            if (request.getIdNumber() != null &&
                    !request.getIdNumber().equals(signatory.getIdNumber())) {
                validateDuplicateIdNumber(request.getIdNumber());
            }

            signatory.setName(request.getName());
            signatory.setPosition(request.getPosition());
            signatory.setEmail(request.getEmail());
            signatory.setPhone(request.getPhone());
            signatory.setIdNumber(request.getIdNumber());

            Signatory updated = signatoryRepository.save(signatory);

            log.info("SUCCESS: Signatory updated - ID: {}, Name: {}",
                    updated.getSignatoryId(),
                    updated.getName());

            return signatoryMapper.toResponse(updated);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Signatory not found for update - ID: {}", id);
            throw e;

        } catch (DuplicateResourceException e) {
            log.error("FAILED: Duplicate data in update - ID: {}, Reason: {}",
                    id,
                    e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error updating signatory - ID: {}, Error: {}",
                    id,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to update signatory", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Signatory update completed in {}ms for ID: {}", duration, id);
        }
    }
    @Override
    @Transactional
    public void deleteSignatory(Long id) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Soft deleting signatory - ID: {}", id);

            // Find signatory
            Signatory signatory = signatoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Signatory not found for deletion - ID: {}", id);
                        return new ResourceNotFoundException("Signatory", "id", id);
                    });

            // Soft delete - just mark as deleted!
            signatory.setDeleted(true);
            signatoryRepository.save(signatory);

            log.info("SUCCESS: Signatory soft deleted - ID: {}, Name: {}",
                    id, signatory.getName());

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Signatory not found for deletion - ID: {}", id);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error deleting signatory - ID: {}, Error: {}",
                    id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete signatory", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Signatory deletion completed in {}ms for ID: {}", duration, id);
        }
    }

    @Override
    @Transactional
    public void addSignatoryToAccount(Long signatoryId, Long accountId) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Adding signatory {} to account {}", signatoryId, accountId);

            Signatory signatory = findSignatoryOrThrow(signatoryId);
            Account account = findAccountOrThrow(accountId);

            if (account.getSignatories().contains(signatory)) {
                throw new DuplicateResourceException(
                        "Signatory is already authorized on this account"
                );
            }

            account.getSignatories().add(signatory);
            accountRepository.save(account);

            log.info("SUCCESS: Signatory {} added to account {}", signatoryId, accountId);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Signatory {} or Account {} not found", signatoryId, accountId);
            throw e;

        } catch (DuplicateResourceException e) {
            log.error("FAILED: Signatory {} already on account {} - {}",
                    signatoryId,
                    accountId,
                    e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error adding signatory {} to account {} - {}",
                    signatoryId,
                    accountId,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to add signatory to account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Add signatory to account completed in {}ms", duration);
        }
    }

    @Override
    @Transactional
    public void removeSignatoryFromAccount(Long signatoryId, Long accountId) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Removing signatory {} from account {}", signatoryId, accountId);

            Signatory signatory = findSignatoryOrThrow(signatoryId);
            Account account = findAccountOrThrow(accountId);

            if (!account.getSignatories().contains(signatory)) {
                throw new ResourceNotFoundException(
                        "Signatory is not associated with this account"
                );
            }

            account.getSignatories().remove(signatory);
            accountRepository.save(account);

            log.info("SUCCESS: Signatory {} removed from account {}", signatoryId, accountId);

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Signatory {} or Account {} not found or not associated",
                    signatoryId,
                    accountId);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error removing signatory {} from account {} - {}",
                    signatoryId,
                    accountId,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to remove signatory from account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Remove signatory from account completed in {}ms", duration);
        }
    }

    @Override
    public List<SignatoryResponse> getSignatoriesByAccount(Long accountId) {

        long startTime = System.currentTimeMillis();

        try {
            log.info("START: Fetching signatories for account - ID: {}", accountId);

            Account account = findAccountOrThrow(accountId);

            List<SignatoryResponse> responses = account.getSignatories()
                    .stream()
                    .map(signatoryMapper::toResponse)
                    .collect(Collectors.toList());

            log.info("SUCCESS: Found {} signatories for account {}",
                    responses.size(),
                    accountId);

            return responses;

        } catch (ResourceNotFoundException e) {
            log.error("FAILED: Account not found - ID: {}", accountId);
            throw e;

        } catch (Exception e) {
            log.error("FAILED: Error fetching signatories for account {} - {}",
                    accountId,
                    e.getMessage(),
                    e);
            throw new RuntimeException("Failed to fetch signatories for account", e);

        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("END: Fetch signatories by account completed in {}ms for account {}",
                    duration,
                    accountId);
        }
    }

    private Signatory findSignatoryOrThrow(Long id) {
        return signatoryRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Signatory", "id", id));
    }

    private Account findAccountOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account", "id", id));
    }

    private void validateDuplicateEmail(String email) {
        if (signatoryRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Signatory with email '" + email + "' already exists"
            );
        }
    }

    private void validateDuplicateIdNumber(String idNumber) {
        if (idNumber != null &&
                signatoryRepository.existsByIdNumber(idNumber)) {
            throw new DuplicateResourceException(
                    "Signatory with ID number '" + idNumber + "' already exists"
            );
        }
    }
}
