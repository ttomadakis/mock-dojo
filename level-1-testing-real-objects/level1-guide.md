# Level 1: Testing with Real Objects - Implementation Guide

## Project Overview

The Java Mocking Framework project is a comprehensive educational resource designed to guide learners through the fundamentals and advanced techniques of unit testing and mocking in Java. The project is structured as a Maven multi-module project with seven progressive levels, each building upon the knowledge gained in previous levels.

### Technical Requirements

- **Java Version**: Java 17
- **Build Tool**: Maven
- **Testing Framework**: JUnit 5
- **IDE**: Any Java IDE with Maven support (IntelliJ IDEA, Eclipse, VS Code with Java extensions, etc.)

### Project Structure

The project follows this overall structure:
```
mocking-framework/
├── pom.xml                             # Parent POM with shared dependencies
├── level-1-testing-real-objects/       # Current level
├── level-2-manual-test-doubles/
├── level-3-dynamic-proxies/
├── level-4-bytebuddy-mocking/
├── level-5-object-instantiation/
├── level-6-simple-mocking-framework/
└── level-7-enhancing-framework/        # Optional advanced level
```

### Learning Progression

The project follows a progressive learning path:

1. **Level 1: Testing with Real Objects** - Introduction to basic testing with actual dependencies
2. **Level 2: Manual Test Doubles** - Creating and using hand-written test doubles
3. **Level 3: Mocking Interfaces with Dynamic Proxies** - Using Java's reflection capabilities
4. **Level 4: Mocking Classes with ByteBuddy** - Extending to concrete classes with bytecode manipulation
5. **Level 5: Object Instantiation in Mocking Frameworks** - Understanding constructor bypass techniques
6. **Level 6: Building a Simple Mocking Framework** - Combining techniques into a cohesive API
7. **Level 7: Enhancing the Framework** - Optional advanced features and improvements

Each level is designed to be self-contained but connected to the overall progression, with detailed README.md files and working examples.

## Level 1 Overview

This guide will help you implement Level 1 of the Java Mocking Framework project. In this level, we focus on "Testing with Real Objects" to illustrate the basics of testing and highlight the challenges that arise when using real dependencies in tests. This level serves as the foundation for understanding why mocking frameworks are necessary.

## Objective

The primary goal of Level 1 is to introduce basic testing using real dependencies and demonstrate their limitations, setting the stage for more advanced mocking techniques in subsequent levels.

## Components to Implement

1. **UserRepository Interface**
   - Define an interface for user data operations
   - Include methods for CRUD operations

2. **RealUserRepository Implementation**
   - Create a concrete implementation of UserRepository
   - Should simulate database operations (without requiring an actual database)
   - Include intentional delays to simulate real-world database latency

3. **UserService Class**
   - Business logic layer that depends on UserRepository
   - Should contain methods that utilize the repository

4. **Tests Using Real Objects**
   - Write JUnit tests for UserService using the real RealUserRepository implementation
   - Demonstrate both the benefits and drawbacks of this approach

## Detailed Instructions

### 1. Setting Up the Project Structure

Follow the Maven project structure as outlined in the project overview:
- Create the main code under `src/main/java/`
- Place tests under `src/test/java/`
- Configure pom.xml with Java 17 and JUnit 5 dependencies

Example pom.xml configuration:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.example</groupId>
        <artifactId>mocking-framework</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    
    <artifactId>level-1-testing-real-objects</artifactId>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2. Implementing the UserRepository Interface

The interface should define standard operations for user management:

```java
public interface UserRepository {
    User findById(String userId);
    List<User> findAll();
    void save(User user);
    void delete(String userId);
    // Consider adding a method that might fail occasionally to demonstrate test reliability issues
    boolean exists(String userId);
}
```

### 3. Creating the User Model

Implement a simple User class:

```java
public class User {
    private String id;
    private String username;
    private String email;
    
    // Constructor, getters, setters
}
```

### 4. Implementing RealUserRepository

This implementation should simulate database operations:

```java
public class RealUserRepository implements UserRepository {
    // Use a map to simulate a database
    private final Map<String, User> users = new HashMap<>();
    
    @Override
    public User findById(String userId) {
        // Simulate database latency
        simulateLatency();
        return users.get(userId);
    }
    
    // Implement other methods
    
    private void simulateLatency() {
        try {
            // Add a delay of 100-300ms to simulate database access
            Thread.sleep(100 + new Random().nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // Optionally: Add a method that randomly fails to simulate real-world behavior
    @Override
    public boolean exists(String userId) {
        simulateLatency();
        // 10% chance of random failure to show test stability issues
        if (new Random().nextInt(10) == 0) {
            throw new RuntimeException("Database connection lost");
        }
        return users.containsKey(userId);
    }
}
```

### 5. Implementing UserService

Create a service layer that depends on the repository:

```java
public class UserService {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public User getUserById(String userId) {
        User user = repository.findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        return user;
    }
    
    public List<User> getActiveUsers() {
        return repository.findAll().stream()
                .filter(user -> /* some condition */)
                .collect(Collectors.toList());
    }
    
    // Add business logic methods that use the repository
}
```

### 6. Writing Tests with Real Objects

Create tests that use the real implementation:

```java
public class UserServiceRealTest {
    private UserRepository repository;
    private UserService service;
    
    @BeforeEach
    void setUp() {
        repository = new RealUserRepository();
        service = new UserService(repository);
        
        // Pre-populate the repository with test data
        User user1 = new User("1", "john", "john@example.com");
        repository.save(user1);
        // Add more test users...
    }
    
    @Test
    void testGetUserById() {
        User user = service.getUserById("1");
        assertEquals("john", user.getUsername());
    }
    
    @Test
    void testUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> {
            service.getUserById("nonexistent");
        });
    }
    
    // More tests...
    
    @Test
    void demonstrateSlowTestExecution() {
        long startTime = System.currentTimeMillis();
        
        // Perform multiple repository operations
        for (int i = 0; i < 10; i++) {
            repository.findById("1");
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Test execution time: " + (endTime - startTime) + "ms");
        
        // Not an actual assertion, just to demonstrate the performance issue
        assertTrue(endTime - startTime > 1000, "Operations should take significant time with real implementation");
    }
    
    @Test
    void demonstrateUnpredictableBehavior() {
        // This test might randomly fail due to the random exception in exists()
        assertDoesNotThrow(() -> {
            repository.exists("1");
        });
    }
}
```

## Best Practices for Level 1

1. **Clear Interface Design**
   - Design a clean and focused UserRepository interface
   - Ensure methods have clear purposes and signatures

2. **Realistic Simulation**
   - Make the RealUserRepository behave like a real database (with delays and occasional failures)
   - Use realistic data structures to mimic persistence storage

3. **Test Organization**
   - Structure tests to clearly demonstrate both benefits and drawbacks
   - Include comments explaining what each test demonstrates
   - Consider using @Tag to group related tests

4. **Documentation**
   - Add clear Javadoc comments to interfaces and classes
   - Include comments in tests explaining what aspects they demonstrate

5. **Error Handling**
   - Implement proper exception handling in the service layer
   - Create custom exceptions where appropriate (e.g., UserNotFoundException)

## Discussion Points to Highlight

When implementing Level 1, emphasize the following points through comments and documentation:

1. **Benefits of Testing with Real Objects:**
   - Tests validate actual integration between components
   - No need to set up complex mocking infrastructure
   - Tests verify real behavior, not just mocked behavior

2. **Drawbacks to Highlight:**
   - Tests are slow due to simulated database latency
   - Tests can be flaky due to the random failures (demonstrate this)
   - Difficult to test edge cases (e.g., what if the database returns specific error codes?)
   - Tests are not isolated (failures in the repository will break service tests)

## README.md Content

Include a README.md file in the level-1 directory that explains:
- The purpose of this level
- How to run the tests
- What to observe in the test results (slow execution, occasional failures)
- How this motivates the need for test doubles in the next level

## Conclusion

This implementation will provide a solid foundation for understanding the basic concepts of testing with real dependencies and highlight the challenges that motivate the use of test doubles in subsequent levels.
