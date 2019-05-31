package org.wystriframework.crudgen.annotation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.wystriframework.core.definition.constraints.LengthConstraint;
import org.wystriframework.crudgen.annotation.Constraint;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(type = LengthConstraint.class)
public @interface Length {
    int min() default 0;
    int max() default 0;
}
