package com.neoapp.cliente_api.controller.commonExceptions;

public class clientNotFoundExceptions extends RuntimeException {
    public clientNotFoundExceptions(String message) {
        super(message);
    }
}
