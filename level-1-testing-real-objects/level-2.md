# Java Mocking Framework - Level 2 Implementation Guide

## Project Overview
A Maven-based learning project exploring Java mocking techniques across 7 levels. Focuses on progression from basic testing to building a custom mocking framework. Targets Java 17 with modern testing practices.

### Tech Stack
- Java 17
- Maven (multi-module)
- JUnit 5
- No external mocking libraries (pure Java)

### Project Structure
    mocking-framework/
    ├── level-1-testing-real-objects/
    ├── level-2-manual-test-doubles/  # Current focus
    │   ├── src/main/java/
    │   ├── src/test/java/
    │   ├── pom.xml
    │   └── README.md
    ├── level-3-dynamic-proxies/
    ...

## Level 2: Manual Test Doubles Implementation

### Objective
Replace real dependencies with manually created test doubles (fakes/stubs) to achieve:
- Better test isolation
- Faster test execution
- Controlled test scenarios

### Key Concepts to Showcase
1. Test Double pattern implementation
2. Behavior simulation without real dependencies
3. Tradeoffs of manual maintenance

### Implementation Instructions

#### 1. Create Fake Implementation
- Implement `UserRepository` interface with hardcoded data
- Example structure:

    // src/main/java/com/mocking/fake/UserRepository.java
    public interface UserRepository {
        User findById(String id);
    }

    // src/main/java/com/mocking/fake/FakeUserRepository.java
    public class FakeUserRepository implements UserRepository {
        private final Map<String, User> users = new HashMap<>();
        
        public FakeUserRepository() {
            // Prepopulate test data
            users.put("1", new User("1", "Test User"));
        }

        @Override
        public User findById(String id) {
            return users.get(id);
        }
    }

#### 2. Write Tests Using Fake
- Create test class for UserService
- Example test case:

    // src/test/java/com/mocking/service/UserServiceTest.java
    class UserServiceTest {
        private UserService userService;
        private FakeUserRepository fakeRepository;

        @BeforeEach
        void setup() {
            fakeRepository = new FakeUserRepository();
            userService = new UserService(fakeRepository);
        }

        @Test
        void getUser_returnsUserFromFake() {
            User result = userService.getUser("1");
            assertEquals("Test User", result.getName());
        }
    }

### Best Practices
1. **Isolation**: Each test should get fresh fake instance
2. **Simplicity**: Keep fake implementations minimal
3. **Documentation**: Add javadoc to fake classes explaining their purpose
4. **Naming**: Use clear names like `Fake...`/`Stub...`
5. **State Verification**: Prefer state checks over interaction verification

### Transition to Level 3
- Highlight maintenance challenges in test:
    // Anti-pattern example
    @Test
    void complexTest() {
        FakeUserRepository fake = new FakeUserRepository();
        fake.setSpecialCase(true);  # Demonstrates growing complexity
        ...
    }
- Explain how this leads to maintenance overhead and the need for dynamic solutions

## Implementation Checklist
- [ ] Create fake implementation of core interface
- [ ] Write tests demonstrating faster execution
- [ ] Add documentation explaining fake usage
- [ ] Update module README.md with level objectives
- [ ] Ensure tests run without external dependencies

## Expected Outcomes
- Test execution time reduction compared to Level 1
- Ability to simulate edge cases (e.g., empty responses)
- Clear demonstration of test isolation benefits