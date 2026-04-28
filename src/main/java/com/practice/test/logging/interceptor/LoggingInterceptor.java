package com.practice.test.logging.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler
    ) {
        request.setAttribute("startTime", System.currentTimeMillis());

        log.info(
                "Incoming request: {} {} | requestId={}",
                request.getRequestURI(),
                request.getMethod(),
                request.getAttribute("requestId")
        );
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        long duration = System.currentTimeMillis() - (long)request.getAttribute("startTime");

        log.info(
                "Outgoing response: {} {} | status={} | duration={}",
                request.getRequestURI(),
                request.getMethod(),
                response.getStatus(),
                duration
        );
    }
}