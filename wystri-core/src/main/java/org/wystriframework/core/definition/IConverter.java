package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IConverter<T> extends Serializable {

    T stringToObject(String s);
    String objectToString(T o);
}
