package org.wystriframework.core.formbuilder.appenders;

import java.util.Optional;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.definition.constraints.MaxLengthConstraint;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.EntityFormProcessor;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.component.behavior.AjaxFormComponentUpdatingActionBehavior;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;

public class StringFieldAppender extends AbstractFieldComponentAppender<String> {

    @Override
    protected FormComponent<String> newFormComponent(RecordModel<? extends IRecord> record, BSFormGroup formGroup, IField<String> field) {
        final IField<String> stringField = (IField<String>) field;
        final Optional<MaxLengthConstraint> maxLength = stringField.getConstraint(MaxLengthConstraint.class);

        final FormComponent<String> fc = (maxLength.isPresent() && maxLength.get().getMaxLength() > 255)
            ? newLongStringField(record, (IField<String>) field)
            : newStringField(record, (IField<String>) field);

        fc.add(new AjaxFormComponentUpdatingActionBehavior("change")
            .andThen(t -> t.add(formGroup))
            .andThen(t -> fc.send(fc, Broadcast.BUBBLE, new EntityFormProcessor())));

        return fc;
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newStringField(final RecordModel<? extends IRecord> record, IField<String> field) {
        return new TextField<String>(field.getName(), record.field(field));
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newLongStringField(final RecordModel<? extends IRecord> record, IField<String> field) {
        final TextArea<String> comp = new TextArea<>(field.getName(), record.field(field));
        comp.add(IBehaviorShortcutsMixin.$b.attrAppend("rows", "5"));
        return comp;
    }
}
