package org.wystriframework.annotation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

abstract class Utils {

    static <A extends Annotation> boolean isAnnotationPresent(AccessibleObject member, Class<A> annotationClass) {
        return resolveAnnotation(member, annotationClass) != null;
    }

    static <A extends Annotation> A resolveAnnotation(AccessibleObject member, Class<A> annotationClass) {
        final A annotation = member.getAnnotation(annotationClass);
        if (annotation != null)
            return annotation;

        for (Annotation ann : member.getAnnotations()) {
            final A subAnnotation = ann.annotationType().getAnnotation(annotationClass);
            if (subAnnotation != null)
                return subAnnotation;
        }
        return null;
    }

    static <T> Type[] getGenericTypesForInterface(Class<? extends T> baseClass, Class<T> interfaceClass) {
        return Stream.of(baseClass.getGenericInterfaces())
            .map(it -> (ParameterizedType) it)
            .filter(it -> it.getRawType() == interfaceClass)
            .map(it -> it.getActualTypeArguments())
            .findFirst()
            .get();
    }

    static <A extends Annotation> List<Method> getAnnotatedMethods(Class<?> type, Class<A> annotationClass) {
        final List<Method> list = new ArrayList<>();
        for (Method method : type.getDeclaredMethods()) {
            final A annotation = method.getAnnotation(annotationClass);
            if (annotation != null)
                list.add(method);
        }
        return list;
    }

    static List<Annotation> getAnnotatedAnnotations(AccessibleObject member, Class<? extends Annotation> metaAnnotationClass) {
        final List<Annotation> list = new ArrayList<>();
        for (Annotation ann : member.getAnnotations()) {
            final Annotation metaAnnotation = ann.annotationType().getAnnotation(metaAnnotationClass);
            if (metaAnnotation != null)
                list.add(ann);
        }
        return list;
    }

    static <A extends Annotation> A getMetaAnnotation(Annotation annotation, Class<A> metaAnnotationClass) {
        return annotation.annotationType().getAnnotation(metaAnnotationClass);
    }
}
