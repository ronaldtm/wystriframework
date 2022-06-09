package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class FieldMetadata implements Serializable {

    private LinkedHashMap<Class<? extends Serializable>, Serializable> registry = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T get(Class<T> key) {
        return (T) registry.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getOrDefault(Class<T> key, T defaultValue) {
        return (T) registry.getOrDefault(key, defaultValue);
    }
    
    public <T extends Serializable> FieldMetadata put(Class<T> key, T obj) {
        registry.put(key, obj);
        return this;
    }
}
