package com.practice.test.Infrastructure.Exceptions;

import com.practice.test.Dtos.Responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleCodedExceptions(GeneralException ex, HttpServletRequest request) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorResponse(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                messageSource.getMessage(ex.getMessage(), ex.getArgs(), LocaleContextHolder.getLocale()),
                request.getRequestURI(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String errorMessage = String.join("; ", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessage,
                request.getRequestURI(),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleInvalidJson(HttpMessageNotReadableException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid enum value, role should be: 'USER' or 'ADMIN'",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }
}