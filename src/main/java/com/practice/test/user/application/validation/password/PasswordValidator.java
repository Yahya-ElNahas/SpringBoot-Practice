package com.practice.test.user.application.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private int min, max;

    private final MessageSource messageSource;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }

        boolean isValid = value.length() >= min && value.length() <= max;
        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    messageSource.getMessage(
                            "invalid.password",
                            new Object[]{min, max},
                            LocaleContextHolder.getLocale()
                    )
            ).addConstraintViolation();
        }
        return isValid;
    }
}