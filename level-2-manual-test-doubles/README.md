# Level 2: Manual Test Doubles

This module demonstrates using manually created test doubles (fakes/stubs) to achieve better test isolation, faster execution, and more controlled test scenarios.

## Objectives

- Replace real dependencies with manually created test doubles
- Understand the test double pattern implementation
- Learn how to simulate behavior without real dependencies
- Identify the tradeoffs of manual test double maintenance

## Key Concepts

### Test Doubles

Test doubles are objects that stand in for real dependencies during testing. They provide a way to isolate the system under test from its dependencies, allowing for faster and more reliable tests.

Types of test doubles:
- **Fake** - A simplified implementation of a dependency
- **Stub** - Returns canned responses for specific inputs
- **Mock** - Records interactions for verification

### Benefits

1. **Isolation** - Tests don't depend on real implementations
2. **Speed** - No database connections or I/O operations 
3. **Determinism** - Predictable test results
4. **Scenario Testing** - Easily simulate edge cases

### Implementation Approach

In this module, we manually implement a `FakeUserRepository` that implements the `UserRepository` interface with hardcoded data. This allows us to test the `UserService` class in isolation.

## Running the Tests

```bash
mvn test
```

## Transitioning to Level 3

While manually created test doubles provide good isolation, they can become increasingly complex to maintain as the number of test scenarios grows. This motivates the need for more dynamic solutions, which we'll explore in Level 3. 