package com.unitech.app.unitechapp.model.request;

import lombok.Data;

@Data
public class MoneyTransferRequest {
    private String fromAccountNo;
    private String toAccountNo;
    private Double amount;
}
