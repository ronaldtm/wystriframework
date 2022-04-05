package org.wystriframework.form.crudgen.view.wicket.theme;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.definition.IField;

@SuppressWarnings("serial")
public class InputFieldBorder<E, F, IF extends IField<E, F>> extends Border {

    public InputFieldBorder(String id, IModel<?> model) {
        super(id, model);
    }
}
