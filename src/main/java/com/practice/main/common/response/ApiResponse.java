package com.practice.main.common.response;

import org.slf4j.MDC;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String requestId,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, null, data, MDC.get("requestId"), Instant.now());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, MDC.get("requestId"), Instant.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, MDC.get("requestId"), Instant.now());
    }
}