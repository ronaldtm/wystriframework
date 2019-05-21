package org.wystriframework.core.formbuilder;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class RecordModel<R extends IRecord> implements IModel<R>, IComponentInheritedModel<R> {

    private Object object;

    public RecordModel(R object) {
        this.object = object;
    }
    public RecordModel(IModel<R> object) {
        this.object = object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public R getObject() {
        return (object instanceof IModel<?>) ? ((IModel<R>) object).getObject() : (R) object;
    }

    @Override
    public void setObject(R object) {
        this.object = object;
    }

    @Override
    public <W> IWrapModel<W> wrapOnInheritance(Component component) {
        return null;
    }

    public <F> IModel<F> field(IField<F> field) {
        return new IModel<F>() {
            @Override
            public F getObject() {
                return RecordModel.this.getObject().getValue(field);
            }
            @Override
            public void setObject(F object) {
                RecordModel.this.getObject().setValue(field, object);
            }
            @Override
            public void detach() {
                RecordModel.this.detach();
            }
        };
    }
}
