package com.practice.main.common.aspect;

import com.practice.main.common.event.LoggingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ApplicationLoggingAspect {

    @AfterReturning(
            pointcut = "com.practice.main.common.aspect.pointcuts.AspectPointcuts.loggingEvent(event)",
            returning = "result",
            argNames = "joinPoint,event,result"
    )
    public void afterSuccess(JoinPoint joinPoint, LoggingEvent event, Object result) {
        log.info("event={} | status=SUCCESS", event.value());
    }
}