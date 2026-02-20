package com.imbank.payments.corporate.corporateinvoicesystem.dto;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedAccountResponse {

    private int status;
    private String message;
    private List<AccountResponse> data;
    private Pagination pagination;
}