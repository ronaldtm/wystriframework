package org.wystriframework.core.formbuilder;

import java.io.Serializable;
import java.util.function.Consumer;

import org.apache.wicket.markup.html.form.FormComponent;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.bootstrap.BSFormGroup;
import org.wystriframework.core.wicket.bootstrap.IBSFormGroupLayout;

public class FieldComponentContext<T> implements Serializable {
    private final IBSFormGroupLayout             layout;
    private final RecordModel<? extends IRecord> record;
    private final IField<T>                      field;
    private final Consumer<BSFormGroup>          groupConfigurer;
    private BSFormGroup                          formGroup;
    private FormComponent<T>                     fieldComponent;

    public FieldComponentContext(IBSFormGroupLayout layout, RecordModel<? extends IRecord> record, IField<T> field, Consumer<BSFormGroup> groupConfigurer) {
        this.layout = layout;
        this.record = record;
        this.field = field;
        this.groupConfigurer = groupConfigurer;
    }
    public IBSFormGroupLayout getLayout() {
        return layout;
    }
    public RecordModel<? extends IRecord> getRecord() {
        return record;
    }
    public IField<T> getField() {
        return field;
    }
    public Consumer<BSFormGroup> getGroupConfigurer() {
        return groupConfigurer;
    }
    public BSFormGroup getFormGroup() {
        return formGroup;
    }
    public FieldComponentContext<T> setFormGroup(BSFormGroup formGroup) {
        this.formGroup = formGroup;
        if (getGroupConfigurer() != null)
            getGroupConfigurer().accept(formGroup);
        return this;
    }
    public FormComponent<T> getFieldComponent() {
        return fieldComponent;
    }
    public FieldComponentContext<T> setFieldComponent(FormComponent<T> fieldComponent) {
        this.fieldComponent = fieldComponent;
        return this;
    }
}