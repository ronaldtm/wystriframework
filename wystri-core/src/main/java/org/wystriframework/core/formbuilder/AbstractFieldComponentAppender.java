package org.wystriframework.core.formbuilder;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
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
    protected abstract <E> FormComponent<T> newFormComponent(FieldComponentContext<E, T> ctx);

    @Override
    public <E> IFieldView<E, T> append(FieldComponentContext<E, T> ctx) {
        final BSFormGroup formGroup = ctx.getLayout().newFormGroup();
        ctx.setFormGroup(formGroup);

        final FormComponent<T> fc = newFormComponent(ctx);
        formGroup.add(fc);

        return configure(ctx.setFieldComponent(fc));
    }

    protected <E> IFieldView<E, T> configure(FieldComponentContext<E, T> ctx) {
        final BSFormGroup formGroup = ctx.getFormGroup();
        final FormComponent<T> fieldComponent = ctx.getFieldComponent();
        final IField<E, T> field = ctx.getField();

        final IFieldView<E, T> fieldView = newFieldView(ctx);
        EntityFormProcessor.associateView(fieldComponent, fieldView);

        fieldComponent
            .setLabel(new LabelModel<>(field))
            .add(new ConstraintValidator<>(ctx, field))
            .add(BSValidationStatusBehavior.getInstance())
            .add(new OnAfterProcessedBehavior<>(field, fieldView, ctx.getRecord()));

        final SerializableConsumer<AjaxRequestTarget> refreshGroup = t -> {
            t.add(formGroup);
            new EntityFormProcessor().bubble(fieldComponent);
        };
        addAjaxUpdateBehavior(fieldComponent, refreshGroup, refreshGroup);

        return fieldView;
    }

    protected <E> IFieldView<E, T> newFieldView(FieldComponentContext<E, T> ctx) {
        return new FormComponentFieldView<>(ctx);
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

    private class OnAfterProcessedBehavior<E> extends Behavior {
        private final IField<E, T>                      field;
        private final IFieldView<E, T>                  fieldView;
        private final RecordModel<? extends IRecord<E>, E> record;
        protected OnAfterProcessedBehavior(IField<E, T> field, IFieldView<E, T> fieldView, RecordModel<? extends IRecord<E>, E> record) {
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

    private static class ConstraintValidator<E, F> implements IValidator<F> {
        private final FieldComponentContext<E, F> ctx;
        private final IField<E, F>                field;
        protected ConstraintValidator(FieldComponentContext<E, F> ctx, IField<E, F> field) {
            this.ctx = ctx;
            this.field = field;
        }
        @Override
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void validate(IValidatable<F> validatable) {
            final ValidatableConstrainable<F> constrainable = new ValidatableConstrainable<F>(field, validatable);

            for (IConstraint<? super F> constraint : field.getConstraints(ctx.getRecord().getObject()))
                constraint.check((IConstrainable) constrainable);
        }
    }

    protected static class ValidatableConstrainable<T> implements IConstrainable<T> {
        private final IField<?, T>    field;
        private final IValidatable<T> validatable;
        protected ValidatableConstrainable(IField<?, T> field, IValidatable<T> validatable) {
            this.field = field;
            this.validatable = validatable;
        }
        @Override
        public Class<T> getType() {
            return field.getType();
        }
        @Override
        public T getValue() {
            return validatable.getValue();
        }
        @Override
        public boolean hasError() {
            return !validatable.isValid();
        }
        @Override
        public void error(String key, Map<String, Object> args) {
            validatable.getModel();

            HashMap<String, Object> map = new HashMap<>();
            map.put("label", field.getLabel());
            map.put("field", field.getName());
            map.put("value", validatable.getValue());
            map.putAll(args);

            IModel<? extends Map<String, Object>> argsModel = Model.of(map);
            validatable.error(new ValidationError(WystriConfiguration.get().localizedString(key, argsModel))
                .setVariables(args));
        }
    }

    private static final class LabelModel<T> implements IModel<String> {
        private final IField<?, T> field;
        private LabelModel(IField<?, T> field) {
            this.field = field;
        }
        @Override
        public String getObject() {
            return WystriConfiguration.get().localizedString(field.getLabel());
        }
    }
}
