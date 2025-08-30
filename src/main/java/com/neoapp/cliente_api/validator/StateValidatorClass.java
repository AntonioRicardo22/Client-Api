package com.neoapp.cliente_api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class StateValidatorClass implements ConstraintValidator<StateValidator, String> {
    // implementação da interface StateValidator, aplicando a lógica para a
    // validação
    private static final List<String> VALID_UFS = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG",
            "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

    @Override
    public boolean isValid(String estado, ConstraintValidatorContext context) {
        if (estado == null || estado.trim().isEmpty()) {
            return false;
        }
        return VALID_UFS.contains(estado.toUpperCase());
    }
}
