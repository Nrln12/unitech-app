package com.unitech.app.unitechapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unitech.app.unitechapp.config.JwtAuthenticationFilter;
import com.unitech.app.unitechapp.model.request.LoginRequest;
import com.unitech.app.unitechapp.model.request.RegisterRequest;
import com.unitech.app.unitechapp.model.response.AuthResponse;
import com.unitech.app.unitechapp.service.AuthService;
import com.unitech.app.unitechapp.service.JwtService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void registerTest() throws Exception {
        var request = new RegisterRequest();

        request.setFirstname("John");
        request.setLastname("Doe");
        request.setPin("4EERWQF");
        request.setPassword("Password");
        var response = new AuthResponse();
        response.setToken("token123");

        when(authService.register(ArgumentMatchers.any(RegisterRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post(REGISTER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.token").value(Matchers.is(response.getToken())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(authService, times(1)).register(request);
    }

    @Test
    void loginTest() throws Exception {
        var request = new LoginRequest();
        request.setPassword("Password1!");
        request.setPin("4EERWQF");
        var response = new AuthResponse();
        response.setToken("token123");
        when(authService.login(ArgumentMatchers.any(LoginRequest.class)))
                .thenReturn(response);
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(jsonPath("$.token").value(Matchers.is((response.getToken()))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }
}