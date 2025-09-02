package com.neoapp.cliente_api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StateValidatorClass.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StateValidator {

    // configurations of anotation custon for validation of acronyms of states
    // brazilian
    String message() default "{endereco.state.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
