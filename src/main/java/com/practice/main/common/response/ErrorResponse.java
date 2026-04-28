package com.practice.main.common.response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Map<String, String> data,
        String path,
        String requestId,
        Instant timeStamp
) {}