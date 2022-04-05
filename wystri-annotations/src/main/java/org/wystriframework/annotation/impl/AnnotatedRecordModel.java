package org.wystriframework.annotation.impl;

import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializablePredicate;

@SuppressWarnings("serial")
public class AnnotatedRecordModel<E> implements IModel<AnnotatedRecord<E>> {

    private final IModel<E>          objectRef;
    private SerializablePredicate<E> changeChecker = obj -> true;
    private AnnotatedRecord<E>       record;

    public AnnotatedRecordModel(E object) {
        this.objectRef = () -> object;
    }
    public AnnotatedRecordModel(IModel<E> objectRef) {
        this.objectRef = objectRef;
    }

    @Override
    public AnnotatedRecord<E> getObject() {
        final E object = this.objectRef.getObject();
        if (this.changeChecker.test(object)) {
            this.changeChecker = createChangeChecker(object);
            this.record = new AnnotatedRecord<>(object);
        }
        return this.record;
    }

    protected SerializablePredicate<E> createChangeChecker(E object) {
        final int hash = object.hashCode();
        return it -> hash != it.hashCode();
    }
    @Override
    public void setObject(AnnotatedRecord<E> record) {
        this.record = record;
    }

    @Override
    public void detach() {
        objectRef.detach();
    }
}
