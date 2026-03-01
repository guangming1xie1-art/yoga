package com.yoga.backend.module.auth.service.impl;

import com.yoga.common.constant.Constants;
import com.yoga.common.dto.auth.LoginRequest;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import com.yoga.common.util.JwtUtils;
import com.yoga.backend.config.JwtProperties;
import com.yoga.backend.module.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse login(LoginRequest request) {
        // TODO: 验证用户名密码，从数据库查询用户信息和角色
        // Mock implementation
        if (!"admin".equals(request.getUsername()) || !"admin123".equals(request.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        Map<String, Object> claims = Map.of(
                Constants.CLAIM_USER_ID, 1L,
                Constants.CLAIM_USERNAME, request.getUsername(),
                Constants.CLAIM_ROLES, List.of(Constants.ROLE_SYS_ADMIN)
        );

        String accessToken = JwtUtils.generateToken(
                "1", claims,
                jwtProperties.getSecret(),
                Duration.ofSeconds(jwtProperties.getAccessTokenExpire()));

        String refreshToken = JwtUtils.generateToken(
                "1", Map.of(),
                jwtProperties.getSecret(),
                Duration.ofSeconds(jwtProperties.getRefreshTokenExpire()));

        redisTemplate.opsForValue().set(
                Constants.REDIS_REFRESH_TOKEN + "1",
                refreshToken,
                jwtProperties.getRefreshTokenExpire(), TimeUnit.SECONDS);

        return new TokenResponse(accessToken, refreshToken,
                jwtProperties.getAccessTokenExpire(), "Bearer");
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        try {
            String userId = JwtUtils.getSubject(refreshToken, jwtProperties.getSecret());
            String key = Constants.REDIS_REFRESH_TOKEN + userId;
            String stored = (String) redisTemplate.opsForValue().get(key);
            if (!refreshToken.equals(stored)) {
                throw new BusinessException(ResultCode.TOKEN_INVALID);
            }
            // TODO: 查询用户信息
            Map<String, Object> claims = Map.of(
                    Constants.CLAIM_USER_ID, Long.parseLong(userId),
                    Constants.CLAIM_ROLES, List.of(Constants.ROLE_SYS_ADMIN)
            );

            String newAccessToken = JwtUtils.generateToken(
                    userId, claims,
                    jwtProperties.getSecret(),
                    Duration.ofSeconds(jwtProperties.getAccessTokenExpire()));

            return new TokenResponse(newAccessToken, refreshToken,
                    jwtProperties.getAccessTokenExpire(), "Bearer");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }
    }

    @Override
    public void logout(String bearerToken) {
        String token = bearerToken.replace(Constants.TOKEN_PREFIX, "");
        String key = Constants.REDIS_TOKEN_BLACKLIST + token;
        redisTemplate.opsForValue().set(key, "1",
                jwtProperties.getAccessTokenExpire(), TimeUnit.SECONDS);
    }
}
