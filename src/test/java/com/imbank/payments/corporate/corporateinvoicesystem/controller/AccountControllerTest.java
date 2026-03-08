package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.AccountStatusUpdateRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.AccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ApiResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedAccountResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountType;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.security.CustomUserDetailsService;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtAuthenticationFilter;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import com.imbank.payments.corporate.corporateinvoicesystem.security.SecurityConfig;
import com.imbank.payments.corporate.corporateinvoicesystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() throws Exception {
        Mockito.doAnswer(invocation -> {
            jakarta.servlet.FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    private AccountResponse buildAccountResponse() {
        AccountResponse response = new AccountResponse();
        response.setAccountId(1L);
        response.setAccountNumber("ACC12345");
        response.setAccountType(AccountType.SAVINGS);
        response.setBalance(new BigDecimal("10000.00"));
        response.setStatus(AccountStatus.ACTIVE);
        response.setClientId(1L);
        return response;
    }

    @Test
    @WithMockUser
    void createAccount_ShouldReturn201_WhenAccountIsCreated() throws Exception {
        AccountRequest request = new AccountRequest(AccountType.SAVINGS, new BigDecimal("10000.00"), 1L);
        when(accountService.createAccount(any(AccountRequest.class))).thenReturn(buildAccountResponse());

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accountNumber").value("ACC12345"))
                .andExpect(jsonPath("$.data.accountType").value("SAVINGS"));
    }

    @Test
    @WithMockUser
    void createAccount_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        AccountRequest request = new AccountRequest(null, null, null);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getAccountById_ShouldReturn200_WhenAccountExists() throws Exception {
        when(accountService.getAccountById(1L)).thenReturn(buildAccountResponse());

        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountNumber").value("ACC12345"))
                .andExpect(jsonPath("$.data.accountType").value("SAVINGS"));
    }

    @Test
    @WithMockUser
    void getAccountById_ShouldReturn404_WhenAccountDoesNotExist() throws Exception {
        when(accountService.getAccountById(999L))
                .thenThrow(new ResourceNotFoundException("Account not found"));

        mockMvc.perform(get("/api/v1/accounts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getAccountsByClientId_ShouldReturn200_WhenClientExists() throws Exception {
        when(accountService.getAccountsByClientId(1L)).thenReturn(List.of(buildAccountResponse()));

        mockMvc.perform(get("/api/v1/accounts/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].accountNumber").value("ACC12345"));
    }

    @Test
    @WithMockUser
    void updateAccount_ShouldReturn200_WhenAccountIsUpdated() throws Exception {
        AccountRequest request = new AccountRequest(AccountType.SAVINGS, new BigDecimal("20000.00"), 1L);
        when(accountService.updateAccount(eq(1L), any(AccountRequest.class))).thenReturn(buildAccountResponse());

        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountNumber").value("ACC12345"));
    }

    @Test
    @WithMockUser
    void deleteAccount_ShouldReturn204_WhenAccountIsDeleted() throws Exception {
        doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void updateAccountStatus_ShouldReturn200_WhenStatusIsUpdated() throws Exception {
        AccountStatusUpdateRequest request = new AccountStatusUpdateRequest();
        request.setStatus("SUSPENDED");

        when(accountService.updateAccountStatus(eq(1L), any(AccountStatus.class)))
                .thenReturn(buildAccountResponse());

        mockMvc.perform(patch("/api/v1/accounts/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountNumber").value("ACC12345"));
    }
    @Test
    @WithMockUser
    void createAccountsBatch_ShouldReturn201_WhenAccountsAreCreated()throws Exception{
        AccountRequest request = new AccountRequest(AccountType.SAVINGS, new BigDecimal("10000.00"), 1L);
        List<AccountRequest> requests = List.of(request);
        when(accountService.createAccountsBatch(anyList()))
                .thenReturn(List.of(buildAccountResponse()));
        mockMvc.perform(post("/api/v1/accounts/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].accountNumber").value("ACC12345"));
    }
    @Test
    @WithMockUser
    void getAllAccounts_ShouldReturn200_WhenAccountExists()throws Exception{
        PagedAccountResponse pagedResponse = new PagedAccountResponse(
                200,
                "Account Retrieved Successfully",
                List.of(buildAccountResponse()),
                null
        );
        when(accountService.getAllAccounts(null,null,1,10))
                .thenReturn(pagedResponse);
        mockMvc.perform(get("/api/v1/accounts")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].accountNumber").value("ACC12345"));

    }
}