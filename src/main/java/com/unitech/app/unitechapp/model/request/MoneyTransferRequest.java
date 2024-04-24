package com.unitech.app.unitechapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransferRequest {
    private String fromAccountNo;
    private String toAccountNo;
    private Double amount;
}
