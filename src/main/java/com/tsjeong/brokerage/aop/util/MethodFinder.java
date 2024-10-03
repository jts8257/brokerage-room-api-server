package com.tsjeong.brokerage.aop.util;

import org.aspectj.lang.JoinPoint;

import java.lang.reflect.Method;

public class MethodFinder {
    public static Method findMethodBy(JoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Method[] methods = targetClass.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == joinPoint.getArgs().length) {
                return method;
            }
        }

        throw new NoSuchMethodException("Method " + methodName + " not found in " + targetClass);
    }
}
