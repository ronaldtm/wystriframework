package org.wystriframework.core.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.Optional;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.definition.constraints.LengthConstraint;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;

public class StringFieldAppender extends AbstractFieldComponentAppender<String> {

    @Override
    protected FormComponent<String> newFormComponent(FieldComponentContext<String> ctx) {
        final IField<String> stringField = (IField<String>) ctx.getField();
        final Optional<LengthConstraint> maxLength = stringField.getConstraint(LengthConstraint.class);

        return (maxLength.isPresent() && maxLength.get().getMax() > 255)
            ? newLongStringField(ctx.getRecord(), (IField<String>) ctx.getField())
            : newStringField(ctx.getRecord(), (IField<String>) ctx.getField());
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newStringField(final RecordModel<? extends IRecord> record, IField<String> field) {
        return new TextField<String>(field.getName(), record.field(field)) {
            @Override
            protected void reportRequiredError() {
                final String msg = field.requiredError();
                if (isNotBlank(msg)) {
                    super.error(WystriConfiguration.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newLongStringField(final RecordModel<? extends IRecord> record, IField<String> field) {
        final TextArea<String> comp = new TextArea<>(field.getName(), record.field(field));
        comp.add(IBehaviorShortcutsMixin.$b.attrAppend("rows", "5"));
        return comp;
    }
}
