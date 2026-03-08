package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.PagedSignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.security.CustomUserDetailsService;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtAuthenticationFilter;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import com.imbank.payments.corporate.corporateinvoicesystem.security.SecurityConfig;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignatoryController.class)
@ActiveProfiles("test")
@Import(SecurityConfig.class)
public class SignatoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignatoryService signatoryService;

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

    private SignatoryResponse buildSignatoryResponse() {
        SignatoryResponse response = new SignatoryResponse();
        response.setSignatoryId(1L);
        response.setName("John Doe");
        response.setPosition("CEO");
        response.setEmail("john@safaricom.co.ke");
        response.setPhone("+254722000000");
        response.setIdNumber("ID001");
        return response;
    }

    private SignatoryRequest buildSignatoryRequest() {
        return new SignatoryRequest(
                "John Doe",
                "CEO",
                "john@safaricom.co.ke",
                "+254722000000",
                "ID001"
        );
    }

    @Test
    @WithMockUser
    void createSignatory_ShouldReturn201_WhenSignatoryIsCreated() throws Exception {
        when(signatoryService.createSignatory(any(SignatoryRequest.class)))
                .thenReturn(buildSignatoryResponse());

        mockMvc.perform(post("/api/v1/signatories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSignatoryRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.email").value("john@safaricom.co.ke"));
    }

    @Test
    @WithMockUser
    void getSignatoryById_ShouldReturn200_WhenSignatoryExists() throws Exception {
        when(signatoryService.getSignatoryById(1L)).thenReturn(buildSignatoryResponse());

        mockMvc.perform(get("/api/v1/signatories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.idNumber").value("ID001"));
    }

    @Test
    @WithMockUser
    void getSignatoryById_ShouldReturn404_WhenSignatoryDoesNotExist() throws Exception {
        when(signatoryService.getSignatoryById(999L))
                .thenThrow(new ResourceNotFoundException("Signatory not found"));

        mockMvc.perform(get("/api/v1/signatories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void updateSignatory_ShouldReturn200_WhenSignatoryIsUpdated() throws Exception {
        when(signatoryService.updateSignatory(eq(1L), any(SignatoryRequest.class)))
                .thenReturn(buildSignatoryResponse());

        mockMvc.perform(put("/api/v1/signatories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildSignatoryRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    @WithMockUser
    void deleteSignatory_ShouldReturn200_WhenSignatoryIsDeleted() throws Exception {
        doNothing().when(signatoryService).deleteSignatory(1L);

        mockMvc.perform(delete("/api/v1/signatories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory deleted successfully"));
    }

    @Test
    @WithMockUser
    void addSignatoryToAccount_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(signatoryService).addSignatoryToAccount(1L, 1L);

        mockMvc.perform(post("/api/v1/signatories/1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory added to account successfully"));
    }

    @Test
    @WithMockUser
    void removeSignatoryFromAccount_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(signatoryService).removeSignatoryFromAccount(1L, 1L);

        mockMvc.perform(delete("/api/v1/signatories/1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory removed from account successfully"));
    }

    @Test
    @WithMockUser
    void getSignatoriesByAccount_ShouldReturn200_WhenAccountExists() throws Exception {
        when(signatoryService.getSignatoriesByAccount(1L))
                .thenReturn(List.of(buildSignatoryResponse()));

        mockMvc.perform(get("/api/v1/signatories/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }
    @Test
    @WithMockUser
    void createSignatoriesBatch_ShouldReturn201_WhenSignatoriesAreCreated()throws Exception{
        List<SignatoryRequest> requests = List.of(buildSignatoryRequest());
        when(signatoryService.createSignatoriesBatch(anyList()))
                .thenReturn(List.of(buildSignatoryResponse()));
        mockMvc.perform(post("/api/v1/signatories/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));


    }
    @Test
    @WithMockUser
    void getAllSignatories_ShouldReturn200_WhenSignatoriesExists() throws Exception{
        PagedSignatoryResponse signatoryResponse = new PagedSignatoryResponse(
                200,
                "Signatories Retrieved Successfully",
                List.of(buildSignatoryResponse()),
                null
        );
        when(signatoryService.getAllSignatories(1,10))
                .thenReturn(signatoryResponse);

        mockMvc.perform(get("/api/v1/signatories")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.data[0].name").value("John Doe"));
    }

}