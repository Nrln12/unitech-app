package com.unitech.app.unitechapp.controller;

import com.unitech.app.unitechapp.model.response.CurrencyRateResponse;
import com.unitech.app.unitechapp.service.CurrencyService;
import com.unitech.app.unitechapp.service.JwtService;
import com.unitech.app.unitechapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyRateController.class)
@AutoConfigureMockMvc(addFilters = false)
class CurrencyRateControllerTest {
    private static final String CURRENCY_RATE_PATH = "/api/currency";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService currencyService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserService userService;

    @Test
    void getCurrencyRateTest() throws Exception {
        String source = "AZN";
        String target = "USD";
        Double amount = 12.2;
        CurrencyRateResponse response = CurrencyRateResponse.builder()
                .base_code("AZN")
                .target_code("USD")
                .conversion_result(123.8)
                .conversion_rate(0.54)
                .build();
        when(currencyService.getCurrencyRate(source, target, amount)).thenReturn(response);
        mockMvc.perform(get(CURRENCY_RATE_PATH + "/{source}/{target}/{amount}", source, target, amount)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conversion_result").value(response.getConversion_result()))
                .andExpect(jsonPath("$.conversion_rate").value(response.getConversion_rate()));
        verify(currencyService, times(1)).getCurrencyRate(source, target, amount);
    }
}