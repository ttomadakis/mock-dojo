package com.example.level1;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that contains business logic for user operations.
 * This class depends on UserRepository for data access.
 */
public class UserService {
    
    private final UserRepository repository;
    
    /**
     * Constructs a new UserService with the specified repository.
     * 
     * @param repository the repository to use for data access
     */
    public UserService(UserRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }
    
    /**
     * Gets a user by their ID.
     * 
     * @param userId the ID of the user to retrieve
     * @return the user
     * @throws UserNotFoundException if the user is not found
     */
    public User getUserById(String userId) {
        User user = repository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        return user;
    }
    
    /**
     * Creates a new user.
     * 
     * @param user the user to create
     * @throws IllegalArgumentException if the user already exists
     */
    public void createUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }
        
        try {
            if (repository.exists(user.getId())) {
                throw new IllegalArgumentException("User already exists: " + user.getId());
            }
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            // Wrap other repository exceptions
            throw new UserServiceException("Error checking if user exists", e);
        }
        
        repository.save(user);
    }
    
    /**
     * Updates an existing user.
     * 
     * @param user the user to update
     * @throws UserNotFoundException if the user does not exist
     */
    public void updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }
        
        // Check if user exists
        User existingUser = repository.findById(user.getId());
        if (existingUser == null) {
            throw new UserNotFoundException("Cannot update non-existent user: " + user.getId());
        }
        
        repository.save(user);
    }
    
    /**
     * Deletes a user by their ID.
     * 
     * @param userId the ID of the user to delete
     */
    public void deleteUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        
        repository.delete(userId);
    }
    
    /**
     * Gets all active users.
     * 
     * @return a list of all active users
     */
    public List<User> getActiveUsers() {
        return repository.findAll().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
    
    /**
     * Deactivates a user.
     * 
     * @param userId the ID of the user to deactivate
     * @throws UserNotFoundException if the user is not found
     */
    public void deactivateUser(String userId) {
        User user = getUserById(userId);
        user.setActive(false);
        repository.save(user);
    }
}

/**
 * Exception thrown when a general error occurs in the user service.
 */
class UserServiceException extends RuntimeException {
    
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
} 