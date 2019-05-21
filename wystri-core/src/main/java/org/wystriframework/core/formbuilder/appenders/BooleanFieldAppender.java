package org.wystriframework.core.formbuilder.appenders;

import static java.util.Arrays.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.event.Broadcast;
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
import org.wystriframework.core.formbuilder.EntityFormProcessor;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.component.behavior.AjaxFormComponentUpdatingActionBehavior;

public class BooleanFieldAppender extends AbstractFieldComponentAppender<Boolean> {

    @Override
    protected FormComponent<Boolean> newFormComponent(RecordModel<? extends IRecord> record, BSFormGroup formGroup, IField<Boolean> field) {
        final IField<Boolean> ifield = (IField<Boolean>) field;

        final FormComponent<Boolean> fc;
        if (field.getType().isPrimitive()) {
            formGroup.setMode(BSFormGroup.Mode.CHECK);
            fc = newBooleanCheckField(record, ifield);
        } else {
            fc = newBooleanSelectField(record, ifield);
        }

        fc.add(new AjaxFormComponentUpdatingActionBehavior("change")
            .andThen(t -> t.add(formGroup))
            .andThen(t -> fc.send(fc, Broadcast.BUBBLE, new EntityFormProcessor())));

        return fc;
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
        return new DropDownChoice<>(field.getName(), record.field(field), new ArrayList<>(asList(true, false)), choiceRenderer);
    }
}
