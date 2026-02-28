package com.yoga.front.module.auth.service;

import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;

public interface AuthService {
    TokenResponse wxLogin(WxLoginRequest request);
    void bindPhone(String token, String phone, String smsCode);
    TokenResponse refreshToken(String refreshToken);
    void logout(String token);
}
