package com.practice.test.common.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Object data,
        String path,
        String requestId,
        LocalDateTime time
) {}