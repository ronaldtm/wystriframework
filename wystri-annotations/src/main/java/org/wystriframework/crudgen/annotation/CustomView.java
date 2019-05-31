package org.wystriframework.crudgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.wystriframework.core.definition.IFormat;
import org.wystriframework.core.formbuilder.IFieldComponentAppender;

@Target({
    ElementType.FIELD,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("rawtypes")
public @interface CustomView {

    Class<? extends IFieldComponentAppender> appender() default IFieldComponentAppender.class;
    String[] appenderArgs() default {};

    Class<? extends IFormat> format() default IFormat.class;
    String[] formatArgs() default {};

}
