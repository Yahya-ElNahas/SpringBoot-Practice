package com.practice.main.common.advice;

import com.practice.main.common.exception.DomainException;
import com.practice.main.common.response.ErrorResponse;
import com.practice.main.security.handler.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private final AccessDeniedHandler accessDeniedHandler;

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleCodedExceptions(DomainException e, HttpServletRequest request) {
        log.warn(e.getMessage(), e);

        HttpStatus status = HttpStatus.resolve(e.getStatus());

        return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                messageSource.getMessage(e.getMessage(), e.getArgs(), LocaleContextHolder.getLocale()),
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                Instant.now()
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
                Instant.now()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(HttpMessageNotReadableException e,
                                                           HttpServletRequest request
    ) {
        log.warn(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                messageSource.getMessage("invalid.request_body", null, LocaleContextHolder.getLocale()),
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                Instant.now()
        ));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public void handleAccessDeniedException(AuthorizationDeniedException e,
                                                                     HttpServletRequest request,
                                                                     HttpServletResponse response
    ) throws Exception {
        accessDeniedHandler.handle(request, response, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception e, HttpServletRequest request) {
        log.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                e.getMessage(),
                null,
                request.getRequestURI(),
                MDC.get("requestId"),
                Instant.now()
        ));
    }
}