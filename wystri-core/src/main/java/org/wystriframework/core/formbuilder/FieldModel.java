package org.wystriframework.core.formbuilder;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class FieldModel<T> implements IModel<T>, IWrapModel<T> {

    private final IModel<IRecord> recordModel;
    private final IField<T>       field;

    public FieldModel(IModel<IRecord> record, IField<T> field) {
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

    public IField<T> getField() {
        return field;
    }

    public IModel<IRecord> getRecordModel() {
        return recordModel;
    }

    public IRecord getRecord() {
        return getRecordModel().getObject();
    }
}
