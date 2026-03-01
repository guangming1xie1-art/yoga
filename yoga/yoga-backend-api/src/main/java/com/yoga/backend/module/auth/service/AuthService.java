package com.yoga.backend.module.auth.service;

import com.yoga.common.dto.auth.LoginRequest;
import com.yoga.common.dto.auth.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    TokenResponse refreshToken(String refreshToken);
    void logout(String token);
}
