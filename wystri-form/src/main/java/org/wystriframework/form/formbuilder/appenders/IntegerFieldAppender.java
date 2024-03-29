package org.wystriframework.form.formbuilder.appenders;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.wystriframework.core.definition.IField;
import org.wystriframework.core.wicket.Wystri;
import org.wystriframework.form.formbuilder.AbstractFieldComponentAppender;
import org.wystriframework.form.formbuilder.FieldComponentContext;

public class IntegerFieldAppender extends AbstractFieldComponentAppender<Integer> {

    @Override
    protected <E> FormComponent<Integer> newFormComponent(FieldComponentContext<E, Integer> ctx) {
        final IField<E, Integer> field = ctx.getField();
        return new TextField<Integer>(field.getName(), ctx.getRecord().field(field), Integer.class) {
            @Override
            protected void reportRequiredError() {
                final String msg = field.requiredError(ctx.getRecord().getObject());
                if (isNotBlank(msg)) {
                    super.error(Wystri.get().localizedString(msg));
                } else {
                    super.reportRequiredError();
                }
            }
        };
    }
}
