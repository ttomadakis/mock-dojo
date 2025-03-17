package com.example.mock.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating mock objects using Java's Dynamic Proxy API.
 */
public class MockProxyFactory {
    
    private final Map<Object, MockInvocationHandler> mockHandlers = new HashMap<>();
    
    /**
     * Creates a mock for the specified interface.
     * 
     * @param interfaceClass The interface to mock
     * @param <T> The interface type
     * @return A mock implementation of the interface
     * @throws IllegalArgumentException if the provided class is not an interface
     */
    @SuppressWarnings("unchecked")
    public <T> T createMock(Class<T> interfaceClass) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("Cannot mock concrete class - " + 
                    interfaceClass.getName() + " is not an interface");
        }
        
        ClassLoader classLoader = interfaceClass.getClassLoader();
        MockInvocationHandler handler = new MockInvocationHandler();
        
        T mockObject = (T) Proxy.newProxyInstance(
                classLoader,
                new Class<?>[]{interfaceClass},
                handler);
        
        mockHandlers.put(mockObject, handler);
        return mockObject;
    }
    
    /**
     * Configures a mock to return a specific value when a method is called.
     * 
     * @param mockObject The mock object to configure
     * @param methodName The name of the method to stub
     * @param returnValue The value to return when the method is called
     * @throws IllegalArgumentException if the method doesn't exist
     */
    public void when(Object mockObject, String methodName, Object returnValue) {
        MockInvocationHandler handler = getHandlerFor(mockObject);
        
        try {
            Class<?>[] interfaces = mockObject.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new IllegalArgumentException("Mock object does not implement any interfaces");
            }
            
            Method[] methods = interfaces[0].getMethods();
            Method matchedMethod = findMethodByName(methods, methodName);
            
            if (matchedMethod == null) {
                throw new IllegalArgumentException("Method not found: " + methodName);
            }
            
            handler.when(matchedMethod, new Object[0], returnValue);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to stub method: " + methodName, e);
        }
    }
    
    /**
     * Configures a mock to return a specific value when a method is called with specific arguments.
     * 
     * @param mockObject The mock object to configure
     * @param methodName The name of the method to stub
     * @param args The arguments to match against
     * @param returnValue The value to return when the method is called
     * @throws IllegalArgumentException if the method doesn't exist
     */
    public void when(Object mockObject, String methodName, Object[] args, Object returnValue) {
        if (args == null) {
            args = new Object[0];
        }
        
        MockInvocationHandler handler = getHandlerFor(mockObject);
        
        try {
            Class<?>[] interfaces = mockObject.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new IllegalArgumentException("Mock object does not implement any interfaces");
            }
            
            Method[] methods = interfaces[0].getMethods();
            int argCount = args.length;
            Method matchedMethod = findMethodByNameAndArgCount(methods, methodName, argCount);
            
            if (matchedMethod == null) {
                throw new IllegalArgumentException("Method not found: " + methodName + " with " + argCount + " arguments");
            }
            
            handler.when(matchedMethod, args, returnValue);
            
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Failed to stub method: " + methodName + " - Null pointer in argument handling", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to stub method: " + methodName, e);
        }
    }
    
    private Method findMethodByName(Method[] methods, String methodName) {
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    private Method findMethodByNameAndArgCount(Method[] methods, String methodName, int argCount) {
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == argCount) {
                return method;
            }
        }
        return null;
    }
    
    /**
     * Gets the invocation handler for a mock object.
     * 
     * @param mockObject The mock object
     * @return The invocation handler
     * @throws IllegalArgumentException if the object is not a mock
     */
    private MockInvocationHandler getHandlerFor(Object mockObject) {
        if (!mockHandlers.containsKey(mockObject)) {
            throw new IllegalArgumentException("Not a mock object: " + mockObject);
        }
        return mockHandlers.get(mockObject);
    }
    
    /**
     * Verifies that a method on a mock was called a specified number of times.
     * 
     * @param mockObject The mock object
     * @param methodName The method name to verify
     * @param times The expected number of calls
     * @return true if the method was called exactly the specified number of times
     */
    public boolean verify(Object mockObject, String methodName, int times) {
        MockInvocationHandler handler = getHandlerFor(mockObject);
        return handler.getInvocationsForMethod(methodName).size() == times;
    }
    
    /**
     * Gets all invocations for a mock object.
     * 
     * @param mockObject The mock object
     * @return List of all method invocations
     */
    public java.util.List<MethodInvocation> getInvocations(Object mockObject) {
        return getHandlerFor(mockObject).getInvocations();
    }
} 