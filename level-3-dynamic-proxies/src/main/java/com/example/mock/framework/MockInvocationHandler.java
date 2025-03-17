package com.example.mock.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles method invocations for dynamic proxies and provides stubbing capabilities.
 */
public class MockInvocationHandler implements InvocationHandler {
    
    private final List<MethodInvocation> invocations = new CopyOnWriteArrayList<>();
    private final Map<MethodKey, Object> stubbedResponses = new ConcurrentHashMap<>();
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Handle Object methods specially
        if (method.getDeclaringClass() == Object.class) {
            return handleObjectMethod(proxy, method, args);
        }
        
        // Ensure args is never null for consistent handling
        if (args == null) {
            args = new Object[0];
        }
        
        // Record the invocation
        MethodInvocation invocation = new MethodInvocation(method, args);
        invocations.add(invocation);
        
        // Return stubbed response if available
        MethodKey key = new MethodKey(method, args);
        if (stubbedResponses.containsKey(key)) {
            return stubbedResponses.get(key);
        }
        
        // Check for a no-args stub that might match
        MethodKey noArgsKey = new MethodKey(method, new Object[0]);
        if (stubbedResponses.containsKey(noArgsKey)) {
            return stubbedResponses.get(noArgsKey);
        }
        
        // Return default values based on return type
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Void.TYPE)) {
            return null;
        } else if (returnType.isPrimitive()) {
            return getPrimitiveDefaultValue(returnType);
        } else {
            return null;
        }
    }
    
    private Object handleObjectMethod(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();
        
        if ("equals".equals(methodName)) {
            return proxy == args[0];
        } else if ("hashCode".equals(methodName)) {
            return System.identityHashCode(proxy);
        } else if ("toString".equals(methodName)) {
            return "Mock@" + Integer.toHexString(System.identityHashCode(proxy));
        }
        
        return null;
    }
    
    private Object getPrimitiveDefaultValue(Class<?> primitiveType) {
        if (primitiveType == boolean.class) return false;
        if (primitiveType == char.class) return '\u0000';
        if (primitiveType == byte.class) return (byte) 0;
        if (primitiveType == short.class) return (short) 0;
        if (primitiveType == int.class) return 0;
        if (primitiveType == long.class) return 0L;
        if (primitiveType == float.class) return 0.0f;
        if (primitiveType == double.class) return 0.0d;
        throw new IllegalArgumentException("Unsupported primitive type: " + primitiveType);
    }
    
    /**
     * Stubs a method to return a specified value
     * 
     * @param method The method to stub
     * @param args The arguments to match against
     * @param returnValue The value to return when the method is called
     */
    public void when(Method method, Object[] args, Object returnValue) {
        if (method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        
        if (args == null) {
            args = new Object[0]; // Ensure we never store null args
        }
        
        // Create a defensive copy of the args to prevent modification after stubbing
        Object[] argsCopy = Arrays.copyOf(args, args.length);
        
        stubbedResponses.put(new MethodKey(method, argsCopy), returnValue);
    }
    
    /**
     * Gets all recorded method invocations
     */
    public List<MethodInvocation> getInvocations() {
        return new ArrayList<>(invocations);
    }
    
    /**
     * Gets invocations for a specific method
     * 
     * @param methodName The name of the method to filter by
     * @return List of invocations for the specified method
     */
    public List<MethodInvocation> getInvocationsForMethod(String methodName) {
        return invocations.stream()
                .filter(inv -> inv.getMethodName().equals(methodName))
                .toList();
    }
    
    /**
     * Class that serves as a key for the stubbed responses map
     */
    private static class MethodKey {
        private final Method method;
        private final Object[] args;
        
        public MethodKey(Method method, Object[] args) {
            if (method == null) {
                throw new IllegalArgumentException("Method cannot be null");
            }
            this.method = method;
            this.args = args != null ? args.clone() : new Object[0];
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            
            MethodKey methodKey = (MethodKey) o;
            
            if (!Objects.equals(method, methodKey.method)) return false;
            
            // Compare arg length
            if (args.length != methodKey.args.length) return false;
            
            // Compare each argument for equality
            for (int i = 0; i < args.length; i++) {
                if (!Objects.deepEquals(args[i], methodKey.args[i])) {
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public int hashCode() {
            int result = method != null ? method.hashCode() : 0;
            result = 31 * result + Arrays.deepHashCode(args);
            return result;
        }
        
        @Override
        public String toString() {
            return method.getName() + Arrays.deepToString(args);
        }
    }
} 