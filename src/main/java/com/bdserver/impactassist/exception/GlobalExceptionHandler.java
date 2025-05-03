package com.bdserver.impactassist.exception;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String titleCaseField = convertCamelCaseToTitleCase(fieldName);
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, titleCaseField + " " + errorMessage);
        });
        Map<String, Object> errors = new HashMap<>();
        errors.put("errors", fieldErrors);
        errors.put("message", "Validation errors");
        return ResponseEntity.badRequest().body(errors);
    }

    private String convertCamelCaseToTitleCase(String camelCase) {
        return Arrays.stream(camelCase.split("(?=[A-Z])"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> handleRequestNotPermitted(RequestNotPermitted ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            InvalidContentTypeException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Invalid content type: " + ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }
}
