# Java Mocking Framework Project - Level 3 Implementation Guide

## Project Overview
A Maven-based multi-module project teaching mocking framework implementation through 7 progressive levels. Focuses on practical understanding of test doubles, dynamic proxies, bytecode manipulation, and framework design.

### Tech Stack
- Java 17
- Maven 3.8+
- JUnit 5
- ByteBuddy (later levels)
- Dynamic Proxies (current level)

### Project Structure
    mocking-framework/
    ├── level-3-dynamic-proxies/
        ├── src/main/java/
        ├── src/test/java/ 
        ├── pom.xml
        ├── README.md

## Level 3: Mocking Interfaces with Dynamic Proxies

### Objective
Implement interface mocking using Java's built-in Dynamic Proxy API to:
1. Generate runtime implementations of interfaces
2. Enable basic method stubbing
3. Track method invocations for verification

### Key Concepts to Showcase
- java.lang.reflect.Proxy class usage
- InvocationHandler implementation
- Basic argument recording
- Return value stubbing

### Implementation Instructions

1. Create Proxy Factory Class
   - Implement a MockProxyFactory with createMock() method
   - Handle registration of interface types
   - Store invocation history

2. Implement Invocation Handler
   - Capture method calls and arguments
   - Allow stubbing return values
   - Handle Object class methods (toString/equals)

3. Test Implementation
   - Mock UserRepository interface
   - Verify findByEmail() method calls
   - Test stubbed return values

### Best Practices
- Keep proxy logic focused on recording/returning values
- Use type-safe method dispatch
- Clear separation between stubbing and verification
- Handle edge cases for Object methods
- Maintain thread-safe invocation records

### Code Examples

UserRepository Interface:

    public interface UserRepository {
        User findByEmail(String email);
        void save(User user);
    }

Test Class Structure:

    class DynamicProxyTest {
        private UserRepository mockRepo;
        private MockInvocationHandler handler;
        
        @BeforeEach
        void setup() {
            handler = new MockInvocationHandler();
            mockRepo = (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                handler
            );
        }
    }

Invocation Handler Implementation:

    public class MockInvocationHandler implements InvocationHandler {
        private final Map<String, Object> stubbedResponses = new HashMap<>();
        private final List<MethodInvocation> invocations = new ArrayList<>();
        
        public Object invoke(Object proxy, Method method, Object[] args) {
            invocations.add(new MethodInvocation(method, args));
            
            if (stubbedResponses.containsKey(method.getName())) {
                return stubbedResponses.get(method.getName());
            }
            
            return null;
        }
        
        public void when(String methodName, Object response) {
            stubbedResponses.put(methodName, response);
        }
    }

Verification Test Example:

    @Test
    void shouldRecordMethodInvocations() {
        mockRepo.findByEmail("test@example.com");
        mockRepo.save(new User());
        
        assertThat(handler.getInvocations())
            .hasSize(2)
            .extracting(MethodInvocation::methodName)
            .containsExactly("findByEmail", "save");
    }

### Transition to Next Level
This implementation's interface limitation naturally leads to Level 4 where we'll use ByteBuddy to mock concrete classes through bytecode manipulation.