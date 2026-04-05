package com.practice.test.Application.Exceptions;

import com.practice.test.Application.Dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityException(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Database error";

        if(ex.getMessage().contains("PUBLIC.USERS(EMAIL NULLS FIRST)")) {
            message = "Email already exists";
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        ));
    }
}