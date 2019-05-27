package org.wystriframework.core.formbuilder;

import java.util.Map;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.bootstrap.BSValidationStatusBehavior;

public abstract class AbstractFieldComponentAppender<T> implements IFieldComponentAppender<T> {

    @SuppressWarnings("unchecked")
    protected abstract FormComponent<T> newFormComponent(FieldComponentContext<T> ctx);

    @Override
    public IFieldView<T> append(FieldComponentContext<T> ctx) {
        final BSFormGroup formGroup = ctx.getLayout().newFormGroup();
        ctx.setFormGroup(formGroup);

        final FormComponent<T> fc = newFormComponent(ctx);
        formGroup.add(fc);

        return configure(ctx.setFieldComponent(fc));
    }

    protected IFieldView<T> configure(FieldComponentContext<T> ctx) {
        final BSFormGroup formGroup = ctx.getFormGroup();
        final FormComponent<T> fieldComponent = ctx.getFieldComponent();
        final IField<T> field = ctx.getField();

        final IFieldView<T> fieldView = new FieldViewImpl<>(ctx);
        EntityFormProcessor.associateView(fieldComponent, fieldView);

        fieldComponent
            .setLabel(new LabelModel<>(field))
            .add(new ConstraintValidator<>(field))
            .add(BSValidationStatusBehavior.getInstance())
            .add(new OnAfterProcessedBehavior(field, fieldView, ctx.getRecord()));

        final SerializableConsumer<AjaxRequestTarget> refreshGroup = t -> {
            t.add(formGroup);
            new EntityFormProcessor().bubble(fieldComponent);
        };
        addAjaxUpdateBehavior(fieldComponent, refreshGroup, refreshGroup);

        return fieldView;
    }

    @SuppressWarnings("unchecked")
    protected void addAjaxUpdateBehavior(FormComponent<T> fieldComponent, SerializableConsumer<AjaxRequestTarget> onSuccess, SerializableConsumer<AjaxRequestTarget> onError) {
        fieldComponent.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSuccess.accept(target);
            }
            @Override
            protected void onError(AjaxRequestTarget target, RuntimeException e) {
                onError.accept(target);
            }
        });
    }

    protected static class FieldViewImpl<T> implements IFieldView<T> {
        private final FieldComponentContext<T> ctx;
        protected FieldViewImpl(FieldComponentContext<T> ctx) {
            this.ctx = ctx;
        }
        @Override
        public IField<T> getField() {
            return ctx.getField();
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
            RequestCycle.get().find(AjaxRequestTarget.class).ifPresent(t -> t.add(ctx.getFormGroup()));
        }
    }

    private class OnAfterProcessedBehavior extends Behavior {
        private final IField<T>                      field;
        private final IFieldView<T>                  fieldView;
        private final RecordModel<? extends IRecord> record;
        protected OnAfterProcessedBehavior(IField<T> field, IFieldView<T> fieldView, RecordModel<? extends IRecord> record) {
            this.field = field;
            this.fieldView = fieldView;
            this.record = record;
        }
        @Override
        @SuppressWarnings("unchecked")
        public void onConfigure(Component comp) {
            super.onConfigure(comp);
            field.getDelegate().onAfterProcessed(fieldView, record.getObject());
        }
    }

    private static class ConstraintValidator<T> implements IValidator<T> {
        private final IField<T> field;
        protected ConstraintValidator(IField<T> field) {
            this.field = field;
        }
        @Override
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void validate(IValidatable<T> validatable) {
            final ValidatableConstrainable<T> constrainable = new ValidatableConstrainable<T>(validatable);

            for (IConstraint<?> constraint : field.getConstraints())
                constraint.check((IConstrainable) constrainable);
        }
    }

    protected static class ValidatableConstrainable<T> implements IConstrainable<Object> {
        private final IValidatable<T> validatable;
        protected ValidatableConstrainable(IValidatable<T> validatable) {
            this.validatable = validatable;
        }
        @Override
        public Object getValue() {
            return validatable.getValue();
        }
        @Override
        public boolean hasError() {
            return !validatable.isValid();
        }
        @Override
        public void error(String key, Map<String, Object> args) {
            validatable.error(new ValidationError(WystriConfiguration.get().localizedString(key))
                .setVariables(args));
        }
    }

    private static final class LabelModel<T> implements IModel<String> {
        private final IField<T> field;
        private LabelModel(IField<T> field) {
            this.field = field;
        }
        @Override
        public String getObject() {
            return WystriConfiguration.get().localizedString(field.getLabel());
        }
    }
}
