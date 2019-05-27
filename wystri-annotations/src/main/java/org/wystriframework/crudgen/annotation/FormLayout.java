package org.wystriframework.crudgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
    ElementType.TYPE,
})
@Retention(RetentionPolicy.RUNTIME)
public @interface FormLayout {

    String title() default "";
    Row[] value() default {};
    Section[] sections() default {};

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Section {
        String title() default "";
        Row[] value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Row {
        Cell[] value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface BeginRow {
        int cols() default Integer.MAX_VALUE;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EndRow {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cell {
        static String DEFAULT_SPEC = "col";
        String spec() default DEFAULT_SPEC;
        String name() default "";
    }
}
