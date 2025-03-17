package com.example.level2.fake;

import com.example.level2.User;
import com.example.level2.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fake implementation of UserRepository for testing purposes.
 * This class stores users in memory and provides an isolated environment for tests.
 */
public class FakeUserRepository implements UserRepository {
    
    private final Map<String, User> users = new HashMap<>();
    
    /**
     * Default constructor - creates an empty repository
     */
    public FakeUserRepository() {
        // Empty by default
    }
    
    /**
     * Constructor that initializes the repository with predefined test data
     * 
     * @param prePopulate whether to pre-populate with test data
     */
    public FakeUserRepository(boolean prePopulate) {
        if (prePopulate) {
            // Pre-populate with test data
            users.put("1", new User("1", "testuser1", "test1@example.com"));
            users.put("2", new User("2", "testuser2", "test2@example.com"));
            users.put("3", new User("3", "inactive", "inactive@example.com", false));
        }
    }
    
    @Override
    public User findById(String userId) {
        return users.get(userId);
    }
    
    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public void save(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and user ID cannot be null");
        }
        users.put(user.getId(), user);
    }
    
    @Override
    public void delete(String userId) {
        users.remove(userId);
    }
    
    @Override
    public boolean exists(String userId) {
        return users.containsKey(userId);
    }
    
    /**
     * Clears all users from the repository.
     * Useful for test setup/cleanup.
     */
    public void clear() {
        users.clear();
    }
    
    /**
     * Adds a collection of users to the repository.
     * Useful for test setup.
     * 
     * @param usersToAdd the users to add
     */
    public void addUsers(List<User> usersToAdd) {
        for (User user : usersToAdd) {
            save(user);
        }
    }
} 