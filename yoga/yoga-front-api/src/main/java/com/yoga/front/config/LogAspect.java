package com.yoga.front.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.yoga.front.module..controller..*.*(..))")
    public void controllerPointcut() {}

    @Pointcut("execution(* com.yoga.front.module..service..*.*(..))")
    public void servicePointcut() {}

    @Around("controllerPointcut()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        if (request != null) {
            log.info("请求开始: {}.{}, URI: {}, Method: {}, Args: {}", 
                    className, methodName, request.getRequestURI(), request.getMethod(), Arrays.toString(args));
        }

        Object result;
        try {
            result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;
            log.info("请求成功: {}.{}, 耗时: {}ms, 结果: {}", 
                    className, methodName, costTime, result);
            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("请求失败: {}.{}, 耗时: {}ms, 异常: {}", 
                    className, methodName, costTime, e.getMessage(), e);
            throw e;
        }
    }

    @Around("servicePointcut()")
    public Object aroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        Object result;
        try {
            result = joinPoint.proceed();
            long costTime = System.currentTimeMillis() - startTime;
            log.debug("服务执行成功: {}.{}, 耗时: {}ms", className, methodName, costTime);
            return result;
        } catch (Throwable e) {
            long costTime = System.currentTimeMillis() - startTime;
            log.error("服务执行失败: {}.{}, 耗时: {}ms, 异常: {}", 
                    className, methodName, costTime, e.getMessage(), e);
            throw e;
        }
    }
}
