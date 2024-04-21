package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.model.response.CurrencyRateResponse;
import com.unitech.app.unitechapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    @Value("${api.key}")
    private String apiKey;
    private final RestTemplate restTemplate;

    @Override
    public CurrencyRateResponse getCurrencyRate(String source, String target, Double amount) {
        String url = API_BASE_URL + apiKey + "/pair/" + source + "/" + target + "/" + amount;

        ResponseEntity<CurrencyRateResponse> responseEntity = new RestTemplate().getForEntity(url, CurrencyRateResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            CurrencyRateResponse response = responseEntity.getBody();
            if (response != null && response.getResult().equals("success")) {
                return response;
            } else {
                throw new RuntimeException("Failed to fetch currency rate: " + responseEntity.getBody());
            }
        } else {
            throw new RuntimeException("Failed to fetch currency rate: HTTP " + responseEntity.getStatusCode());
        }
    }
}
