package org.wystriframework.form.formbuilder.appenders;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.form.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.form.formbuilder.FieldComponentContext;
import org.wystriframework.form.formbuilder.RecordModel;
import org.wystriframework.ui.bootstrap.BSFormGroup;

@SuppressWarnings("serial")
public class CheckboxFieldAppender extends AbstractFieldComponentAppender<Boolean> {

    @Override
    protected <E> FormComponent<Boolean> newFormComponent(FieldComponentContext<E, Boolean> ctx) {
        final IField<E, Boolean> ifield = (IField<E, Boolean>) ctx.getField();

        ctx.getFormGroup().setMode(BSFormGroup.Mode.CHECK);
        return newBooleanCheckField(ctx.getRecord(), ifield);
    }

    private <E> FormComponent<Boolean> newBooleanCheckField(final RecordModel<? extends IRecord<E>, E> record, IField<E, Boolean> field) {
        return new CheckBox(field.getName(), record.field(field));
    }
}
