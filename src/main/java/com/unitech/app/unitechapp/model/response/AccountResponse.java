package com.unitech.app.unitechapp.model.response;

import lombok.Data;

@Data
public class AccountResponse {
    private String accountNo;
    private Double balance;
    private UserResponse user;
}
