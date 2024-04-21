package com.unitech.app.unitechapp.controller;

import com.unitech.app.unitechapp.model.request.AccountRequest;
import com.unitech.app.unitechapp.model.request.MoneyTransferRequest;
import com.unitech.app.unitechapp.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<?> createAccount(@RequestBody AccountRequest request) {
        accountService.createAccount(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<?> getAccounts(@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok(accountService.getAccountsByPin(token));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> moneyTransfer(@RequestBody MoneyTransferRequest request) {
        accountService.moneyTransfer(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}