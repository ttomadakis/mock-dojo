package com.example.level1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;

/**
 * Tests for UserService using a real UserRepository implementation.
 * These tests demonstrate both the benefits and drawbacks of testing with real dependencies.
 */
@DisplayName("UserService tests with real implementation")
class UserServiceRealTest {

    private UserRepository repository;
    private UserService service;
    
    @BeforeEach
    void setUp() {
        // Using the real repository implementation
        repository = new RealUserRepository();
        service = new UserService(repository);
        
        // Pre-populate the repository with test data
        User user1 = new User("1", "john", "john@example.com");
        User user2 = new User("2", "alice", "alice@example.com");
        User user3 = new User("3", "bob", "bob@example.com", false); // Inactive user
        
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);
    }
    
    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() {
        // When
        User user = service.getUserById("1");
        
        // Then
        assertEquals("john", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("Should throw exception when user not found")
    void testUserNotFound() {
        // When/Then
        assertThrows(UserNotFoundException.class, () -> {
            service.getUserById("nonexistent");
        });
    }
    
    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        // Given
        User newUser = new User("4", "emma", "emma@example.com");
        
        // When
        service.createUser(newUser);
        
        // Then
        User retrieved = repository.findById("4");
        assertNotNull(retrieved);
        assertEquals("emma", retrieved.getUsername());
    }
    
    @Test
    @DisplayName("Should get only active users")
    void testGetActiveUsers() {
        // When
        List<User> activeUsers = service.getActiveUsers();
        
        // Then
        assertEquals(2, activeUsers.size());
        assertTrue(activeUsers.stream()
                .allMatch(User::isActive));
    }
    
    @Test
    @DisplayName("Should deactivate a user")
    void testDeactivateUser() {
        // When
        service.deactivateUser("1");
        
        // Then
        User user = repository.findById("1");
        assertFalse(user.isActive());
    }
    
    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateNonExistentUser() {
        // Given
        User nonExistentUser = new User("99", "nobody", "nobody@example.com");
        
        // When/Then
        assertThrows(UserNotFoundException.class, () -> {
            service.updateUser(nonExistentUser);
        });
    }
    
    @Test
    @DisplayName("Should update an existing user")
    void testUpdateUser() {
        // Given
        User updatedUser = new User("1", "johnupdated", "johnupdated@example.com");
        
        // When
        service.updateUser(updatedUser);
        
        // Then
        User retrieved = repository.findById("1");
        assertEquals("johnupdated", retrieved.getUsername());
        assertEquals("johnupdated@example.com", retrieved.getEmail());
    }
    
    @Test
    @DisplayName("Should delete a user")
    void testDeleteUser() {
        // When
        service.deleteUser("1");
        
        // Then
        assertNull(repository.findById("1"));
    }
    
    // DEMONSTRATION OF ISSUES WITH REAL DEPENDENCIES
    
    @Test
    @Tag("performance")
    @DisplayName("ISSUE: Demonstrates slow test execution with real repository")
    void demonstrateSlowTestExecution() {
        // Record start time
        long startTime = System.currentTimeMillis();
        
        // Perform multiple repository operations
        for (int i = 0; i < 10; i++) {
            repository.findById("1");
        }
        
        // Calculate execution time
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        System.out.println("Test execution time: " + executionTime + "ms");
        
        // This is not a typical assertion but demonstrates the performance issue
        // Each operation takes ~100-300ms, so 10 operations should take >1000ms
        assertTrue(executionTime > 1000, 
                "Operations should take significant time with real implementation");
    }
    
    @RepeatedTest(5)
    @Tag("reliability")
    @DisplayName("ISSUE: Demonstrates unpredictable test behavior")
    void demonstrateUnpredictableBehavior() {
        // This test might randomly fail due to the random exception in exists()
        User newUser = new User("5", "chris", "chris@example.com");
        
        try {
            service.createUser(newUser);
            // If we get here, the operation succeeded without an exception
            assertTrue(true);
        } catch (UserServiceException e) {
            // This demonstrates that tests with real dependencies can fail unpredictably
            System.out.println("Test failed due to simulated database error: " + 
                    e.getMessage());
            
            // We're not failing the test here because we want to demonstrate
            // that this is an expected issue with real dependencies
            assertTrue(true, "This failure is expected occasionally");
        }
    }
} 