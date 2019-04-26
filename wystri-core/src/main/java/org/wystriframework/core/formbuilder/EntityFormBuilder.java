package org.wystriframework.core.formbuilder;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.bootstrap.BSFormRowLayout;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;

public class EntityFormBuilder implements Serializable {

    @SuppressWarnings("unchecked")
    public Component build(String id, IModel<IRecord> recordModel) {
        final RecordModel<IRecord> record = new RecordModel<>(recordModel);

        final BSFormRowLayout layout = new BSFormRowLayout(id);

        for (IField<?> field : record.getObject().getEntity().getFields()) {

            switch (field.getType()) {
                case LONG_STRING:
                    layout.newFormGroup()
                        .add(newLongStringField(record, (IField<String>) field));
                    break;
                case STRING:
                    layout.newFormGroup()
                        .add(newStringField(record, (IField<String>) field));
                    break;
                case INTEGER:
                    layout.newFormGroup()
                        .add(newIntegerField(record, (IField<Integer>) field));
                    break;
                case BOOLEAN:
                    layout.newFormGroup()
                        .setMode(BSFormGroup.Mode.CHECK)
                        .add(new CheckBox(field.getName(), record.field((IField<Boolean>) field)));
                    break;
                case CHAR:
                    break;
                case DATE:
                    break;
                case DECIMAL:
                    break;
                case FILE:
                    break;
                case MONEY:
                    break;
                case TIME:
                    break;
                case TIMESTAMP:
                    break;
                default:
                    break;
            }

        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newStringField(final RecordModel<IRecord> record, IField<String> field) {
        return new TextField<>(field.getName(), record.field(field));
    }

    @SuppressWarnings("unchecked")
    private FormComponent<String> newLongStringField(final RecordModel<IRecord> record, IField<String> field) {
        final TextArea<String> textarea = new TextArea<>(field.getName(), record.field(field));
        textarea.add(IBehaviorShortcutsMixin.$b.attrAppend("rows", "5"));
        return textarea;
    }

    @SuppressWarnings("unchecked")
    private FormComponent<Integer> newIntegerField(final RecordModel<IRecord> record, IField<Integer> field) {
        return new TextField<>(field.getName(), record.field(field), Integer.class);
    }

}
