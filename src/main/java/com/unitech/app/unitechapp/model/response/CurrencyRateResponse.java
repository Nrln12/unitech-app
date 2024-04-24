package com.unitech.app.unitechapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRateResponse {
    private String result;
    private String base_code;
    private String target_code;
    private Double conversion_result;
    private Double conversion_rate;
}
