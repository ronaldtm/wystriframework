package org.wystriframework.core.formbuilder.appenders;

import static java.util.Arrays.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.formbuilder.FieldComponentContext;
import org.wystriframework.core.formbuilder.FormComponentFieldView;
import org.wystriframework.core.formbuilder.RecordModel;
import org.wystriframework.core.wicket.WystriConfiguration;

public class BooleanFieldAppender extends AbstractFieldComponentAppender<Boolean> {

    private static final String TRUE_VALUE  = "Y";
    private static final String FALSE_VALUE = "N";

    private String              trueDisplay;
    private String              falseDisplay;
    private String              nullDisplay = "";

    public BooleanFieldAppender() {
        this(TRUE_VALUE, FALSE_VALUE);
    }
    public BooleanFieldAppender(String trueDisplay, String falseDisplay) {
        this.trueDisplay = trueDisplay;
        this.falseDisplay = falseDisplay;
    }
    public BooleanFieldAppender(String trueDisplay, String falseDisplay, String nullDisplay) {
        this.trueDisplay = trueDisplay;
        this.falseDisplay = falseDisplay;
        this.nullDisplay = nullDisplay;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected FormComponent<Boolean> newFormComponent(FieldComponentContext<Boolean> ctx) {

        final IField<Boolean> ifield = (IField<Boolean>) ctx.getField();
        final RecordModel<? extends IRecord> record = ctx.getRecord();

        final IChoiceRenderer<Boolean> choiceRenderer = new IChoiceRenderer<Boolean>() {
            @Override
            public Object getDisplayValue(Boolean object) {
                return (Boolean.TRUE.equals(object)) ? trueDisplay
                    : (Boolean.FALSE.equals(object)) ? falseDisplay
                        : nullDisplay;
            }
            @Override
            public String getIdValue(Boolean object, int index) {
                return (Boolean.TRUE.equals(object)) ? TRUE_VALUE
                    : (Boolean.FALSE.equals(object)) ? FALSE_VALUE
                        : null;
            }
            @Override
            public Boolean getObject(String id, IModel<? extends List<? extends Boolean>> choices) {
                return TRUE_VALUE.equals(id) ? Boolean.TRUE
                    : FALSE_VALUE.equals(id) ? Boolean.FALSE
                        : null;
            }
        };

        return new DropDownChoice<>(ifield.getName(), record.field(ifield), new ArrayList<>(asList(true, false)), choiceRenderer) {
            @Override
            protected void reportRequiredError() {
                final String msg = ifield.requiredError(record.getObject());
                if (isNotBlank(msg)) {
                    super.error(WystriConfiguration.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
            @Override
            protected String getNullValidDisplayValue() {
                return nullDisplay;
            }
        };
    }

    @Override
    protected IFieldView<Boolean> newFieldView(FieldComponentContext<Boolean> ctx) {
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
