package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.entity.User;
import com.unitech.app.unitechapp.exception.AlreadyExistsException;
import com.unitech.app.unitechapp.model.request.RegisterRequest;
import com.unitech.app.unitechapp.model.response.AuthResponse;
import com.unitech.app.unitechapp.repository.UserRepository;
import com.unitech.app.unitechapp.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private ModelMapper modelMapper; // Mock ModelMapper dependency

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setPin("123456");
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setPassword("Password123!");

        User userMapped = new User();
        when(modelMapper.map(request, User.class)).thenReturn(userMapped);

        when(userRepository.findByPin(request.getPin())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        AuthResponse response = authService.register(request);

        verify(modelMapper, times(1)).map(request, User.class);

        verify(userRepository, times(1)).save(userMapped);

        assertNotNull(response.getToken());
    }

    @Test
    public void testRegister_AlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setPin("123456");
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setPassword("Password123");

        User existingUser = new User();
        when(userRepository.findByPin(request.getPin())).thenReturn(Optional.of(existingUser));

        assertThrows(AlreadyExistsException.class, () -> authService.register(request));

        verify(modelMapper, never()).map(any(), any());

        verify(userRepository, never()).save(any());
    }

}
