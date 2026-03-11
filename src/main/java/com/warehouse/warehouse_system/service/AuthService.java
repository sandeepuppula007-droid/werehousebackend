package com.warehouse.warehouse_system.service;

import com.warehouse.warehouse_system.dto.LoginRequest;
import com.warehouse.warehouse_system.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}