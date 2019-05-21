package org.wystriframework.core.formbuilder;

import java.util.Map;
import java.util.Objects;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.wystriframework.core.definition.IConstrainable;
import org.wystriframework.core.definition.IConstraint;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.bootstrap.IBSFormGroupLayout;

public abstract class AbstractFieldComponentAppender<T> implements IFieldComponentAppender<T> {

    @SuppressWarnings("unchecked")
    protected abstract FormComponent<T> newFormComponent(final RecordModel<? extends IRecord> record, BSFormGroup formGroup, IField<T> field);

    @Override
    public IFieldView<T> append(IBSFormGroupLayout layout, RecordModel<? extends IRecord> record, IField<T> field) {
        BSFormGroup formGroup = layout.newFormGroup();
        FormComponent<T> fc = newFormComponent(record, formGroup, field);
        formGroup.add(fc);

        return configure(formGroup, fc, field, record);
    }

    protected IFieldView<T> configure(BSFormGroup formGroup, FormComponent<T> fieldComponent, IField<T> field, RecordModel<? extends IRecord> record) {
        IFieldView<T> fieldView = new FieldViewImpl<>(formGroup, fieldComponent, field);
        EntityFormProcessor.associateView(fieldComponent, fieldView);

        fieldComponent.setLabel(new LabelModel<>(field));
        fieldComponent.add(new ConstraintValidator<>(field));
        fieldComponent.add(new Behavior() {
            @Override
            @SuppressWarnings("unchecked")
            public void onConfigure(Component comp) {
                super.onConfigure(comp);
                field.getDelegate().onAfterProcessed(fieldView, record.getObject());
            }
        });
        return fieldView;
    }

    protected static class FieldViewImpl<T> implements IFieldView<T> {
        private final FormComponent<T> fc;
        private final BSFormGroup      formGroup;
        private final IField<T>        field;
        protected FieldViewImpl(BSFormGroup formGroup, FormComponent<T> fc, IField<T> field) {
            this.fc = fc;
            this.formGroup = formGroup;
            this.field = field;
        }
        @Override
        public IField<T> getField() {
            return field;
        }
        @Override
        public T getValue() {
            return fc.getModelObject();
        }
        @Override
        public void setValue(T value) {
            if (!Objects.equals(fc.getModelObject(), value))
                markDirty();
            fc.setModelObject(value);
        }
        @Override
        public void setRequired(boolean required) {
            if (fc.isRequired() != required)
                markDirty();
            fc.setRequired(required);
        }
        @Override
        public void setVisible(boolean visible) {
            if (fc.isVisible() != visible)
                markDirty();
            fc.setVisible(visible);
        }
        @Override
        public void setEnabled(boolean enabled) {
            if (fc.isEnabled() != enabled)
                markDirty();
            fc.setEnabled(enabled);
        }
        @Override
        public void error(String msg) {
            fc.error(new ValidationError(WystriConfiguration.get().localizedString(msg)));
            markDirty();
        }
        @Override
        public void info(String msg) {
            fc.info(new ValidationError(WystriConfiguration.get().localizedString(msg)));
            markDirty();
        }
        @Override
        public void markDirty() {
            RequestCycle.get().find(AjaxRequestTarget.class).ifPresent(t -> t.add(formGroup));
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
            return WystriConfiguration.get().localizedString(field.getName());
        }
    }
}
