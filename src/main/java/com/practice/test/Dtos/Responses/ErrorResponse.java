package com.practice.test.Dtos.Responses;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String message, String path, LocalDateTime time) {}