package com.example.level2;

/**
 * Exception thrown when a requested user is not found.
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new user not found exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new user not found exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 