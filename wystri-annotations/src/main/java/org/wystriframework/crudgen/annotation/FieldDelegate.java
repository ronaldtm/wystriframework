package org.wystriframework.crudgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.crudgen.annotation.impl.AnnotatedFieldDelegate;

@Target({
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface FieldDelegate {

    Class<? extends IFieldDelegate> delegate() default AnnotatedFieldDelegate.class;
}
