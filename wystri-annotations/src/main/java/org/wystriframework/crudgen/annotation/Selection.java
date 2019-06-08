package org.wystriframework.crudgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.wystriframework.core.definition.IOptionsProvider;

@Target({
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface Selection {

    String[] value() default {};

    Option[] options() default {};

    Class<? extends IOptionsProvider> provider() default IOptionsProvider.class;

    @interface Option {
        String id() default "";
        String value();
    }
}
