package com.tsjeong.brokerage.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class ArgIndexFinder {

    public static Integer findIndexByAnnotation(
            Parameter[] parameters,
            Class<? extends Annotation> targetAnnotationClass
    ) {
        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameters[i].getAnnotations()) {
                if (annotation.annotationType().equals(targetAnnotationClass)) {
                    return i;
                }
            }
        }
        return null;
    }
}