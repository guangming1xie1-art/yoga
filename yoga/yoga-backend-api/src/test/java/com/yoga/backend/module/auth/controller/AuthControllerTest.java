package com.yoga.backend.module.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.backend.module.auth.service.AuthService;
import com.yoga.common.dto.auth.LoginRequest;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@DisplayName("后台管理端认证控制器测试")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest validLoginRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("admin");
        validLoginRequest.setPassword("admin123");

        tokenResponse = new TokenResponse(
                "test.access.token",
                "test.refresh.token",
                28800L,
                "Bearer"
        );
    }

    @Test
    @DisplayName("登录成功")
    void login_Success() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/backend/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.accessToken").value("test.access.token"))
                .andExpect(jsonPath("$.data.refreshToken").value("test.refresh.token"))
                .andExpect(jsonPath("$.data.expiresIn").value(28800))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("登录失败-用户名或密码错误")
    void login_Failure_InvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误"));

        mockMvc.perform(post("/api/backend/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    @DisplayName("登录失败-用户名不能为空")
    void login_Failure_EmptyUsername() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("");
        request.setPassword("admin123");

        mockMvc.perform(post("/api/backend/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));
    }

    @Test
    @DisplayName("登录失败-密码不能为空")
    void login_Failure_EmptyPassword() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("");

        mockMvc.perform(post("/api/backend/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));
    }

    @Test
    @DisplayName("刷新Token成功")
    void refreshToken_Success() throws Exception {
        when(authService.refreshToken(anyString())).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/backend/auth/refresh")
                        .with(csrf())
                        .param("refreshToken", "valid.refresh.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.accessToken").value("test.access.token"));

        verify(authService).refreshToken("valid.refresh.token");
    }

    @Test
    @DisplayName("刷新Token失败-Token无效")
    void refreshToken_Failure_InvalidToken() throws Exception {
        when(authService.refreshToken(anyString()))
                .thenThrow(new BusinessException(ResultCode.TOKEN_INVALID));

        mockMvc.perform(post("/api/backend/auth/refresh")
                        .with(csrf())
                        .param("refreshToken", "invalid.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.TOKEN_INVALID.getMessage()));

        verify(authService).refreshToken("invalid.token");
    }

    @Test
    @WithMockUser
    @DisplayName("登出成功")
    void logout_Success() throws Exception {
        doNothing().when(authService).logout(anyString());

        mockMvc.perform(post("/api/backend/auth/logout")
                        .with(csrf())
                        .header("Authorization", "Bearer valid.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(authService).logout("Bearer valid.token");
    }

    @Test
    @DisplayName("登出失败-未授权")
    void logout_Failure_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/backend/auth/logout")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).logout(anyString());
    }
}
