package com.practice.test.Application.Dtos;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String message, String path, LocalDateTime time) {
}
