package com.unitech.app.unitechapp.controller;

import com.unitech.app.unitechapp.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyRateController {
    private final CurrencyService currencyService;

    @GetMapping("{source}/{target}/{amount}")
    public ResponseEntity<?> getCurrency(@PathVariable String source, @PathVariable String target, @PathVariable Double amount) {
        return ResponseEntity.ok(currencyService.getCurrencyRate(source, target, amount));
    }
}
