package com.pos.medicineApp.utils.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CnpValidator.class)
public @interface CnpValidation {
    String message() default "Invalid CNP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
