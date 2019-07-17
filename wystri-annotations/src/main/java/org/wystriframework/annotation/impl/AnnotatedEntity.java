package org.wystriframework.annotation.impl;

import java.util.stream.Stream;

import org.wystriframework.annotation.Field;
import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IFieldLayout;
import org.wystriframework.core.util.ReflectionUtils;

public class AnnotatedEntity<E> implements IEntity<E> {

    private String   name;

    private Class<E> objectClass;

    public AnnotatedEntity(Class<E> type) {
        this.name = type.getSimpleName();
        this.objectClass = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stream<AnnotatedField<E, ?>> fields() {
        return ReflectionUtils.allDeclaredFields(getObjectClass())
            .filter(field -> ReflectionUtils.isAnnotationPresent(field, Field.class))
            .map(field -> new AnnotatedField<>(this, field));
    }

    @Override
    public IFieldLayout<E> getLayout() {
        return new AnnotatedFieldLayout<E>(this);
    }

    @SuppressWarnings("unchecked")
    public Class<E> getObjectClass() {
        return objectClass;
    }
}
