package com.imbank.payments.corporate.corporateinvoicesystem.dto;

public record Pagination(
        int size,
        int page,
        int totalRecords,
        int totalPages
){}
