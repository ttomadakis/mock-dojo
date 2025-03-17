package com.example.level1;

/**
 * Exception thrown when a user is not found in the repository.
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 