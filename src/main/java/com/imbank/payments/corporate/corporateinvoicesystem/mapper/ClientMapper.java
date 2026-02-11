package com.imbank.payments.corporate.corporateinvoicesystem.mapper;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.ClientDTO;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.CorporateClient;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public CorporateClient toEntity(ClientDTO dto) {
        CorporateClient entity = new CorporateClient();
        entity.setCompanyName(dto.getCompanyName());
        entity.setRegistrationNumber(dto.getRegistrationNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setCreditLimit(dto.getCreditLimit());
        entity.setAccountStatus(dto.getAccountStatus());
        entity.setClientType(dto.getClientType());
        return entity;
    }

    public ClientDTO toDTO(CorporateClient entity) {
        ClientDTO dto = new ClientDTO();
        dto.setClientId(entity.getClientId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setRegistrationNumber(entity.getRegistrationNumber());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setCreditLimit(entity.getCreditLimit());
        dto.setAccountStatus(entity.getAccountStatus());
        dto.setCreatedAt(entity.getCreated_at());
        dto.setUpdatedAt(entity.getUpdated_at());
        dto.setClientType(entity.getClientType());
        return dto;
    }
}