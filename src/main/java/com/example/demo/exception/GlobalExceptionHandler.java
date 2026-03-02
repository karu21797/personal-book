package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidGoogleBookException.class)
    public ResponseEntity<String> handleInvalid(InvalidGoogleBookException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

/**
 * Global exception handler for REST controllers.
 *
 * <p>
 * This class centralizes exception handling across the entire application.
 * Any exception thrown from controller layers that matches the configured
 * {@link ExceptionHandler} methods will be intercepted here.
 * </p>
 *
 * <p>
 * This implementation currently handles:
 * <ul>
 *     <li>{@link InvalidGoogleBookException}</li>
 * </ul>
 * </p>
 * @author Karunya L
 */