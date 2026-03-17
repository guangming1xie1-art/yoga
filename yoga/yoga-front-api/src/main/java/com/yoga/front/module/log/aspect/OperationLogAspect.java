package com.yoga.front.module.log.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoga.common.entity.OperationLog;
import com.yoga.front.module.log.annotation.OperationLog;
import com.yoga.front.module.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class OperationLogAspect {

    private final OperationLogService operationLogService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        long startTime = System.currentTimeMillis();
        OperationLog logEntity = new OperationLog();
        Object result = null;
        Exception exception = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            fillOperationLog(logEntity, operationLog, joinPoint, request, costTime, result, exception);
            operationLogService.saveLog(logEntity);
        }
    }

    private void fillOperationLog(OperationLog logEntity, OperationLog annotation, ProceedingJoinPoint joinPoint, HttpServletRequest request, long costTime, Object result, Exception exception) {
        // 设置模块和操作
        logEntity.setModule(annotation.module());
        logEntity.setAction(annotation.action());
        logEntity.setDescription(annotation.description());

        // 获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            // 这里需要根据实际的用户实体来获取用户ID和姓名
            // 暂时设置为系统默认值，实际使用时需要根据具体情况调整
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                logEntity.setOperatorName((String) principal);
            }
            // 实际项目中需要根据UserDetails获取用户ID和姓名
        }

        // 设置请求信息
        logEntity.setIp(getIpAddress(request));
        logEntity.setHttpMethod(request.getMethod());
        logEntity.setRequestUri(request.getRequestURI());
        logEntity.setCostMs(costTime);

        // 设置响应状态码
        logEntity.setResponseCode(exception == null ? 200 : 500);

        // 设置请求参数（如果需要记录）
        if (annotation.recordParams()) {
            try {
                String params = Arrays.toString(joinPoint.getArgs());
                if (params.length() > 1000) { // 限制参数长度
                    params = params.substring(0, 1000) + "...";
                }
                logEntity.setRequestParams(params);
            } catch (Exception e) {
                log.warn("记录请求参数时发生异常", e);
            }
        }

        logEntity.setCreatedAt(LocalDateTime.now());
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}