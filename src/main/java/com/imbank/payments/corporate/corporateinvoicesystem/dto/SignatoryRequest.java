package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignatoryRequest {

    private String name;
    private String position;
    private String email;
    private String phone;
    private String idNumber;
}