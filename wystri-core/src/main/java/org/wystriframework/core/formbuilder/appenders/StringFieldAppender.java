package org.wystriframework.core.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Optional;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.definition.constraints.MaxLengthConstraint;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;

public class StringFieldAppender extends AbstractFieldComponentAppender<String> {

    @Override
    protected FormComponent<String> newFormComponent(FieldComponentContext<String> ctx) {
        final IField<String> stringField = (IField<String>) ctx.getField();
        final Optional<MaxLengthConstraint> maxLength = stringField.getConstraint(MaxLengthConstraint.class);

        return (maxLength.isPresent() && maxLength.get().getMaxLength() > 255)
            ? newLongStringField(ctx.getRecord(), (IField<String>) ctx.getField(), ctx.getRequiredErrorMessageSupplier())
            : newStringField(ctx.getRecord(), (IField<String>) ctx.getField(), ctx.getRequiredErrorMessageSupplier());
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newStringField(final RecordModel<? extends IRecord> record, IField<String> field, SerializableSupplier<String> requiredErrorMessageSupplier) {
        return new TextField<String>(field.getName(), record.field(field)) {
            @Override
            protected void reportRequiredError() {
                final String msg = requiredErrorMessageSupplier.get();
                if (isNotBlank(msg)) {
                    super.error(WystriConfiguration.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newLongStringField(final RecordModel<? extends IRecord> record, IField<String> field, SerializableSupplier<String> requiredErrorMessageSupplier) {
        final TextArea<String> comp = new TextArea<>(field.getName(), record.field(field));
        comp.add(IBehaviorShortcutsMixin.$b.attrAppend("rows", "5"));
        return comp;
    }
}
