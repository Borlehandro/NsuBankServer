package com.sibdever.nsu_bank_system_server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCredentialsException(String message) {
        super(message);
    }
}