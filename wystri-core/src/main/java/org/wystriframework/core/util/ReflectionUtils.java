package org.wystriframework.core.util;

import static com.google.common.collect.Sets.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public abstract class ReflectionUtils {
    private ReflectionUtils() {}

    public static <T> int getSubclassDistance(Class<? extends T> superclass, Class<? extends T> subclass) {
        return difference(
            newLinkedHashSet(classHierarchy(subclass, Object.class)),
            newLinkedHashSet(classHierarchy(superclass, Object.class)))
                .size();
    }

    public static List<Class<?>> classHierarchy(Class<?> baseClass) {
        return classHierarchy(baseClass, null);
    }

    public static List<Class<?>> classHierarchy(Class<?> baseClass, Class<?> upperLimit) {
        List<Class<?>> list = new ArrayList<>();
        if (upperLimit == null) {
            for (Class<?> cls = baseClass; cls != null; cls = cls.getSuperclass())
                list.add(cls);
        } else {
            for (Class<?> cls = baseClass; (cls != null) && (cls != upperLimit) && upperLimit.isAssignableFrom(cls); cls = cls.getSuperclass())
                list.add(cls);
        }
        return list;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static <T> Type[] getGenericTypesForInterface(Class<? extends T> baseClass, Class<T> interfaceClass) {
        return Stream.of(baseClass.getGenericInterfaces())
            .map(it -> (ParameterizedType) it)
            .filter(it -> it.getRawType() == interfaceClass)
            .map(it -> it.getActualTypeArguments())
            .findFirst()
            .get();
    }

    public static Stream<Field> allDeclaredFields(Class<?> type) {
        return Lists.reverse(classHierarchy(type, Object.class)).stream()
            .flatMap(cls -> Stream.of(cls.getDeclaredFields()));
    }

    public static <A extends Annotation> boolean isAnnotationPresent(AccessibleObject member, Class<A> annotationClass) {
        return resolveAnnotation(member, annotationClass) != null;
    }

    public static <A extends Annotation> A resolveAnnotation(AccessibleObject member, Class<A> annotationClass) {
        final A annotation = member.getAnnotation(annotationClass);
        if (annotation != null)
            return annotation;

        for (Annotation ann : member.getAnnotations()) {
            final A subAnnotation = ann.getClass().getAnnotation(annotationClass);
            if (subAnnotation != null)
                return subAnnotation;
        }
        return null;
    }

    public static List<Annotation> getAnnotatedAnnotations(AccessibleObject member, Class<? extends Annotation> metaAnnotationClass) {
        final List<Annotation> list = new ArrayList<>();
        for (Annotation ann : member.getAnnotations()) {
            final Annotation metaAnnotation = ann.annotationType().getAnnotation(metaAnnotationClass);
            if (metaAnnotation != null)
                list.add(ann);
        }
        return list;
    }

    public static <A extends Annotation> A getMetaAnnotation(Annotation annotation, Class<A> metaAnnotationClass) {
        return annotation.annotationType().getAnnotation(metaAnnotationClass);
    }
}
