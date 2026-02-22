package com.imbank.payments.corporate.corporateinvoicesystem.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusUpdateRequest {
    private String status;
}