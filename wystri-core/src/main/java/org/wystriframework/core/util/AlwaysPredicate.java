package org.wystriframework.core.util;

import org.danekja.java.util.function.serializable.SerializablePredicate;

public class AlwaysPredicate implements SerializablePredicate<Object> {
    @Override
    public boolean test(Object t) {
        return true;
    }
}