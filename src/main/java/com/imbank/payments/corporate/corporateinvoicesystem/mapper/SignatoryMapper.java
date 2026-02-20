package com.imbank.payments.corporate.corporateinvoicesystem.mapper;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.Signatory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class SignatoryMapper {

    public Signatory toEntity(SignatoryRequest request) {
        Signatory signatory = new Signatory();
        signatory.setName(request.getName());
        signatory.setPosition(request.getPosition());
        signatory.setEmail(request.getEmail());
        signatory.setPhone(request.getPhone());
        signatory.setIdNumber(request.getIdNumber());
        return signatory;
    }
    public SignatoryResponse toResponse(Signatory signatory) {
        SignatoryResponse response = new SignatoryResponse();
        response.setSignatoryId(signatory.getSignatoryId());
        response.setName(signatory.getName());
        response.setPosition(signatory.getPosition());
        response.setEmail(signatory.getEmail());
        response.setPhone(signatory.getPhone());
        response.setIdNumber(signatory.getIdNumber());
        response.setCreatedAt(signatory.getCreatedAt());
        response.setUpdatedAt(signatory.getUpdatedAt());

        if (signatory.getAccounts() != null) {
            response.setAccountNumbers(
                    signatory.getAccounts()
                            .stream()
                            .map(account -> account.getAccountNumber())
                            .collect(Collectors.toList())
            );
        } else {
            response.setAccountNumbers(new ArrayList<>());
        }

        return response;
    }
}