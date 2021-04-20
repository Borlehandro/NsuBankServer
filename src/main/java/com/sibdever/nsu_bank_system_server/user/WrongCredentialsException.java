package com.sibdever.nsu_bank_system_server.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongCredentialsException extends Exception {
    public WrongCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}