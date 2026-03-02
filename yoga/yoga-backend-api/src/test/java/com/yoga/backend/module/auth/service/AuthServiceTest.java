package com.yoga.backend.module.auth.service;

import com.yoga.backend.config.JwtProperties;
import com.yoga.backend.module.auth.service.impl.AuthServiceImpl;
import com.yoga.common.constant.Constants;
import com.yoga.common.dto.auth.LoginRequest;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import com.yoga.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("后台管理端认证服务测试")
class AuthServiceTest {

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String TEST_SECRET = "test-secret-key-at-least-32-characters-long-for-jwt-signing";

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn(TEST_SECRET);
        when(jwtProperties.getAccessTokenExpire()).thenReturn(28800L);
        when(jwtProperties.getRefreshTokenExpire()).thenReturn(604800L);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("登录成功")
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        TokenResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();
        assertThat(response.getExpiresIn()).isEqualTo(28800L);
        assertThat(response.getTokenType()).isEqualTo("Bearer");

        verify(valueOperations).set(
                eq(Constants.REDIS_REFRESH_TOKEN + "1"),
                anyString(),
                eq(604800L),
                eq(TimeUnit.SECONDS)
        );
    }

    @Test
    @DisplayName("登录失败-用户名错误")
    void login_Failure_InvalidUsername() {
        LoginRequest request = new LoginRequest();
        request.setUsername("wronguser");
        request.setPassword("admin123");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    @DisplayName("登录失败-密码错误")
    void login_Failure_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrongpassword");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("用户名或密码错误");
    }

    @Test
    @DisplayName("刷新Token成功")
    void refreshToken_Success() {
        Map<String, Object> claims = Map.of(
                Constants.CLAIM_USER_ID, 1L,
                Constants.CLAIM_ROLES, Constants.ROLE_SYS_ADMIN
        );

        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        when(valueOperations.get(Constants.REDIS_REFRESH_TOKEN + "1")).thenReturn(refreshToken);

        TokenResponse response = authService.refreshToken(refreshToken);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(response.getExpiresIn()).isEqualTo(28800L);
    }

    @Test
    @DisplayName("刷新Token失败-Redis中不存在该Token")
    void refreshToken_Failure_TokenNotInRedis() {
        Map<String, Object> claims = Map.of(
                Constants.CLAIM_USER_ID, 1L,
                Constants.CLAIM_ROLES, Constants.ROLE_SYS_ADMIN
        );

        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        when(valueOperations.get(Constants.REDIS_REFRESH_TOKEN + "1")).thenReturn(null);

        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.TOKEN_INVALID.getCode());
                });
    }

    @Test
    @DisplayName("刷新Token失败-Token不匹配")
    void refreshToken_Failure_TokenMismatch() {
        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        String anotherToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        when(valueOperations.get(Constants.REDIS_REFRESH_TOKEN + "1")).thenReturn(anotherToken);

        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.TOKEN_INVALID.getCode());
                });
    }

    @Test
    @DisplayName("刷新Token失败-无效的Token格式")
    void refreshToken_Failure_InvalidToken() {
        assertThatThrownBy(() -> authService.refreshToken("invalid.token.format"))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.TOKEN_INVALID.getCode());
                });
    }

    @Test
    @DisplayName("登出成功")
    void logout_Success() {
        String bearerToken = "Bearer valid.token.here";

        authService.logout(bearerToken);

        verify(valueOperations).set(
                eq(Constants.REDIS_TOKEN_BLACKLIST + "valid.token.here"),
                eq("1"),
                eq(28800L),
                eq(TimeUnit.SECONDS)
        );
    }

    @Test
    @DisplayName("生成的Token包含正确的用户信息")
    void login_TokenContainsCorrectUserInfo() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        TokenResponse response = authService.login(request);

        var claims = JwtUtils.parseClaims(response.getAccessToken(), TEST_SECRET);
        assertThat(claims.get(Constants.CLAIM_USER_ID)).isEqualTo(1);
        assertThat(claims.get(Constants.CLAIM_USERNAME)).isEqualTo("admin");
        assertThat(claims.get(Constants.CLAIM_ROLES)).asList().contains(Constants.ROLE_SYS_ADMIN);
    }
}
