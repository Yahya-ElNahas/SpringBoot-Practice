package com.practice.main.dashboard.application.dto.response;

public record TaskResponse(
        String feature,
        String method,
        Object result,
        String timeTaken
) {}