package org.wystriframework.core.util;

import static com.google.common.collect.Sets.*;

import java.util.Iterator;

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
}
