package org.wystriframework.core.definition.constraints;

import static org.apache.commons.lang3.StringUtils.*;

import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;

public interface ISimpleConstraint<T> extends IConstraint<T> {
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