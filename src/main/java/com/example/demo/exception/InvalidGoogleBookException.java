package com.example.demo.exception;

public class InvalidGoogleBookException extends RuntimeException {
    public InvalidGoogleBookException(String message) {
        super(message);
    }
}
/**
 * Custom runtime exception thrown when an invalid or malformed
 * response is received from the Google Books API.
 *
 * <p>
 * This exception is typically raised when:
 * <ul>
 *     <li>The API response is null or incomplete</li>
 *     <li>Required fields are missing</li>
 *     <li>Unexpected data format is encountered</li>
 * </ul>
 * </p>
 *
 * <p>
 * Since this extends {@link RuntimeException}, it is an unchecked
 * exception and does not require explicit handling at the call site.
 * It is centrally handled by {@link GlobalExceptionHandler}.
 * </p>
 *
 * @author Karunya L
 */