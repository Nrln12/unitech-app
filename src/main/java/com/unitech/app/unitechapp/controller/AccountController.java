package com.unitech.app.unitechapp.controller;

import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping()
    @ResponseStatus(CREATED)
    public void createAccount(@RequestBody AccountRequest request) {
        accountService.createAccount(request);
    }

    @GetMapping()
    public ResponseEntity<?> getAccounts(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok(accountService.getAccountsByPin(token));
    }

    @PostMapping("/transfer")
    @ResponseStatus(CREATED)
    public void moneyTransfer(@RequestBody MoneyTransferRequest request) {
        accountService.moneyTransfer(request);
    }
}