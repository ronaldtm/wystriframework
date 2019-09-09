package org.wystriframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.danekja.java.util.function.serializable.SerializablePredicate;
import org.wystriframework.annotation.impl.AnnotatedFieldDelegate;
import org.wystriframework.core.definition.IFieldDelegate;

@Target({
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface Field {

    static String KEEP_VALUE = "###KEEPVALUE###";

    String label() default "";

    Bool required() default Bool.UNDEFINED;

    Class<? extends SerializablePredicate> requiredIf() default SerializablePredicate.class;
    String requiredError() default "";

    Class<? extends SerializablePredicate> enabledIf() default SerializablePredicate.class;
    String disabledDefaultValue() default KEEP_VALUE;

    Class<? extends SerializablePredicate> visibleIf() default SerializablePredicate.class;
    String invisibleDefaultValue() default KEEP_VALUE;

    Class<? extends IFieldDelegate> delegate() default AnnotatedFieldDelegate.class;
}
