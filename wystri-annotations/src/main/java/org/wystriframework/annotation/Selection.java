package org.wystriframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.wystriframework.core.definition.IOptionsProvider;

@Target({
    ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
@Field
public @interface Selection {

    String[] value() default {};

    Option[] options() default {};

    Class<? extends IOptionsProvider> provider() default IOptionsProvider.class;

    @interface Option {
        String id() default "";
        String value();
    }
}
