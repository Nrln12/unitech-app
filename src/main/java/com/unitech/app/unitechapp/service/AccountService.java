package com.unitech.app.unitechapp.service;

import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.model.response.AccountResponse;

import java.util.List;

public interface AccountService {
    void createAccount(AccountRequest request);

    List<AccountResponse> getAccountsByPin(String token);

    void moneyTransfer(MoneyTransferRequest request);
}
