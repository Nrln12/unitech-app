package com.unitech.app.unitechapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.model.response.AccountResponse;
import com.unitech.app.unitechapp.service.AccountService;
import com.unitech.app.unitechapp.service.JwtService;
import com.unitech.app.unitechapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {
    private static final String ACCOUNT_PATH = "/api/accounts";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @Test
    void createAccountTest() throws Exception {
        var request = new AccountRequest();
        request.setAccountNo("A12345");
        request.setPin("3VSWEKJ");
        doNothing().when(accountService).createAccount(request);
        mockMvc.perform(post(ACCOUNT_PATH)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(accountService, times(1)).createAccount(request);

    }

    @Test
    void getAccounts() throws Exception {
        String token = "valid";
        AccountResponse response = AccountResponse.builder()
                .accountNo("A12345")
                .balance(124.3)
                .build();
        List<AccountResponse> responses = List.of(response);
        when(accountService.getAccountsByPin(eq(token))).thenReturn(responses);
        mockMvc.perform(get(ACCOUNT_PATH)
                        .with(csrf())
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].balance").value(response.getBalance()))
                .andExpect(jsonPath("$.[0].accountNo").value(responses.get(0).getAccountNo()));

        verify(accountService, times(1)).getAccountsByPin(token);
    }

    @Test
    void moneyTransfer() throws Exception {
        var request = MoneyTransferRequest.builder()
                .fromAccountNo("A12345")
                .toAccountNo("B12345")
                .amount(124.3)
                .build();
        doNothing().when(accountService).moneyTransfer(request);
        mockMvc
                .perform(post(ACCOUNT_PATH + "/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(accountService, times(1)).moneyTransfer(request);

    }
}