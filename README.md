# Mocking Framework - Project Restructure

This project has been restructured to organize different levels of mocking framework development into separate branches. The `main` branch now serves as a placeholder and guide to access the code for each level.

## Development Levels

The project is divided into the following levels, each on its own branch:

*   **`level-1-testing-real-objects`**: Focuses on testing with real objects and understanding the limitations that lead to mocking.
*   **`level-2-manual-test-doubles`**: Introduces manual creation of test doubles (fakes, stubs) to isolate units under test.
*   **`level-3-dynamic-proxies`**: Explores the use of Java's dynamic proxies to create mock objects more dynamically.

## How to Access Code for Each Level

To view the code and history for a specific level, you need to check out the corresponding branch. Use the following Git commands:

To see Level 1:
```bash
git checkout level-1-testing-real-objects
```

To see Level 2:
```bash
git checkout level-2-manual-test-doubles
```

To see Level 3:
```bash
git checkout level-3-dynamic-proxies
```

Each branch is a self-contained Maven project representing that stage of development.
