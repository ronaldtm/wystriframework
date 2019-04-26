package org.wystriframework.core.definition;

import java.util.Collections;
import java.util.Map;

public interface IConstrainable<T> {

    String getRawValue();

    T getValue();

    boolean hasError();

    void error(String key, Map<String, Object> args);

    default void error(String key) {
        error(key, Collections.emptyMap());
    }

}
