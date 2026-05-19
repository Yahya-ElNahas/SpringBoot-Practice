package com.practice.main.common.logging.aspect.pointcuts;

import com.practice.main.common.event.LoggingEvent;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class AspectPointcuts {

    @Pointcut("execution(* com.practice.main..application..*Service.*(..))")
    public void generalLogging() {}

    @Pointcut("@annotation(event)")
    public void loggingEvent(LoggingEvent event) {}
}
