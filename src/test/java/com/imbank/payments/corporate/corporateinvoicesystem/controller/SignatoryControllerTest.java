package com.imbank.payments.corporate.corporateinvoicesystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.request.SignatoryRequest;
import com.imbank.payments.corporate.corporateinvoicesystem.dto.response.SignatoryResponse;
import com.imbank.payments.corporate.corporateinvoicesystem.exception.ResourceNotFoundException;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtAuthenticationFilter;
import com.imbank.payments.corporate.corporateinvoicesystem.security.JwtUtil;
import com.imbank.payments.corporate.corporateinvoicesystem.service.SignatoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SignatoryController.class)
@ActiveProfiles("test")
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
    void getSignatoryById_ShouldReturn200_WhenSignatoryExists() throws Exception {
        when(signatoryService.getSignatoryById(1L)).thenReturn(buildSignatoryResponse());

        mockMvc.perform(get("/api/v1/signatories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("John Doe"))
                .andExpect(jsonPath("$.data.idNumber").value("ID001"));
    }

    @Test
    void getSignatoryById_ShouldReturn404_WhenSignatoryDoesNotExist() throws Exception {
        when(signatoryService.getSignatoryById(999L))
                .thenThrow(new ResourceNotFoundException("Signatory not found"));

        mockMvc.perform(get("/api/v1/signatories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
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
    void deleteSignatory_ShouldReturn200_WhenSignatoryIsDeleted() throws Exception {
        doNothing().when(signatoryService).deleteSignatory(1L);

        mockMvc.perform(delete("/api/v1/signatories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory deleted successfully"));
    }

    @Test
    void addSignatoryToAccount_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(signatoryService).addSignatoryToAccount(1L, 1L);

        mockMvc.perform(post("/api/v1/signatories/1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory added to account successfully"));
    }

    @Test
    void removeSignatoryFromAccount_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(signatoryService).removeSignatoryFromAccount(1L, 1L);

        mockMvc.perform(delete("/api/v1/signatories/1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Signatory removed from account successfully"));
    }

    @Test
    void getSignatoriesByAccount_ShouldReturn200_WhenAccountExists() throws Exception {
        when(signatoryService.getSignatoriesByAccount(1L))
                .thenReturn(List.of(buildSignatoryResponse()));

        mockMvc.perform(get("/api/v1/signatories/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("John Doe"));
    }
}