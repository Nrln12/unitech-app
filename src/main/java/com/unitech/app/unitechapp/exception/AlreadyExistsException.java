package com.unitech.app.unitechapp.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class AlreadyExistsException extends CustomException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public AlreadyExistsException(String message, Map<String, String> params) {
        super(message, HttpStatus.BAD_REQUEST, params);
    }
}
