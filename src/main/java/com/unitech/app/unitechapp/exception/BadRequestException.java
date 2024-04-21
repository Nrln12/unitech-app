package com.unitech.app.unitechapp.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BadRequestException extends CustomException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Map<String, String> params) {
        super(message, HttpStatus.BAD_REQUEST, params);
    }
}
