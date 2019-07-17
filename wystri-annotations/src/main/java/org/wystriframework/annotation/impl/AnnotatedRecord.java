package org.wystriframework.annotation.impl;

import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class AnnotatedRecord<E> implements IRecord<E> {

    private final AnnotatedEntity<E> entity;
    private final E                  object;

    public AnnotatedRecord(AnnotatedEntity<E> entity, E object) {
        this.entity = entity;
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    public AnnotatedRecord(E object) {
        this(new AnnotatedEntity<>((Class<E>) object.getClass()), object);
    }

    @Override
    public E getTargetObject() {
        return object;
    }

    @Override
    public AnnotatedEntity<E> getEntity() {
        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> F getValue(IField<E, F> field) {
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) field;
        return afield.getValue(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> void setValue(IField<E, F> field, F value) {
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) field;
        afield.setValue(object, value);
    }
}
