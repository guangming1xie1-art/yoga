package com.yoga.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类
 * 统一管理 token 生成、解析、校验
 */
@Slf4j
public final class JwtUtils {

    private JwtUtils() {}

    /**
     * 生成 JWT Token
     *
     * @param subject    主题（通常是 userId）
     * @param extraClaims 附加 Claims（如 roles、username）
     * @param secret     签名密钥（至少32字节）
     * @param ttl        有效期
     * @return JWT 字符串
     */
    public static String generateToken(String subject, Map<String, Object> extraClaims,
                                       String secret, Duration ttl) {
        SecretKey key = buildKey(secret);
        Instant now = Instant.now();
        Instant exp = now.plus(ttl);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(extraClaims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token 返回 Claims（不验证过期，适合刷新场景）
     */
    public static Claims parseClaimsAllowExpired(String token, String secret) {
        try {
            return parseClaims(token, secret);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 解析 Token 返回 Claims（严格校验过期）
     */
    public static Claims parseClaims(String token, String secret) {
        SecretKey key = buildKey(secret);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /** 从 Token 中提取 subject（userId） */
    public static String getSubject(String token, String secret) {
        return parseClaims(token, secret).getSubject();
    }

    /** 校验 token 是否有效 */
    public static boolean isValid(String token, String secret) {
        try {
            parseClaims(token, secret);
            return true;
        } catch (Exception e) {
            log.debug("Token 校验失败: {}", e.getMessage());
            return false;
        }
    }

    /** 是否过期 */
    public static boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private static SecretKey buildKey(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret 不能为空");
        }
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalArgumentException("JWT secret 至少需要32字节（256位）");
        }
        return Keys.hmacShaKeyFor(bytes);
    }
}
