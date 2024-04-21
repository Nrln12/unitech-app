package com.unitech.app.unitechapp.model.response;

import lombok.Data;

@Data
public class CurrencyRateResponse {
    private String result;
    private String base_code;
    private String target_code;
    private Double conversion_result;
    private Double conversion_rate;
}
