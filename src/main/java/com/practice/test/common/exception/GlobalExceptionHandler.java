package com.practice.test.common.exception;

import com.practice.test.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ErrorResponse> handleCodedExceptions(GeneralException e, HttpServletRequest request) {
        log.warn(e.getMessage(), e);
        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(
                e.getStatus().value(),
                e.getStatus().getReasonPhrase(),
                messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()),
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                    HttpServletRequest request
    ) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a
                ));

        log.warn("Validation failed: {}", errors, e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed",
                errors,
                request.getRequestURI(),
                MDC.get("requestId"),
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException e,
                                                           HttpServletRequest request
    ) {
        log.warn(e.getMessage(), e);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("invalid.request_body", null, LocaleContextHolder.getLocale()),
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Something went wrong",
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(error);
    }
}