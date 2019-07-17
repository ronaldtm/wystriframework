package org.wystriframework.ui.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FencedFeedbackPanel;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.wystriframework.ui.util.WicketComponentUtils;

public class BSValidationFeedback extends FencedFeedbackPanel {

    public BSValidationFeedback(String id, Component fence, IFeedbackMessageFilter filter) {
        super(id, fence, filter);
    }

    public BSValidationFeedback(String id, Component fence) {
        super(id, fence);
    }

    public BSValidationFeedback(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    public BSValidationFeedback(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setRenderBodyOnly(true);
    }

    @Override
    protected Component newMessageDisplayComponent(String id, FeedbackMessage message) {
        return super.newMessageDisplayComponent(id, message)
            .add(new Behavior() {
                @Override
                public void onComponentTag(Component component, ComponentTag tag) {
                    super.onComponentTag(component, tag);
                    WicketComponentUtils.appendCssClasses(tag, getItemCSSClass(message));
                }
            });
    }

    protected String getItemCSSClass(FeedbackMessage message) {
        switch (message.getLevel()) {

            case FeedbackMessage.ERROR:
            case FeedbackMessage.FATAL:
                return "invalid-feedback";

            case FeedbackMessage.SUCCESS:
                return "valid-feedback";

            case FeedbackMessage.INFO:
                return "form-text text-info small";

            case FeedbackMessage.WARNING:
                return "form-text text-warning small";

            default:
                return "form-text text-muted small";
        }
    }

    @Override
    protected String getCSSClass(FeedbackMessage message) {
        return "";
    }
}
