package com.practice.test.Infrastructure.Exceptions;

import com.practice.test.Dtos.Responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CodedException.class)
    public ResponseEntity<ErrorResponse> handleCodedExceptions(CodedException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponse(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        ));
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
//
//    }
}