package org.wystriframework.core.util;

import static java.util.Arrays.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ReflectionUtilsTest {

    @Test
    public void getSubclassDistance() {
        assertEquals(0, ReflectionUtils.getSubclassDistance(String.class, String.class));
        assertEquals(1, ReflectionUtils.getSubclassDistance(Object.class, String.class));
        assertEquals(1, ReflectionUtils.getSubclassDistance(CharSequence.class, String.class));

        assertEquals(2, ReflectionUtils.getSubclassDistance(Object.class, Integer.class));

        class A {}
        class B extends A {}
        class C extends B {}
        class D extends C {}
        assertEquals(0, ReflectionUtils.getSubclassDistance(A.class, A.class));
        assertEquals(1, ReflectionUtils.getSubclassDistance(A.class, B.class));
        assertEquals(2, ReflectionUtils.getSubclassDistance(A.class, C.class));
        assertEquals(3, ReflectionUtils.getSubclassDistance(A.class, D.class));
    }

    @Test
    public void classHierarchy() {
        class A extends Object {}
        class B extends A {}
        class C extends B {}
        class D extends C {}

        List<Class<?>> expected = asList(D.class, C.class, B.class, A.class, Object.class);
        assertEquals(expected, ReflectionUtils.classHierarchy(D.class));

        List<Class<?>> limited = asList(D.class, C.class, B.class, A.class);
        assertEquals(limited, ReflectionUtils.classHierarchy(D.class, Object.class));
    }
}
