package org.wystriframework.form.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Optional;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.definition.constraints.LengthConstraint;
import org.wystriframework.core.wicket.Wystri;
import org.wystriframework.form.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.form.formbuilder.FieldComponentContext;
import org.wystriframework.form.formbuilder.RecordModel;
import org.wystriframework.ui.util.IBehaviorShortcutsMixin;

public class StringFieldAppender extends AbstractFieldComponentAppender<String> {

    @Override
    protected <E> FormComponent<String> newFormComponent(FieldComponentContext<E, String> ctx) {
        final IField<E, String> stringField = (IField<E, String>) ctx.getField();
        final Optional<LengthConstraint> maxLength = stringField.getConstraint(ctx.getRecord().getObject(), LengthConstraint.class);

        return (maxLength.isPresent() && maxLength.get().getMax() > 255)
            ? newLongStringField(ctx.getRecord(), stringField)
            : newStringField(ctx.getRecord(), stringField);
    }

    @SuppressWarnings("unchecked")
    private <E> FormComponent<String> newStringField(final RecordModel<? extends IRecord<E>, E> record, IField<E, String> field) {
        return new TextField<String>(field.getName(), record.field(field)) {
            @Override
            protected void reportRequiredError() {
                final String msg = field.requiredError(record.getObject());
                if (isNotBlank(msg)) {
                    super.error(Wystri.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private <E> FormComponent<String> newLongStringField(final RecordModel<? extends IRecord<E>, E> record, IField<E, String> field) {
        final TextArea<String> comp = new TextArea<>(field.getName(), record.field(field));
        comp.add(IBehaviorShortcutsMixin.$b.attrAppend("rows", "5"));
        return comp;
    }
}
