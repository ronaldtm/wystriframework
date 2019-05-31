package org.wystriframework.crudgen.annotation.impl;

import java.util.stream.Stream;

import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IFieldLayout;
import org.wystriframework.core.util.ReflectionUtils;
import org.wystriframework.crudgen.annotation.Field;

public class AnnotatedEntity<T> implements IEntity {

    private String   name;

    private Class<T> objectClass;

    public AnnotatedEntity(Class<T> type) {
        this.name = type.getSimpleName();
        this.objectClass = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stream<AnnotatedField<T, ?>> fields() {
        return ReflectionUtils.allDeclaredFields(getObjectClass())
            .filter(field -> ReflectionUtils.isAnnotationPresent(field, Field.class))
            .map(field -> new AnnotatedField<>(this, field));
    }

    @Override
    public IFieldLayout getLayout() {
        return new AnnotatedFieldLayout<>(this);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getObjectClass() {
        return objectClass;
    }
}
