package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFormat<T> extends Serializable {

    T parse(String s);
    default String format(T v) {
        return String.valueOf(v);
    }
}
