package com.practice.test.Infrastructure.Annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordAnnotationValidator implements ConstraintValidator<Password, String> {

    private int minLength, maxLength;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.length() >= minLength && value.length() <= maxLength;
    }
}
