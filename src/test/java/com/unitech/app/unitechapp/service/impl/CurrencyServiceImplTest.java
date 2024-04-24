package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.model.response.CurrencyRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceImplTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private CurrencyServiceImpl currencyService;
    private String source;
    private String target;
    private Double amount;

    @BeforeEach
    public void setUp() {
        source = "AZN";
        target = "USD";
        amount = 50.0;
    }

    @Test
    public void testGetCurrencyRate_Success() {
        CurrencyRateResponse mockResponse = new CurrencyRateResponse();
        mockResponse.setResult("success");
        mockResponse.setConversion_rate(0.85);

        when(restTemplate.getForObject(
                eq("https://v6.exchangerate-api.com/v6/db41ec93e26061809ae3b62f/pair/AZN/USD/50.0"),
                eq(CurrencyRateResponse.class)))
                .thenReturn(mockResponse);

        CurrencyRateResponse response = currencyService.getCurrencyRate(source, target, amount);

        assertEquals("success", response.getResult());
        assertEquals(0.85, response.getConversion_rate());

        verify(restTemplate).getForObject(
                eq("https://v6.exchangerate-api.com/v6/db41ec93e26061809ae3b62f/pair/AZN/USD/50.0"),
                eq(CurrencyRateResponse.class));
    }
}
