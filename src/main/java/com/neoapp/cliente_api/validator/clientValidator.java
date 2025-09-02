package com.neoapp.cliente_api.validator;
import br.com.caelum.stella.validation.InvalidStateException;
import lombok.RequiredArgsConstructor;
import br.com.caelum.stella.validation.CPFValidator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class clientValidator {

    // Class of library Caelum Stella  - this check rule of math this cpf is valid

    public  static boolean validarCpf(String cpf){
        CPFValidator cpfValidator = new CPFValidator();
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        try {cpfValidator.assertValid(cpfLimpo);
        return  true;
        } catch (InvalidStateException e) {
            System.out.println("Error " + e.getMessage());
            return false;
        }
    }



}
