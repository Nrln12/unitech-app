package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.entity.User;
import com.unitech.app.unitechapp.exception.AlreadyExistsException;
import com.unitech.app.unitechapp.exception.BadRequestException;
import com.unitech.app.unitechapp.exception.NotFoundException;
import com.unitech.app.unitechapp.exception.UnauthorizedException;
import com.unitech.app.unitechapp.model.request.LoginRequest;
import com.unitech.app.unitechapp.model.request.RegisterRequest;
import com.unitech.app.unitechapp.model.response.AuthResponse;
import com.unitech.app.unitechapp.repository.UserRepository;
import com.unitech.app.unitechapp.service.AuthService;
import com.unitech.app.unitechapp.service.JwtService;
import com.unitech.app.unitechapp.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        userRepository.findByPin(request.getPin())
                .ifPresent(user -> {
                    throw new AlreadyExistsException("The user with this pin already exists");
                });

        if (request.getFirstname() == null || request.getFirstname().isEmpty() || !(ValidationUtils.nameValidation(request.getFirstname())))
            throw new BadRequestException("The name you entered is not valid");
        if (request.getLastname() == null || request.getLastname().isEmpty() || !(ValidationUtils.nameValidation(request.getLastname())))
            throw new BadRequestException("The lastname you entered is not valid");
        if (request.getPassword() == null || request.getPassword().isEmpty() || !(ValidationUtils.passwordValidation(request.getPassword())))
            throw new BadRequestException("The password you entered is not valid");
        if (request.getPin() == null || request.getPin().isEmpty() || !(ValidationUtils.pinValidation(request.getPin())))
            throw new BadRequestException("The pin you entered is not valid");


        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request.getPin() == null || request.getPin().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BadRequestException("PIN or password is missing or empty");
        }

        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken
                            (request.getPin(),
                                    request.getPassword()));
        } catch (Exception e) {
            throw new UnauthorizedException("Incorrect PIN or password");
        }
        User user = userRepository.findByPin(request.getPin())
                .orElseThrow(() -> new NotFoundException("User with this PIN doesn't exist"));

        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder().token(jwtToken).build();
    }
}
