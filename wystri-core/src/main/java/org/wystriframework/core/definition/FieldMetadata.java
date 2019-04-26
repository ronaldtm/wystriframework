package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class FieldMetadata implements Serializable {

    private LinkedHashMap<Class<? extends Serializable>, Serializable> registry = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T get(Class<T> type) {
        return (T) registry.get(type);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> FieldMetadata put(T obj) {
        registry.put(obj.getClass(), obj);
        return this;
    }
}
