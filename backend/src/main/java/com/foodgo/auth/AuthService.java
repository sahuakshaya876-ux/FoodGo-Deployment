package com.foodgo.auth;

import com.foodgo.auth.dto.AuthResponse;
import com.foodgo.auth.dto.LoginRequest;
import com.foodgo.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
