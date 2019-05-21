package org.wystriframework.core.formbuilder.appenders;

import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.EntityFormProcessor;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.component.behavior.AjaxFormComponentUpdatingActionBehavior;

public class IntegerFieldAppender extends AbstractFieldComponentAppender<Integer> {

    @Override
    protected FormComponent<Integer> newFormComponent(RecordModel<? extends IRecord> record, BSFormGroup formGroup, IField<Integer> field) {
        final IField<Integer> ifield = (IField<Integer>) field;

        final FormComponent<Integer> fc = new TextField<>(ifield.getName(), record.field(ifield), Integer.class);

        fc.add(new AjaxFormComponentUpdatingActionBehavior("change")
            .andThen(t -> t.add(formGroup))
            .andThen(t -> fc.send(fc, Broadcast.BUBBLE, new EntityFormProcessor())));

        return fc;
    }

}
