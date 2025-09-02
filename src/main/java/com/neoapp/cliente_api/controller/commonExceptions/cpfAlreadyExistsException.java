package com.neoapp.cliente_api.controller.commonExceptions;

public class cpfAlreadyExistsException extends RuntimeException {
    public cpfAlreadyExistsException(String message) {
        super(message);
    }
}
