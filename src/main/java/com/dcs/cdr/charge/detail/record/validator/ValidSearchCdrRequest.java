package com.dcs.cdr.charge.detail.record.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {SearchCdrRequestValidator.class})
@Documented
public @interface ValidSearchCdrRequest {

    String message() default "Invalid Cdr Request";

    Class<?>[] groups() default {};

    Class<?extends Payload> [] payload() default {};

}
