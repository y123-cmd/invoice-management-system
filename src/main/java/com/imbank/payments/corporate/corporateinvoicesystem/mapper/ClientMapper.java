package com.imbank.payments.corporate.corporateinvoicesystem.mapper;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {


    public CorporateClient toEntity(ClientRequest request) {
        CorporateClient entity = new CorporateClient();
        entity.setClientType(request.getClientType());
        entity.setCompanyName(request.getCompanyName());
        entity.setRegistrationNumber(request.getRegistrationNumber());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setCreditLimit(request.getCreditLimit());

        return entity;
    }


    public ClientResponse toResponse(CorporateClient entity) {
        ClientResponse response = new ClientResponse();
        response.setClientId(entity.getClientId());
        response.setClientType(entity.getClientType());
        response.setCompanyName(entity.getCompanyName());
        response.setRegistrationNumber(entity.getRegistrationNumber());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        response.setCreditLimit(entity.getCreditLimit());
        response.setAccountStatus(entity.getAccountStatus());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}