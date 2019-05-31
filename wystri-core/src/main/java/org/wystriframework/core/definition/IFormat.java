package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

public interface IFormat<T> extends Serializable {

    String format(T v);
    T parse(String s);

    default T parse(String s, List<? extends T> options) {
        return parse(s);
    }

    default String display(T v) {
        return format(v);
    }
}
