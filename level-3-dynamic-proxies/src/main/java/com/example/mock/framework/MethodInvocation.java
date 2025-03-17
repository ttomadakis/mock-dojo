package com.example.mock.framework;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * Stores information about a method invocation including the method and its arguments.
 */
public class MethodInvocation {
    private final Method method;
    private final Object[] arguments;

    public MethodInvocation(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments != null ? arguments.clone() : new Object[0];
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments.clone(); // Return a defensive copy
    }

    public String getMethodName() {
        return method.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        MethodInvocation that = (MethodInvocation) o;
        
        if (!Objects.equals(method, that.method)) return false;
        return Arrays.deepEquals(arguments, that.arguments);
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + Arrays.deepHashCode(arguments);
        return result;
    }

    @Override
    public String toString() {
        return method.getName() + "(" + 
                (arguments.length > 0 ? Arrays.deepToString(arguments) : "") + ")";
    }
} 