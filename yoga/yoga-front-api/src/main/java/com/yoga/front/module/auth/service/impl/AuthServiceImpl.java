package com.yoga.front.module.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.yoga.common.constant.Constants;
import com.yoga.common.dto.auth.TokenResponse;
import com.yoga.common.dto.auth.WxLoginRequest;
import com.yoga.common.entity.User;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.result.ResultCode;
import com.yoga.common.util.JwtUtils;
import com.yoga.front.config.JwtProperties;
import com.yoga.front.config.WxProperties;
import com.yoga.front.module.auth.service.AuthService;
import com.yoga.front.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
    private final WxProperties wxProperties;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;

    @Override
    public TokenResponse wxLogin(WxLoginRequest request) {
        try {
            log.info("开始微信登录，code: {}", request.getCode());

            String wxUrl = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                    wxProperties.getLoginUrl(),
                    wxProperties.getAppId(),
                    wxProperties.getAppSecret(),
                    request.getCode());

            String response = restTemplate.getForObject(wxUrl, String.class);
            JSONObject json = JSON.parseObject(response);

            if (json.containsKey("errcode")) {
                log.error("微信登录失败: {}", json.getString("errmsg"));
                throw new BusinessException(ResultCode.WX_LOGIN_FAILED);
            }

            String openid = json.getString("openid");
            String sessionKey = json.getString("session_key");
            String unionid = json.getString("unionid");

            User user = userMapper.selectByOpenid(openid);
            if (user == null) {
                user = new User();
                user.setOpenid(openid);
                user.setUnionid(unionid);
                user.setNickname("微信用户");
                user.setGender(0);
                userMapper.insert(user);
                log.info("新用户注册，openid={}", openid);
            }

            if (user.getDisabled() == 1) {
                throw new BusinessException(ResultCode.USER_DISABLED);
            }

            log.info("微信登录成功，用户ID: {}, openid: {}", user.getId(), openid);
            return buildTokenResponse(user);

        } catch (BusinessException e) {
            log.warn("微信登录失败，业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("微信登录失败，系统异常", e);
            throw new BusinessException(ResultCode.WX_LOGIN_FAILED);
        }
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
