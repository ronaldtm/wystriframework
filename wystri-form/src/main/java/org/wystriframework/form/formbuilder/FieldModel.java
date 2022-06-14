package org.wystriframework.form.formbuilder;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class FieldModel<E, T> implements IWrapModel<T> {

    private final IModel<IRecord<E>> recordModel;
    private final IField<E, T>       field;

    public FieldModel(IModel<IRecord<E>> record, IField<E, T> field) {
        this.recordModel = record;
        this.field = field;
    }

    @Override
    public T getObject() {
        return recordModel.getObject().getValue(field);
    }

    @Override
    public void setObject(T object) {
        recordModel.getObject().setValue(field, object);
    }

    @Override
    public void detach() {
        getRecordModel().detach();
    }

    @Override
    public IModel<?> getWrappedModel() {
        return getRecordModel();
    }

    public IField<E, T> getField() {
        return field;
    }

    public IModel<IRecord<E>> getRecordModel() {
        return recordModel;
    }

    public IRecord<E> getRecord() {
        return getRecordModel().getObject();
    }
}
