package org.wystriframework.form.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import java.util.List;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IOptionsProvider;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.Wystri;
import org.wystriframework.form.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.form.formbuilder.FieldComponentContext;
import org.wystriframework.form.formbuilder.FormComponentFieldView;
import org.wystriframework.form.formbuilder.RecordModel;

public class DropDownFieldAppender<T> extends AbstractFieldComponentAppender<T> {

    @Override
    @SuppressWarnings("unchecked")
    protected <E> FormComponent<T> newFormComponent(FieldComponentContext<E, T> ctx) {

        final IField<E, T> field = (IField<E, T>) ctx.getField();
        final RecordModel<? extends IRecord<E>, E> recordModel = ctx.getRecord();

        IOptionsProvider<E, T> optionsProvider = field.getOptionsProvider().orElse(null);

        final IChoiceRenderer<T> choiceRenderer = new IChoiceRenderer<T>() {
            @Override
            public Object getDisplayValue(T object) {
                List<? extends T> options = optionsProvider.getOptions(recordModel.getObject());
                return optionsProvider.objectToDisplay(object, options);
            }
            @Override
            public String getIdValue(T object, int index) {
                return optionsProvider.objectToId(object);
            }
            @Override
            public T getObject(String id, IModel<? extends List<? extends T>> choices) {
                return optionsProvider.idToObject(id, choices.getObject());
            }
        };

        return new DropDownChoice<>(field.getName(),
            recordModel.field(field),
            () -> optionsProvider.getOptions(recordModel.getObject()),
            choiceRenderer) {
            @Override
            protected void reportRequiredError() {
                final String msg = field.requiredError(recordModel.getObject());
                if (isNotBlank(msg)) {
                    super.error(Wystri.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }

    @Override
    protected <E> IFieldView<E, T> newFieldView(FieldComponentContext<E, T> ctx) {
        return new FormComponentFieldView<>(ctx) {
            @Override
            public void setRequired(boolean required) {
                super.setRequired(required);
                if (ctx.getFieldComponent() instanceof AbstractSingleSelectChoice<?>) {
                    ((AbstractSingleSelectChoice<?>) ctx.getFieldComponent()).setNullValid(!required);
                }
            }
        };
    }
}
