package org.wystriframework.crudgen.base;

import static org.apache.commons.lang3.StringUtils.*;

import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;

public class Constraints {

    public static <T> IConstraint<T> required() {
        return ISimpleConstraint.of(c -> (isBlank(c.getRawValue())) ? "field.required" : null);
    }

    public static <T> IConstraint<T> maxLength(int len) {
        return ISimpleConstraint.of(c -> ((len > 0) && (c.getRawValue().length() > len)) ? "field.maxLength" : null);
    }

    private interface ISimpleConstraint<T> extends IConstraint<T> {
        @Override
        default void check(IConstrainable<T> c) {
            final String errorKey = checkSimple(c);
            if (isNotBlank(errorKey))
                c.error(errorKey);
        }
        String checkSimple(IConstrainable<T> c);

        static <T> ISimpleConstraint<T> of(ISimpleConstraint<T> sc) {
            return sc;
        }
    }
}
