package com.example.demo.exception;

public class InvalidGoogleBookException extends RuntimeException {
    public InvalidGoogleBookException(String message) {
        super(message);
    }
}
