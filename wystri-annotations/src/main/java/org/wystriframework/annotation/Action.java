package org.wystriframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.core.util.NeverPredicate;
import org.wystriframework.annotation.impl.ActionType;

@Target({
    ElementType.METHOD,
    ElementType.ANNOTATION_TYPE
})
public @interface Action {

    ActionType type() default ActionType.SECONDARY;

    String iconSpec() default "";

    boolean transactional() default false;

    Class<? extends SerializablePredicate<?>> enabledIf() default NeverPredicate.class;

    Class<? extends SerializablePredicate<?>> visibleIf() default NeverPredicate.class;
}
