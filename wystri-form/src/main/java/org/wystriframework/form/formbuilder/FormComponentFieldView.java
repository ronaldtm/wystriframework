package org.wystriframework.form.formbuilder;

import java.util.Objects;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.ValidationError;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.wicket.WystriConfiguration;

public class FormComponentFieldView<E, T> implements IFieldView<E, T> {
    private final FieldComponentContext<E, T> ctx;
    private transient boolean                 dirty;
    public FormComponentFieldView(FieldComponentContext<E, T> ctx) {
        this.ctx = ctx;
    }
    @Override
    public IField<E, T> getField() {
        return ctx.getField();
    }
    @Override
    public T getPreviousValue() {
        final IModel<T> model = ctx.getFieldComponent().getModel();
        return (model instanceof ISnapshotModel<?>)
            ? ((ISnapshotModel<T>) model).getLastSnapshot()
            : null;
    }
    @Override
    public T getValue() {
        return ctx.getFieldComponent().getModelObject();
    }
    @Override
    public void setValue(T value) {
        if (!Objects.equals(ctx.getFieldComponent().getModelObject(), value))
            markDirty();
        ctx.getFieldComponent().setModelObject(value);
    }
    @Override
    public void setRequired(boolean required) {
        if (ctx.getFieldComponent().isRequired() != required)
            markDirty();
        ctx.getFieldComponent().setRequired(required);
    }
    @Override
    public void setVisible(boolean visible) {
        if (ctx.getFieldComponent().isVisible() != visible)
            markDirty();
        ctx.getFieldComponent().setVisible(visible);
    }
    @Override
    public void setEnabled(boolean enabled) {
        if (ctx.getFieldComponent().isEnabled() != enabled)
            markDirty();
        ctx.getFieldComponent().setEnabled(enabled);
    }
    @Override
    public void error(String msg) {
        ctx.getFieldComponent().error(new ValidationError(WystriConfiguration.get().localizedString(msg)));
        markDirty();
    }
    @Override
    public void info(String msg) {
        ctx.getFieldComponent().info(new ValidationError(WystriConfiguration.get().localizedString(msg)));
        markDirty();
    }
    @Override
    public void markDirty() {
        if (!dirty)
            RequestCycle.get().find(AjaxRequestTarget.class).ifPresent(t -> t.add(ctx.getFormGroup()));
        this.dirty = true;
    }
    @Override
    public void cleanUp() {
        this.dirty = false;
    }
}