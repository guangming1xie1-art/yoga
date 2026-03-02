package com.yoga.front.module.auth.service;

import com.yoga.common.constant.Constants;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;
import com.yoga.common.entity.User;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import com.yoga.common.util.JwtUtils;
import com.yoga.front.config.JwtProperties;
import com.yoga.front.module.auth.service.impl.AuthServiceImpl;
import com.yoga.front.module.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("用户端认证服务测试")
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String TEST_SECRET = "test-secret-key-at-least-32-characters-long-for-jwt-signing";

    @BeforeEach
    void setUp() {
        when(jwtProperties.getSecret()).thenReturn(TEST_SECRET);
        when(jwtProperties.getAccessTokenExpire()).thenReturn(7200L);
        when(jwtProperties.getRefreshTokenExpire()).thenReturn(604800L);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    @DisplayName("微信登录成功-已存在用户")
    void wxLogin_Success_ExistingUser() {
        WxLoginRequest request = new WxLoginRequest();
        request.setCode("wx_code_123");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setOpenid("mock_openid_wx_code_123");
        existingUser.setNickname("测试用户");

        when(userMapper.selectByOpenid("mock_openid_wx_code_123")).thenReturn(existingUser);

        TokenResponse response = authService.wxLogin(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();
        assertThat(response.getExpiresIn()).isEqualTo(7200L);
        assertThat(response.getTokenType()).isEqualTo("Bearer");

        verify(userMapper).selectByOpenid("mock_openid_wx_code_123");
        verify(userMapper, never()).insert(any(User.class));
        verify(valueOperations).set(
                eq(Constants.REDIS_REFRESH_TOKEN + "1"),
                anyString(),
                eq(604800L),
                eq(TimeUnit.SECONDS)
        );
    }

    @Test
    @DisplayName("微信登录成功-新用户注册")
    void wxLogin_Success_NewUser() {
        WxLoginRequest request = new WxLoginRequest();
        request.setCode("wx_code_new");

        String expectedOpenid = "mock_openid_wx_code_new";

        when(userMapper.selectByOpenid(expectedOpenid)).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            return 1;
        });

        TokenResponse response = authService.wxLogin(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isNotBlank();

        verify(userMapper).selectByOpenid(expectedOpenid);
        verify(userMapper).insert(any(User.class));
    }

    @Test
    @DisplayName("绑定手机号-待实现功能")
    void bindPhone_NotImplemented() {
        assertThatThrownBy(() -> authService.bindPhone("Bearer token", "13800138000", "123456"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("待实现");
    }

    @Test
    @DisplayName("刷新Token成功")
    void refreshToken_Success() {
        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        User user = new User();
        user.setId(1L);
        user.setOpenid("mock_openid_test");

        when(valueOperations.get(Constants.REDIS_REFRESH_TOKEN + "1")).thenReturn(refreshToken);
        when(userMapper.selectById(1L)).thenReturn(user);

        TokenResponse response = authService.refreshToken(refreshToken);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(response.getExpiresIn()).isEqualTo(7200L);
    }

    @Test
    @DisplayName("刷新Token失败-Redis中不存在")
    void refreshToken_Failure_NotInRedis() {
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
    @DisplayName("刷新Token失败-用户不存在")
    void refreshToken_Failure_UserNotFound() {
        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                TEST_SECRET,
                Duration.ofSeconds(604800)
        );

        when(valueOperations.get(Constants.REDIS_REFRESH_TOKEN + "1")).thenReturn(refreshToken);
        when(userMapper.selectById(1L)).thenReturn(null);

        assertThatThrownBy(() -> authService.refreshToken(refreshToken))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getCode()).isEqualTo(ResultCode.USER_NOT_FOUND.getCode());
                });
    }

    @Test
    @DisplayName("刷新Token失败-Token格式无效")
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
                eq(7200L),
                eq(TimeUnit.SECONDS)
        );
    }

    @Test
    @DisplayName("生成的Token包含正确的用户信息")
    void wxLogin_TokenContainsCorrectUserInfo() {
        WxLoginRequest request = new WxLoginRequest();
        request.setCode("wx_test_code");

        User user = new User();
        user.setId(100L);
        user.setOpenid("mock_openid_wx_test_code");

        when(userMapper.selectByOpenid("mock_openid_wx_test_code")).thenReturn(user);

        TokenResponse response = authService.wxLogin(request);

        var claims = JwtUtils.parseClaims(response.getAccessToken(), TEST_SECRET);
        assertThat(claims.get(Constants.CLAIM_USER_ID)).isEqualTo(100);
        assertThat(claims.get(Constants.CLAIM_ROLES)).asList().contains(Constants.ROLE_USER);
    }
}
