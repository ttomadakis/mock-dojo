package com.example.level1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Real implementation of UserRepository that simulates database operations.
 * This implementation includes intentional delays and occasional failures
 * to represent real-world behavior of a database.
 */
public class RealUserRepository implements UserRepository {
    
    // Map to simulate a database store
    private final Map<String, User> users = new HashMap<>();
    
    // Random generator for simulating latency and errors
    private final Random random = new Random();
    
    @Override
    public User findById(String userId) {
        // Simulate database access latency
        simulateLatency();
        
        // Return a copy of the user to avoid unintended modifications
        User original = users.get(userId);
        if (original == null) {
            return null;
        }
        
        return new User(
            original.getId(),
            original.getUsername(),
            original.getEmail(),
            original.isActive()
        );
    }
    
    @Override
    public List<User> findAll() {
        // Simulate database access latency
        simulateLatency();
        
        // Return copies of users to avoid unintended modifications
        List<User> userList = new ArrayList<>();
        for (User original : users.values()) {
            userList.add(new User(
                original.getId(),
                original.getUsername(),
                original.getEmail(),
                original.isActive()
            ));
        }
        
        return userList;
    }
    
    @Override
    public void save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        // Simulate database access latency
        simulateLatency();
        
        // Store a copy of the user to avoid unintended modifications
        users.put(user.getId(), new User(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.isActive()
        ));
    }
    
    @Override
    public void delete(String userId) {
        // Simulate database access latency
        simulateLatency();
        
        users.remove(userId);
    }
    
    @Override
    public boolean exists(String userId) {
        // Simulate database access latency
        simulateLatency();
        
        // 10% chance of random failure to demonstrate test reliability issues
        if (random.nextInt(10) == 0) {
            throw new RuntimeException("Database connection lost");
        }
        
        return users.containsKey(userId);
    }
    
    /**
     * Helper method to simulate database latency
     */
    private void simulateLatency() {
        try {
            // Add a delay of 100-300ms to simulate database access
            Thread.sleep(100 + random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Simulated database operation was interrupted", e);
        }
    }
} 