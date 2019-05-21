package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFormat<T> extends Serializable {

    T parse(String s);
    String format(T v);

    default String display(T v) {
        return format(v);
    }
}
