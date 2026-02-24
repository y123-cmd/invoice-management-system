package com.imbank.payments.corporate.corporateinvoicesystem.dto.response;

import com.imbank.payments.corporate.corporateinvoicesystem.dto.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedClientResponse {

    private int status;
    private String message;
    private List<ClientResponse> data;
    private Pagination pagination;
}