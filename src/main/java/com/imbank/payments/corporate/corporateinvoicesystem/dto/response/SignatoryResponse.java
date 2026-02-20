package com.imbank.payments.corporate.corporateinvoicesystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatoryResponse {

    private Long signatoryId;
    private String name;
    private String position;
    private String email;
    private String phone;
    private String idNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> accountNumbers;
}