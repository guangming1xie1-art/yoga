package com.yoga.front.config;

import io.github.bucket4j.core.Bucket4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final Bucket4j buckets;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
                              Object handler) throws Exception {
        String uri = request.getRequestURI();
        String bucketKey = getBucketKey(uri);
        
        Bucket bucket = buckets.getBucket(bucketKey);
        if (bucket == null) {
            return true;
        }
        
        if (bucket.tryConsume(1)) {
            return true;
        }
        
        response.setStatus(429);
        response.setContentType("application/json");
        response.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}");
        return false;
    }

    private String getBucketKey(String uri) {
        if (uri.contains("/booking")) {
            return "booking_api";
        }
        return "user_api";
    }
}
