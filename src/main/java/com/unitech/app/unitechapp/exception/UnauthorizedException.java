package com.unitech.app.unitechapp.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Map<String, String> params) {
        super(message, HttpStatus.UNAUTHORIZED, params);
    }
}
