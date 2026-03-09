package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.ClientRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.ClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedClientResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.AccountStatus;
import com.imbank.payments.corporate.corporateinvoicesystem.entity.ClientType;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.security.CustomUserDetailsService;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtAuthenticationFilter;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import com.imbank.payments.corporate.corporateinvoicesystem.security.SecurityConfig;
import com.imbank.payments.corporate.corporateinvoicesystem.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
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

@WebMvcTest(ClientController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

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

    private ClientResponse buildClientResponse() {
        ClientResponse response = new ClientResponse();
        response.setClientId(1L);
        response.setCompanyName("Safaricom");
        response.setEmail("saf@gmail.com");
        response.setRegistrationNumber("SAF-2026-001");
        response.setClientType(ClientType.CORPORATE);
        response.setCreditLimit(new BigDecimal("5000000.00"));
        response.setAccountStatus(AccountStatus.ACTIVE);
        return response;
    }

    private ClientRequest buildClientRequest() {
        return new ClientRequest(
                ClientType.CORPORATE,
                "Safaricom",
                "SAF-2026-001",
                "saf@gmail.com",
                "+254722000000",
                new BigDecimal("5000000.00")
        );
    }

    @Test
    @WithMockUser
    void createClient_ShouldReturn201_WhenClientIsCreated() throws Exception {
        when(clientService.createClient(any(ClientRequest.class))).thenReturn(buildClientResponse());

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildClientRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.companyName").value("Safaricom"))
                .andExpect(jsonPath("$.data.email").value("saf@gmail.com"));
    }

    @Test
    @WithMockUser
    void createClient_ShouldReturn400_WhenRequestIsInvalid() throws Exception {
        ClientRequest invalid = new ClientRequest(null, null, null, null, null, null);

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getClientById_ShouldReturn200_WhenClientExists() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(buildClientResponse());

        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyName").value("Safaricom"))
                .andExpect(jsonPath("$.data.registrationNumber").value("SAF-2026-001"));
    }

    @Test
    @WithMockUser
    void getClientById_ShouldReturn404_WhenClientDoesNotExist() throws Exception {
        when(clientService.getClientById(999L))
                .thenThrow(new ResourceNotFoundException("Client not found"));

        mockMvc.perform(get("/api/v1/clients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateClient_ShouldReturn200_WhenClientIsUpdated() throws Exception {
        when(clientService.updateClient(eq(1L), any(ClientRequest.class)))
                .thenReturn(buildClientResponse());

        mockMvc.perform(put("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildClientRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyName").value("Safaricom"));
    }

    @Test
    @WithMockUser
    void deleteClient_ShouldReturn204_WhenClientIsDeleted() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getActiveClients_ShouldReturn200_WhenActiveClientsExist() throws Exception {
        when(clientService.getActiveClients()).thenReturn(List.of(buildClientResponse()));

        mockMvc.perform(get("/api/v1/clients/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].companyName").value("Safaricom"));
    }

    @Test
    @WithMockUser
    void updateClientStatus_ShouldReturn200_WhenStatusIsUpdated() throws Exception {
        when(clientService.updateClientStatus(eq(1L), any(AccountStatus.class)))
                .thenReturn(buildClientResponse());

        mockMvc.perform(patch("/api/v1/clients/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AccountStatus.SUSPENDED)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.companyName").value("Safaricom"));
    }

    @Test
    @WithMockUser
    void findByRegistrationNumber_ShouldReturn200_WhenClientExists() throws Exception {
        when(clientService.findByRegistrationNumber("SAF-2026-001"))
                .thenReturn(buildClientResponse());

        mockMvc.perform(get("/api/v1/clients/registration/SAF-2026-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.registrationNumber").value("SAF-2026-001"));
    }
    @Test
    @WithMockUser
    void createClientsBatch_ShouldReturn201_WhenClientsAreCreated() throws Exception{
        List<ClientRequest> requests = List.of(buildClientRequest());
        when(clientService.createClientsBatch(anyList()))
                .thenReturn(List.of(buildClientResponse()));
        mockMvc.perform(post("/api/v1/clients/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].companyName").value("Safaricom"));
    }
    @Test
    @WithMockUser
    void getAllClients_ShouldReturn200_WhenClientsExists()throws Exception{
        PagedClientResponse clientResponse = new PagedClientResponse(
                200,
                "Clients Retrieved Successfully",
                List.of(buildClientResponse()),
                null
        );
        when(clientService.getAllClients(null,null,null,1,10))
                .thenReturn(clientResponse);
        mockMvc.perform(get("/api/v1/clients")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].companyName").value("Safaricom"));
    }
}