package com.bdserver.impactassist.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDatabaseExceptions(DataIntegrityViolationException ex) {
        String errorMessage = ex.getMessage();
        Throwable cause = ex.getCause();
        if (cause != null) {
            String message = cause.getMessage();
            if (message.contains("users_email_key")) {
                errorMessage = "Email is already in use.";
            }
        }
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
