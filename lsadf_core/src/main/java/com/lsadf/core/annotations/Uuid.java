package com.lsadf.core.annotations;

import com.lsadf.core.annotations.validator.UuidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
  ElementType.PARAMETER,
  ElementType.FIELD
}) // Can be applied to method parameters and fields
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UuidValidator.class)
public @interface Uuid {
  String message() default "Invalid UUID format";

  boolean nullable() default false;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
