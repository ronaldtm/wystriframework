package org.wystriframework.core.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.wystriframework.core.util.ReflectionUtils;

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

}
