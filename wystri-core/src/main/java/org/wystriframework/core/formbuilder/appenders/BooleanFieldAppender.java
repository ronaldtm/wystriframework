package org.wystriframework.core.formbuilder.appenders;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFormat;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.definition.formats.BooleanFormat;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;

public class BooleanFieldAppender extends AbstractFieldComponentAppender<Boolean> {

    @Override
    protected FormComponent<Boolean> newFormComponent(FieldComponentContext<Boolean> ctx) {
        final IField<Boolean> ifield = (IField<Boolean>) ctx.getField();

        if (ctx.getField().getType().isPrimitive()) {
            ctx.getFormGroup().setMode(BSFormGroup.Mode.CHECK);
            return newBooleanCheckField(ctx.getRecord(), ifield);
        } else {
            return newBooleanSelectField(ctx.getRecord(), ifield);
        }
    }

    @SuppressWarnings("unchecked")
    private FormComponent<Boolean> newBooleanCheckField(final RecordModel<? extends IRecord> record, IField<Boolean> field) {
        return new CheckBox(field.getName(), record.field(field));
    }

    @SuppressWarnings("unchecked")
    private FormComponent<Boolean> newBooleanSelectField(final RecordModel<? extends IRecord> record, IField<Boolean> field) {
        field.getMetadata().put(IFormat.class, new BooleanFormat());
        field.getMetadata().get(IFormat.class);
        final IChoiceRenderer<Boolean> choiceRenderer = new IChoiceRenderer<Boolean>() {
            @Override
            public Object getDisplayValue(Boolean object) {
                return null;
            }
            @Override
            public String getIdValue(Boolean object, int index) {
                return null;
            }
            @Override
            public Boolean getObject(String id, IModel<? extends List<? extends Boolean>> choices) {
                return null;
            }
        };
        return new DropDownChoice<>(field.getName(), record.field(field), new ArrayList<>(asList(true, false)), choiceRenderer) {
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
}
