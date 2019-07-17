package org.wystriframework.form.crudgen.view.wicket.theme;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;

public interface CrudgenTheme {

    public static CrudgenTheme get() {
        return new CrudgenTheme() {};
    }

    default <T> Component fieldFeedback(String id, MarkupContainer fieldContainer, Component fieldComponent) {
        return new FieldFeedbackPanel(id, fieldContainer);
    }
}
