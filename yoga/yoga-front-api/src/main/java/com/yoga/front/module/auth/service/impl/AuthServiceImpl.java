package com.yoga.front.module.auth.service.impl;

import com.yoga.common.constant.Constants;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;
import com.yoga.common.entity.User;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import com.yoga.common.util.JwtUtils;
import com.yoga.front.config.JwtProperties;
import com.yoga.front.module.auth.service.AuthService;
import com.yoga.front.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public TokenResponse wxLogin(WxLoginRequest request) {
        String openid = "mock_openid_" + request.getCode();

        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            userMapper.insert(user);
            log.info("新用户注册，openid={}", openid);
        }

        return buildTokenResponse(user);
    }

    @Override
    public void bindPhone(String token, String phone, String smsCode) {
        throw new BusinessException(ResultCode.INTERNAL_ERROR.getCode(), "待实现：短信验证码绑定");
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
            User user = userMapper.selectById(Long.parseLong(userId));
            if (user == null) throw new BusinessException(ResultCode.USER_NOT_FOUND);
            return buildTokenResponse(user);
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

    private TokenResponse buildTokenResponse(User user) {
        Map<String, Object> claims = Map.of(
                Constants.CLAIM_USER_ID, user.getId(),
                Constants.CLAIM_ROLES, List.of(Constants.ROLE_USER)
        );

        String accessToken = JwtUtils.generateToken(
                String.valueOf(user.getId()), claims,
                jwtProperties.getSecret(),
                Duration.ofSeconds(jwtProperties.getAccessTokenExpire()));

        String refreshToken = JwtUtils.generateToken(
                String.valueOf(user.getId()), Map.of(),
                jwtProperties.getSecret(),
                Duration.ofSeconds(jwtProperties.getRefreshTokenExpire()));

        redisTemplate.opsForValue().set(
                Constants.REDIS_REFRESH_TOKEN + user.getId(),
                refreshToken,
                jwtProperties.getRefreshTokenExpire(), TimeUnit.SECONDS);

        return new TokenResponse(accessToken, refreshToken,
                jwtProperties.getAccessTokenExpire(), "Bearer");
    }
}
