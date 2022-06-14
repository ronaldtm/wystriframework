package org.wystriframework.form.formbuilder;

import java.util.concurrent.atomic.AtomicReference;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;

public class RecordModel<R extends IRecord<E>, E> implements IComponentInheritedModel<R> {

    public class FieldModel<F> implements IModel<F> {
        private final IField<E, F>           field;
        private transient AtomicReference<F> snapshot;
        public FieldModel(IField<E, F> field) {
            this.field = field;
        }
        @Override
        public F getObject() {
            return RecordModel.this.getObject().getValue(field);
        }
        @Override
        public void setObject(F object) {
            if (this.snapshot == null)
                this.snapshot = new AtomicReference<>(getObject());
            RecordModel.this.getObject().setValue(field, object);
        }
        @Override
        public void detach() {
            RecordModel.this.detach();
            this.snapshot = null;
        }
    }

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

    public <F> IModel<F> field(IField<E, F> field) {
        return new FieldModel<>(field);
    }
}
