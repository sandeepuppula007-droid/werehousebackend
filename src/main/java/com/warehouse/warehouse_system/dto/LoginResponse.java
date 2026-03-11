package com.warehouse.warehouse_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String message;
    private String role;
    
    public LoginResponse(String token) {
        this.token = token;
    }
}