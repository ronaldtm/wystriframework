package org.wystriframework.core.formbuilder.appenders;

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
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.formbuilder.FormComponentFieldView;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;

public class DropDownFieldAppender<T> extends AbstractFieldComponentAppender<T> {

    @Override
    @SuppressWarnings("unchecked")
    protected FormComponent<T> newFormComponent(FieldComponentContext<T> ctx) {

        final IField<T> field = (IField<T>) ctx.getField();
        final RecordModel<? extends IRecord> recordModel = ctx.getRecord();

        IOptionsProvider<T> optionsProvider = field.getOptionsProvider().orElse(null);

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
                    super.error(WystriConfiguration.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }

    @Override
    protected IFieldView<T> newFieldView(FieldComponentContext<T> ctx) {
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
