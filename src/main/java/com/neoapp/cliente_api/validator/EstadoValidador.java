package com.neoapp.cliente_api.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EstadoValidadorClass.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EstadoValidador {

   // configurations of anotation custon for validation of acronyms of states brazilian
    String message() default "{endereco.estado.invalido}";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};

}
