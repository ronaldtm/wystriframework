package org.wystriframework.core.util;

import static com.google.common.collect.Sets.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class ReflectionUtils {
    private ReflectionUtils() {}

    public static <T> int getSubclassDistance(Class<? extends T> superclass, Class<? extends T> subclass) {
        return difference(
            newLinkedHashSet(classHierarchy(subclass)),
            newLinkedHashSet(classHierarchy(superclass)))
                .size();
    }

    public static Iterable<Class<?>> classHierarchy(Class<?> baseClass) {
        return () -> new Iterator<Class<?>>() {
            Class<?> cls = baseClass;
            @Override
            public boolean hasNext() {
                return cls.getSuperclass() != null;
            }
            @Override
            public Class<?> next() {
                final Class<?> current = cls;
                cls = cls.getSuperclass();
                return current;
            }
        };
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
}
