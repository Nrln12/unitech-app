package com.unitech.app.unitechapp.service;

import com.unitech.app.unitechapp.model.request.LoginRequest;
import com.unitech.app.unitechapp.model.request.RegisterRequest;
import com.unitech.app.unitechapp.model.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
