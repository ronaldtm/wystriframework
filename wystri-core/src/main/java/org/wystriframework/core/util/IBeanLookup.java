package org.wystriframework.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.wystriframework.core.wicket.Wystri;

public interface IBeanLookup {

    <T> T byType(Class<T> type);
    <T> T inject(T bean);

    @SuppressWarnings({ "unchecked" })

    default <T, C> T newInstance(Class<T> type, String[] args) {
        for (Constructor<?> con : type.getConstructors()) {
            if (con.getParameterCount() != args.length)
                continue;

            Class<?>[] argTypes = con.getParameterTypes();
            Object[] convertedArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                convertedArgs[i] = Wystri.get().getConverter(argTypes[i]).stringToObject(args[i]);
            }
            try {
                return (T) con.newInstance(convertedArgs);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new IllegalArgumentException(ex.getMessage(), ex);
            }
        }
        throw new IllegalArgumentException();
    }
}
