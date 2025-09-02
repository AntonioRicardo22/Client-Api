package com.neoapp.cliente_api;

import com.neoapp.cliente_api.controller.dto.clientWithAddressDto;
import com.neoapp.cliente_api.validator.clientValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ClienteApiApplicationTests {

    @Autowired
    Validator validator;

    @Test
	void contextLoads() {
	}

    @Test
    public void salvarTest(){
    }

    @ Test
    public  void testCpf(){
        String cpfTest = "109.191.114-24";

        // LINHA DE DEBBUGING
        System.out.println("Validando string: '" + cpfTest + "' com tamanho: " + cpfTest.length());
        boolean resultado = clientValidator.validarCpf(cpfTest);
        if (resultado == true){
            System.out.println("Cpf Valído");
        }
        else {
            System.out.println("Cpf nÃO -- Valído");
        }

    }

    @Test
    void devePassar_quandoNomeForValido() {
        // Cria a instância do record, passando 'null' para os outros campos
        clientWithAddressDto dto = new clientWithAddressDto("João silva","1091911124", LocalDate.of(2035,6,13), "toni@gmail.com",
                "8197300a0800", "rua , loes angeles, 234",
                "ilha de itamaraca", "12151","PE");
        // Valida apenas o campo 'nome'
        Set<ConstraintViolation<clientWithAddressDto>> violations = validator.validateProperty(dto, "state");

        if (violations.isEmpty()) {
            System.out.println("A validação passou! Nenhuma violação encontrada.");
        } else {
            System.out.println("A validação falhou! Encontradas " + violations.size() + " violações.");
            for (ConstraintViolation<clientWithAddressDto> violation : violations) {
                System.out.println("- " + violation.getMessage());
                // Verifica se a mensagem de erro está presente
                String errorMessage = violations.iterator().next().getMessage();
                System.out.println("Mensagem de erro: " + errorMessage);
            }
        }
        assertTrue(violations.isEmpty());
    }
}
