package org.wystriframework.crudgen.annotation.impl;

import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class AnnotatedRecord<E> implements IRecord {

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
    public AnnotatedEntity<E> getEntity() {
        return entity;
    }

    public E getObject() {
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> F getValue(IField<F> field) {
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) field;
        return afield.getValue(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> void setValue(IField<F> field, F value) {
        final AnnotatedField<E, F> afield = (AnnotatedField<E, F>) field;
        afield.setValue(object, value);
    }
}
