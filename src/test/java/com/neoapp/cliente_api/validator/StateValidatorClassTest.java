package com.neoapp.cliente_api.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class StateValidatorClassTest {

    private StateValidatorClass validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new StateValidatorClass();
    }

    @Test
    void testIsValid_ValidStates() {
        assertTrue(validator.isValid("SP", context));
        assertTrue(validator.isValid("RJ", context));
        assertTrue(validator.isValid("MG", context));
        assertTrue(validator.isValid("RS", context));
    }

    @Test
    void testIsValid_ValidStatesCaseInsensitive() {
        assertTrue(validator.isValid("sp", context));
        assertTrue(validator.isValid("Sp", context));
        assertTrue(validator.isValid("sP", context));
    }

    @Test
    void testIsValid_NullAndEmpty() {
        assertFalse(validator.isValid(null, context));
        assertFalse(validator.isValid("", context));
        assertFalse(validator.isValid("   ", context));
    }

    @ParameterizedTest
    @ValueSource(strings = { "XX", "ZZ", "AB", "CD", "INVALID" })
    void testIsValid_InvalidStates(String invalidState) {
        assertFalse(validator.isValid(invalidState, context));
    }

    @Test
    void testIsValid_AllValidStates() {
        String[] validStates = {
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
                "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
                "RS", "RO", "RR", "SC", "SP", "SE", "TO"
        };

        for (String state : validStates) {
            assertTrue(validator.isValid(state, context), "State " + state + " should be valid");
        }
    }

    @Test
    void testIsValid_WithWhitespace() {
        assertFalse(validator.isValid(" SP ", context));
        assertFalse(validator.isValid("\tRJ\t", context));
    }
}
