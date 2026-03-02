package com.yoga.front.module.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.front.module.auth.service.AuthService;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@DisplayName("用户端认证控制器测试")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private WxLoginRequest wxLoginRequest;
    private TokenResponse tokenResponse;

    @BeforeEach
    void setUp() {
        wxLoginRequest = new WxLoginRequest();
        wxLoginRequest.setCode("wx_auth_code_123");

        tokenResponse = new TokenResponse(
                "test.access.token",
                "test.refresh.token",
                7200L,
                "Bearer"
        );
    }

    @Test
    @DisplayName("微信登录成功")
    void wxLogin_Success() throws Exception {
        when(authService.wxLogin(any(WxLoginRequest.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/front/auth/wx-login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wxLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.accessToken").value("test.access.token"))
                .andExpect(jsonPath("$.data.refreshToken").value("test.refresh.token"))
                .andExpect(jsonPath("$.data.expiresIn").value(7200))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));

        verify(authService).wxLogin(any(WxLoginRequest.class));
    }

    @Test
    @DisplayName("微信登录失败-微信code为空")
    void wxLogin_Failure_EmptyCode() throws Exception {
        WxLoginRequest request = new WxLoginRequest();
        request.setCode("");

        mockMvc.perform(post("/api/front/auth/wx-login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()));

        verify(authService, never()).wxLogin(any(WxLoginRequest.class));
    }

    @Test
    @DisplayName("微信登录失败-微信服务异常")
    void wxLogin_Failure_WxServiceError() throws Exception {
        when(authService.wxLogin(any(WxLoginRequest.class)))
                .thenThrow(new BusinessException(ResultCode.WX_LOGIN_FAILED));

        mockMvc.perform(post("/api/front/auth/wx-login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wxLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.WX_LOGIN_FAILED.getCode()))
                .andExpect(jsonPath("$.message").value(ResultCode.WX_LOGIN_FAILED.getMessage()));

        verify(authService).wxLogin(any(WxLoginRequest.class));
    }

    @Test
    @WithMockUser
    @DisplayName("绑定手机号成功")
    void bindPhone_Success() throws Exception {
        doNothing().when(authService).bindPhone(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/front/auth/bind-phone")
                        .with(csrf())
                        .header("Authorization", "Bearer valid.token")
                        .param("phone", "13800138000")
                        .param("code", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(authService).bindPhone("Bearer valid.token", "13800138000", "123456");
    }

    @Test
    @WithMockUser
    @DisplayName("绑定手机号失败-验证码错误")
    void bindPhone_Failure_InvalidCode() throws Exception {
        doThrow(new BusinessException(ResultCode.VALIDATION_ERROR.getCode(), "验证码错误"))
                .when(authService).bindPhone(anyString(), anyString(), anyString());

        mockMvc.perform(post("/api/front/auth/bind-phone")
                        .with(csrf())
                        .header("Authorization", "Bearer valid.token")
                        .param("phone", "13800138000")
                        .param("code", "wrongcode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ResultCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value("验证码错误"));

        verify(authService).bindPhone("Bearer valid.token", "13800138000", "wrongcode");
    }

    @Test
    @DisplayName("绑定手机号失败-未授权")
    void bindPhone_Failure_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/front/auth/bind-phone")
                        .with(csrf())
                        .param("phone", "13800138000")
                        .param("code", "123456"))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).bindPhone(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("刷新Token成功")
    void refreshToken_Success() throws Exception {
        when(authService.refreshToken(anyString())).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/front/auth/refresh")
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

        mockMvc.perform(post("/api/front/auth/refresh")
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

        mockMvc.perform(post("/api/front/auth/logout")
                        .with(csrf())
                        .header("Authorization", "Bearer valid.token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));

        verify(authService).logout("Bearer valid.token");
    }

    @Test
    @DisplayName("登出失败-未授权")
    void logout_Failure_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/front/auth/logout")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        verify(authService, never()).logout(anyString());
    }
}
