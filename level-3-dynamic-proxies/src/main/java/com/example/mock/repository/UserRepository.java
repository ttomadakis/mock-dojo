package com.example.mock.repository;

import com.example.mock.model.User;

/**
 * User repository interface for testing the mocking framework.
 */
public interface UserRepository {
    User findByEmail(String email);
    void save(User user);
    boolean exists(Long id);
    int count();
} 