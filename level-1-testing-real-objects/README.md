# Level 1: Testing with Real Objects

## Overview
This module introduces the basics of testing with real dependencies. The goal is to demonstrate both the benefits and limitations of using actual implementations in tests, setting the stage for understanding why mocking frameworks are necessary in more complex testing scenarios.

## Components
- **UserRepository Interface**: Defines CRUD operations for user data
- **RealUserRepository**: Concrete implementation simulating database operations with intentional delays
- **User Model**: Simple POJO representing user data
- **UserService**: Business logic layer that depends on UserRepository
- **Tests**: JUnit tests using real implementations to highlight testing challenges

## What to Observe
When running the tests in this module, pay special attention to:

1. **Test Execution Speed**: Notice how tests are slow due to simulated database latency
2. **Test Reliability**: Some tests may randomly fail due to simulated database errors
3. **Test Isolation**: Failures in the repository affect service tests

## Running the Tests
To run the tests in this module:

```bash
mvn test
```

## Benefits of Testing with Real Objects
- Tests validate actual integration between components
- No need to set up complex mocking infrastructure
- Tests verify real behavior, not just mocked behavior

## Drawbacks of Testing with Real Objects
- Tests are slow due to simulated database latency
- Tests can be flaky due to random failures
- Difficult to test edge cases
- Tests are not isolated

These limitations will motivate the need for test doubles introduced in the subsequent levels of this project. 