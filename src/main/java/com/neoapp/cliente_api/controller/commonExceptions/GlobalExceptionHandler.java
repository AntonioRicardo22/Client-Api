package com.neoapp.cliente_api.controller.commonExceptions;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        return new ResponseEntity<Map<String,String>>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<String> handlerValidationExceptions(ClientNotFoundException exceptions){
        return new ResponseEntity<String>(exceptions.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity <String> handleCpfAlreadyExistsException(CpfAlreadyExistsException exception){
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, String> errors = new HashMap<>();
        // Verifica se a exceção é por um erro de formatação de data
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            if (ife.getTargetType() != null && LocalDate.class.isAssignableFrom(ife.getTargetType())) {
                String mensagem = "Formato de data inválido. Use o padrão yyyy-MM-dd.";
                errors.put("birthDate", mensagem);
            }
        }
        if (errors.isEmpty()) {
            errors.put("erro", "Corpo da requisição inválido.");
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
