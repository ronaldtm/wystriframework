package org.wystriframework.ui.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.ComponentTag;
import org.wystriframework.ui.util.WicketComponentUtils;

public class BSValidationStatusBehavior extends Behavior {

    private static final BSValidationStatusBehavior INSTANCE = new BSValidationStatusBehavior();

    public static BSValidationStatusBehavior getInstance() {
        return INSTANCE;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        if (component.hasErrorMessage())
            WicketComponentUtils.appendCssClasses(tag, "is-invalid");

        else if (component.getFeedbackMessages().hasMessage(FeedbackMessage.SUCCESS))
            WicketComponentUtils.appendCssClasses(tag, "is-valid");
    }
}