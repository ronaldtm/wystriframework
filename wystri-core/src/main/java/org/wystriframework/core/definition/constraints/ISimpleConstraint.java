package org.wystriframework.core.definition.constraints;

import static org.apache.commons.lang3.StringUtils.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;

public interface ISimpleConstraint<T> extends IConstraint<T> {
    @Override
    default void check(IConstrainable<T> c) {
        final String errorKey = checkSimple(c);
        if (isNotBlank(errorKey)) {
            final Map<String, Object> args = new HashMap<>();
            for (PropertyDescriptor pd : PropertyUtils.getPropertyDescriptors(this.getClass())) {
                if (PropertyUtils.isReadable(this, pd.getName())) {
                    try {

                        args.put(pd.getName(), PropertyUtils.getProperty(this, pd.getName()));

                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new RuntimeException(ex.getMessage(), ex);
                    }
                }
            }
            c.error(errorKey, args);
        }
    }

    String checkSimple(IConstrainable<T> c);

    static <T> ISimpleConstraint<T> of(ISimpleConstraint<T> sc) {
        return sc;
    }
}