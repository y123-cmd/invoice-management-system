package com.imbank.payments.corporate.corporateinvoicesystem.service;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;

import java.util.List;

public interface SignatoryService {

    SignatoryResponse createSignatory(SignatoryRequest request);

    List<SignatoryResponse> createSignatoriesBatch(List<SignatoryRequest> requests);

    List<SignatoryResponse> getAllSignatories();

    SignatoryResponse getSignatoryById(Long id);

    SignatoryResponse updateSignatory(Long id, SignatoryRequest request);

    void deleteSignatory(Long id);

    void addSignatoryToAccount(Long signatoryId, Long accountId);

    void removeSignatoryFromAccount(Long signatoryId, Long accountId);

    List<SignatoryResponse> getSignatoriesByAccount(Long accountId);
}