package org.wystriframework.core.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.core.wicket.WystriConfiguration;

public class IntegerFieldAppender extends AbstractFieldComponentAppender<Integer> {

    @Override
    protected FormComponent<Integer> newFormComponent(FieldComponentContext<Integer> ctx) {
        final IField<Integer> ifield = (IField<Integer>) ctx.getField();
        return new TextField<Integer>(ifield.getName(), ctx.getRecord().field(ifield), Integer.class) {
            @Override
            protected void reportRequiredError() {
                final String msg = ctx.getRequiredErrorMessageSupplier().get();
                if (isNotBlank(msg)) {
                    super.error(WystriConfiguration.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }
}
