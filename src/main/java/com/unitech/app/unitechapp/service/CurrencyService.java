package com.unitech.app.unitechapp.service;

import com.unitech.app.unitechapp.model.response.CurrencyRateResponse;

public interface CurrencyService {
    CurrencyRateResponse getCurrencyRate(String source, String target, Double amount);
}
