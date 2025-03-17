# Level 3: Dynamic Proxies Mocking Framework

This level demonstrates how to build a mocking framework using Java's built-in Dynamic Proxy API.

## Overview

The implementation focuses on:
1. Creating mock objects for interfaces using Java's dynamic proxies
2. Recording method invocations for later verification
3. Stubbing method return values with and without argument matching
4. Providing default values for primitive returns

## Key Components

- `MockProxyFactory`: Creates mock instances and provides an API for stubbing and verification
- `MockInvocationHandler`: Handles method invocations, records them, and returns stubbed values
- `MethodInvocation`: Stores information about method calls including method reference and arguments

## Usage Example

```java
// Create a mock factory
MockProxyFactory factory = new MockProxyFactory();

// Create a mock repository
UserRepository mockRepo = factory.createMock(UserRepository.class);

// Stub a method
User testUser = new User(1L, "test@example.com", "Test User");
factory.when(mockRepo, "findByEmail", testUser);

// Use the mock
User result = mockRepo.findByEmail("test@example.com"); // returns testUser
mockRepo.save(new User()); // does nothing but records the call

// Verify calls
boolean verified = factory.verify(mockRepo, "findByEmail", 1); // true
List<MethodInvocation> invocations = factory.getInvocations(mockRepo); // gets all calls
```

## Limitations

- Can only mock interfaces, not concrete classes
- Argument matching is simple equality-based (no matchers like "any()" or "eq()")
- Method matching is done by name rather than method references

## Project Structure

```
level-3-dynamic-proxies/
├── src/
│   ├── main/java/com/example/mock/
│   │   ├── framework/
│   │   │   ├── MethodInvocation.java
│   │   │   ├── MockInvocationHandler.java
│   │   │   └── MockProxyFactory.java
│   │   ├── model/
│   │   │   └── User.java
│   │   └── repository/
│   │       └── UserRepository.java
│   └── test/java/com/example/mock/
│       └── framework/
│           └── DynamicProxyMockTest.java
└── pom.xml
```

## Running the Tests

```
mvn clean test
```

This level serves as the foundation for Level 4 where we'll use ByteBuddy to overcome the interface-only limitation and mock concrete classes. 