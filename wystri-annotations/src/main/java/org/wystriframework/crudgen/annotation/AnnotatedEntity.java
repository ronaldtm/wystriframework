package org.wystriframework.crudgen.annotation;

import java.util.stream.Stream;

import org.wystriframework.core.definition.IEntity;
import org.wystriframework.core.definition.IFieldLayout;

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
        return Stream.of(getObjectClass().getDeclaredFields())
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
