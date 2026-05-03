package com.practice.main.dashboard.application.dto.request;

import java.util.List;

public record TaskRequest(
        String feature,
        String method,
        List<Object> args
) {}