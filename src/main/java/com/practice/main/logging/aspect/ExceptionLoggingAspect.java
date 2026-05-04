package com.practice.main.logging.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ExceptionLoggingAspect {

    @AfterThrowing(
            pointcut = "com.practice.main.common.aspect.pointcuts.AspectPointcuts.generalLogging()",
            throwing = "e"
    )
    public void exceptionLogging(JoinPoint joinPoint, Throwable e) {
        log.error("method={}() | status=FAILED | cause={}",
                joinPoint.getSignature().getName(),
                e.getClass().getSimpleName()
        );
    }
}
