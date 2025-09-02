package com.neoapp.cliente_api.controller.commonExceptions;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> erros = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String erroMessage = error.getDefaultMessage();
            erros.put(fieldName,erroMessage);
        });
        return new ResponseEntity<Map<String,String>>(erros, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(clientNotFoundExceptions.class)
    public ResponseEntity<String> handlerValidationExceptions(clientNotFoundExceptions exceptions){
        return new ResponseEntity<String>(exceptions.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(cpfAlreadyExistsException.class)
    public ResponseEntity <String> handllercpfAlreadyExistsException(cpfAlreadyExistsException exception){
        return new ResponseEntity<String>("Erro: CPF já cadastrado", HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<String> handlePSQLException(PSQLException ex) {
        // O código '23505' no PostgreSQL significa violação de chave única
        if ("23505".equals(ex.getSQLState())) {
            if (ex.getMessage().contains("idx_clients_cpf")) {
                return new ResponseEntity<>("Erro: CPF já cadastrado.", HttpStatus.CONFLICT);
            }
        }
        // Se não for uma violação de chave única, retorne uma resposta genérica
        return new ResponseEntity<>("Erro inesperado no banco de dados.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity <String> handlerConstraintViolationException(ConstraintViolationException exception){
        String errorMessage = exception.getConstraintViolations().iterator().next().getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);

    }
}
