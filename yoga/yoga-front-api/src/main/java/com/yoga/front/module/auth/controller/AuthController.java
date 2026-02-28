package com.yoga.front.module.auth.controller;

import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;
import com.yoga.common.result.Result;
import com.yoga.front.module.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户认证", description = "微信登录/绑定手机/Token刷新")
@RestController
@RequestMapping("/api/front/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "微信登录（code换取Token）")
    @PostMapping("/wx-login")
    public Result<TokenResponse> wxLogin(@Valid @RequestBody WxLoginRequest request) {
        return Result.ok(authService.wxLogin(request));
    }

    @Operation(summary = "绑定手机号")
    @PostMapping("/bind-phone")
    public Result<Void> bindPhone(@RequestHeader("Authorization") String token,
                                  @RequestParam String phone,
                                  @RequestParam String code) {
        // TODO: 短信验证码校验 + 绑定
        authService.bindPhone(token, phone, code);
        return Result.ok();
    }

    @Operation(summary = "刷新 Token")
    @PostMapping("/refresh")
    public Result<TokenResponse> refreshToken(@RequestParam String refreshToken) {
        return Result.ok(authService.refreshToken(refreshToken));
    }

    @Operation(summary = "登出")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return Result.ok();
    }
}
