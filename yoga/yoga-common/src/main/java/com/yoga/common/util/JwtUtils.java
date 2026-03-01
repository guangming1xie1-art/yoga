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
 */
@Slf4j
public final class JwtUtils {

    private JwtUtils() {}

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

    public static Claims parseClaimsAllowExpired(String token, String secret) {
        try {
            return parseClaims(token, secret);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static Claims parseClaims(String token, String secret) {
        SecretKey key = buildKey(secret);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getSubject(String token, String secret) {
        return parseClaims(token, secret).getSubject();
    }

    public static boolean isValid(String token, String secret) {
        try {
            parseClaims(token, secret);
            return true;
        } catch (Exception e) {
            log.debug("Token 校验失败: {}", e.getMessage());
            return false;
        }
    }

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
