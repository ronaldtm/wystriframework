package org.wystriframework.core.formbuilder.appenders;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;

public class CheckboxFieldAppender extends AbstractFieldComponentAppender<Boolean> {

    @Override
    protected FormComponent<Boolean> newFormComponent(FieldComponentContext<Boolean> ctx) {
        final IField<Boolean> ifield = (IField<Boolean>) ctx.getField();

        ctx.getFormGroup().setMode(BSFormGroup.Mode.CHECK);
        return newBooleanCheckField(ctx.getRecord(), ifield);
    }

    @SuppressWarnings("unchecked")
    private FormComponent<Boolean> newBooleanCheckField(final RecordModel<? extends IRecord> record, IField<Boolean> field) {
        return new CheckBox(field.getName(), record.field(field));
    }
}
