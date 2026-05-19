package com.practice.main.common.logging.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class PerformanceLoggingAspect {

    @Around("com.practice.main.common.aspect.pointcuts.AspectPointcuts.generalLogging()")
    public Object performanceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        log.info("executed {}() in {}ms",
                method.getName(),
                duration
        );

        return result;
    }
}
