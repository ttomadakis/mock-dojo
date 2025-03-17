package com.example.level2;

import java.util.List;

/**
 * Repository interface for User data operations.
 * Defines standard CRUD operations for managing users.
 */
public interface UserRepository {
    
    /**
     * Finds a user by their unique identifier
     * 
     * @param userId The user's unique identifier
     * @return The user if found, null otherwise
     */
    User findById(String userId);
    
    /**
     * Retrieves all users from the repository
     * 
     * @return A list of all users
     */
    List<User> findAll();
    
    /**
     * Saves a user to the repository
     * If the user already exists (same ID), it will be updated
     * 
     * @param user The user to save
     */
    void save(User user);
    
    /**
     * Deletes a user from the repository
     * 
     * @param userId The ID of the user to delete
     */
    void delete(String userId);
    
    /**
     * Checks if a user with the given ID exists
     * 
     * @param userId The user ID to check
     * @return true if the user exists, false otherwise
     */
    boolean exists(String userId);
} 