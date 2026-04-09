package com.practice.test.Infrastructure.Annotations.Validators;

import com.practice.test.Infrastructure.Annotations.Password;
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
        if(value == null) {
            return false;
        }

        boolean isValid = value.length() >= minLength && value.length() <= maxLength;

        if(!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must be between " + minLength + " and " + maxLength + " characters long"
            ).addConstraintViolation();
        }

        return isValid;
    }
}