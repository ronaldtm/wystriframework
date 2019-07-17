package org.wystriframework.annotation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.wystriframework.annotation.Constraint;
import org.wystriframework.core.definition.constraints.RangeConstraint;

@Retention(RUNTIME)
@Target(FIELD)
@Constraint(type = RangeConstraint.class)
public @interface Range {
    String rangeExpression();
}
