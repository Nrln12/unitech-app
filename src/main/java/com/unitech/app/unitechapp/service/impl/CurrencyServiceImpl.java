package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.model.response.CurrencyRateResponse;
import com.unitech.app.unitechapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private String apiKey = "db41ec93e26061809ae3b62f";
    private final RestTemplate restTemplate;

    @Override
    public CurrencyRateResponse getCurrencyRate(String source, String target, Double amount) {
        String url = API_BASE_URL + apiKey + "/pair/" + source + "/" + target + "/" + amount;

        try {
            CurrencyRateResponse response = restTemplate.getForObject(url, CurrencyRateResponse.class);
            System.out.println(url);
            if (response != null && response.getResult().equals("success")) {
                return response;
            }
            throw new RuntimeException("Failed to fetch currency rate");
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException("Currency pair not found or API endpoint not reachable");
        }
    }

}

