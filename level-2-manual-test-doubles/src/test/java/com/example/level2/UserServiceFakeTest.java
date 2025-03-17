package com.example.level2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import com.example.level2.fake.FakeUserRepository;

/**
 * Tests for UserService using a fake UserRepository implementation.
 * These tests demonstrate the benefits of test isolation and control.
 */
@DisplayName("UserService tests with fake implementation")
class UserServiceFakeTest {

    private FakeUserRepository fakeRepository;
    private UserService userService;
    
    @BeforeEach
    void setUp() {
        // Using our fake repository instead of a real one
        fakeRepository = new FakeUserRepository();
        userService = new UserService(fakeRepository);
        
        // Pre-populate with test data
        User user1 = new User("1", "john", "john@example.com");
        User user2 = new User("2", "alice", "alice@example.com");
        User user3 = new User("3", "bob", "bob@example.com", false); // Inactive user
        
        fakeRepository.save(user1);
        fakeRepository.save(user2);
        fakeRepository.save(user3);
    }
    
    @Test
    @DisplayName("Should get user by ID")
    void testGetUserById() {
        // When
        User user = userService.getUserById("1");
        
        // Then
        assertEquals("john", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("Should throw exception when user not found")
    void testUserNotFound() {
        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById("999"));
    }
    
    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        // Given
        User newUser = new User("4", "charlie", "charlie@example.com");
        
        // When
        userService.createUser(newUser);
        
        // Then - Using fake repository to verify
        User savedUser = fakeRepository.findById("4");
        assertNotNull(savedUser);
        assertEquals("charlie", savedUser.getUsername());
        assertEquals("charlie@example.com", savedUser.getEmail());
    }
    
    @Test
    @DisplayName("Should get only active users")
    void testGetActiveUsers() {
        // When
        List<User> activeUsers = userService.getActiveUsers();
        
        // Then
        assertEquals(2, activeUsers.size());
        assertTrue(activeUsers.stream().allMatch(User::isActive));
        assertFalse(activeUsers.stream().anyMatch(user -> "bob".equals(user.getUsername())));
    }
    
    @Test
    @DisplayName("Should deactivate a user")
    void testDeactivateUser() {
        // When
        userService.deactivateUser("1");
        
        // Then
        User user = fakeRepository.findById("1");
        assertFalse(user.isActive());
    }
    
    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    void testUpdateNonExistentUser() {
        // Given
        User nonExistentUser = new User("999", "unknown", "unknown@example.com");
        
        // When, Then
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(nonExistentUser));
    }
    
    @Test
    @DisplayName("Should update an existing user")
    void testUpdateUser() {
        // Given
        User updatedUser = new User("1", "john-updated", "john-updated@example.com");
        
        // When
        userService.updateUser(updatedUser);
        
        // Then
        User user = fakeRepository.findById("1");
        assertEquals("john-updated", user.getUsername());
        assertEquals("john-updated@example.com", user.getEmail());
    }
    
    @Test
    @DisplayName("Should delete a user")
    void testDeleteUser() {
        // When
        userService.deleteUser("1");
        
        // Then
        assertNull(fakeRepository.findById("1"));
    }
    
    @Test
    @DisplayName("Demonstrates control over test scenarios")
    void demonstrateEdgeCaseControl() {
        // Given - Create a new repository with specific test data
        fakeRepository.clear(); // Clear existing data
        
        // When - No data available
        List<User> activeUsers = userService.getActiveUsers();
        
        // Then - We can easily test the empty case
        assertEquals(0, activeUsers.size());
    }
    
    @Test
    @DisplayName("Demonstrates test isolation benefits")
    void demonstrateTestIsolation() {
        // Given - We can manipulate the repository state without affecting other tests
        fakeRepository.clear();
        fakeRepository.save(new User("test", "test-user", "test@example.com"));
        
        // When/Then - Our changes only affect this test
        assertEquals(1, fakeRepository.findAll().size());
        assertEquals("test-user", fakeRepository.findById("test").getUsername());
    }
} 