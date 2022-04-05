package org.wystriframework.form.formbuilder;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.string.StringValue;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.ui.bootstrap.BSFormGroup;
import org.wystriframework.ui.bootstrap.IBSFormGroupLayout;

@SuppressWarnings("serial")
public class FieldComponentContext<E, F> implements Serializable {

    private static final StringValue                   EMPTY_PARAM = StringValue.valueOf("");

    private final IBSFormGroupLayout                   layout;
    private final RecordModel<? extends IRecord<E>, E> record;
    private final IField<E, F>                         field;
    private final Map<String, StringValue>             params;
    private final Consumer<BSFormGroup>                groupConfigurer;
    private BSFormGroup                                formGroup;
    private FormComponent<F>                           fieldComponent;

    public FieldComponentContext(IBSFormGroupLayout layout, RecordModel<? extends IRecord<E>, E> record, IField<E, F> field, Map<String, StringValue> params, SerializableConsumer<BSFormGroup> groupConfigurer) {
        this.layout = layout;
        this.record = record;
        this.field = field;
        this.params = params;
        this.groupConfigurer = groupConfigurer;
    }

    public StringValue getParam(String paramName) {
        return params.getOrDefault(paramName, EMPTY_PARAM);
    }

    public IBSFormGroupLayout getLayout() {
        return layout;
    }
    public RecordModel<? extends IRecord<E>, E> getRecord() {
        return record;
    }
    public IField<E, F> getField() {
        return field;
    }
    public Consumer<BSFormGroup> getGroupConfigurer() {
        return groupConfigurer;
    }
    public BSFormGroup getFormGroup() {
        return formGroup;
    }
    public FieldComponentContext<E, F> setFormGroup(BSFormGroup formGroup) {
        this.formGroup = formGroup;
        if (getGroupConfigurer() != null)
            getGroupConfigurer().accept(formGroup);
        return this;
    }
    public FormComponent<F> getFieldComponent() {
        return fieldComponent;
    }
    public FieldComponentContext<E, F> setFieldComponent(FormComponent<F> fieldComponent) {
        this.fieldComponent = fieldComponent;
        return this;
    }
}