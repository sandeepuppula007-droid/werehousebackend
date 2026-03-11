package com.warehouse.warehouse_system.service.impl;

import com.warehouse.warehouse_system.dto.LoginRequest;
import com.warehouse.warehouse_system.dto.LoginResponse;
import com.warehouse.warehouse_system.entity.User;
import com.warehouse.warehouse_system.repository.UserRepository;
import com.warehouse.warehouse_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    
    @Override
    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }
        
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return new LoginResponse("success", "Login successful", user.getRole().toString());
    }
}