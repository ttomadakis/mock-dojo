package com.example.level2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.level2.fake.FakeUserRepository;

/**
 * This test class demonstrates the maintenance challenges that arise
 * when using manually created test doubles, motivating the need for
 * dynamic proxies in level 3.
 */
@DisplayName("Demonstrating maintenance challenges with manual test doubles")
class MaintenanceChallengesTest {

    // Extended version of FakeUserRepository that adds special case logic
    private static class ComplexFakeUserRepository extends FakeUserRepository {
        private boolean specialCaseFlag;
        private boolean simulateNetworkFailure;
        private boolean slowResponseMode;
        private int callCount = 0;
        
        public void setSpecialCase(boolean flag) {
            this.specialCaseFlag = flag;
        }
        
        public void setSimulateNetworkFailure(boolean flag) {
            this.simulateNetworkFailure = flag;
        }
        
        public void setSlowResponseMode(boolean flag) {
            this.slowResponseMode = flag;
        }
        
        @Override
        public User findById(String userId) {
            callCount++;
            
            if (simulateNetworkFailure && callCount % 3 == 0) {
                throw new RuntimeException("Simulated network failure");
            }
            
            if (slowResponseMode) {
                try {
                    Thread.sleep(500); // Simulate slow response
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (specialCaseFlag && "special".equals(userId)) {
                return new User("special", "Special User", "special@example.com");
            }
            
            return super.findById(userId);
        }
        
        @Override
        public boolean exists(String userId) {
            if (simulateNetworkFailure) {
                throw new RuntimeException("Simulated network failure");
            }
            
            if (specialCaseFlag && "special".equals(userId)) {
                return true;
            }
            
            return super.exists(userId);
        }
    }
    
    @Test
    @DisplayName("Anti-pattern: Complex test with growing complexity")
    void complexTest() {
        // Given - Special case setup
        ComplexFakeUserRepository fake = new ComplexFakeUserRepository();
        fake.setSpecialCase(true);
        
        UserService service = new UserService(fake);
        
        // When/Then
        User specialUser = service.getUserById("special");
        assertEquals("Special User", specialUser.getUsername());
        
        // More test scenarios requiring different behavior
        fake.setSimulateNetworkFailure(true);
        assertThrows(RuntimeException.class, () -> service.createUser(new User("1", "test")));
        
        // Reset and try another case
        fake.setSimulateNetworkFailure(false);
        fake.setSlowResponseMode(true);
        
        // Test continues with more special case handling...
        // This demonstrates how test complexity grows with more special cases
    }
    
    @Test
    @DisplayName("Anti-pattern: Multiple fake implementations needed")
    void multipleImplementationsNeeded() {
        // As requirements grow, we need multiple fake implementations
        // or increasingly complex configuration options
        
        // For testing normal cases
        FakeUserRepository normalFake = new FakeUserRepository();
        
        // For testing special cases
        ComplexFakeUserRepository specialFake = new ComplexFakeUserRepository();
        specialFake.setSpecialCase(true);
        
        // Different service instances for different test scenarios
        UserService normalService = new UserService(normalFake);
        UserService specialService = new UserService(specialFake);
        
        // Tests that use both services with different behaviors
        // require careful maintenance when the underlying interface changes
    }
    
    @Test
    @DisplayName("Anti-pattern: Hard-to-update fakes when interface changes")
    void hardToUpdateOnInterfaceChange() {
        // When UserRepository interface changes (e.g., adds new methods),
        // all fake implementations must be updated
        
        // Imagine we want to add a new method to UserRepository:
        // List<User> findByUsername(String username);
        
        // We would need to update:
        // 1. FakeUserRepository
        // 2. ComplexFakeUserRepository
        // 3. Any other fake implementations
        
        // This maintenance burden increases with each new fake
        // and becomes harder to manage over time
    }
} 