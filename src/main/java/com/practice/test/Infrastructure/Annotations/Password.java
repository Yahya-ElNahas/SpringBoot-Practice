package com.practice.test.Infrastructure.Annotations;

import com.practice.test.Infrastructure.Annotations.Validators.PasswordAnnotationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordAnnotationValidator.class)
public @interface Password {
    String message() default "{invalid.password}";

    int min() default 3;
    int max() default 12;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
