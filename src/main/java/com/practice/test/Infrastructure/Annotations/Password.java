package com.practice.test.Infrastructure.Annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordAnnotationValidator.class)
public @interface Password {
    String message() default "Invalid password";

    int minLength() default 3;
    int maxLength() default 12;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
