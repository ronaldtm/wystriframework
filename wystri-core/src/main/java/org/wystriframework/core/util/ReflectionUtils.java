package org.wystriframework.core.util;

import static com.google.common.collect.Sets.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public static Stream<Field> allDeclaredFields(Class<?> type) {
        return Lists.reverse(classHierarchy(type, Object.class)).stream()
            .flatMap(cls -> Stream.of(cls.getDeclaredFields()));
    }

}
