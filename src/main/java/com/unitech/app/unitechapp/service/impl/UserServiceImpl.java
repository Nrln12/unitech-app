package com.unitech.app.unitechapp.service.impl;

import com.unitech.app.unitechapp.exception.NotFoundException;
import com.unitech.app.unitechapp.repository.UserRepository;
import com.unitech.app.unitechapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String pin) {
                return userRepository.findByPin(pin)
                        .orElseThrow(() -> new NotFoundException("User not found"));
            }
        };
    }
}
