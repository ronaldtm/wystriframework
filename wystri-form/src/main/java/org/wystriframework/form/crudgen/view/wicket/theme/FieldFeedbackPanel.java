package org.wystriframework.form.crudgen.view.wicket.theme;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FencedFeedbackPanel;
import org.apache.wicket.markup.ComponentTag;

@SuppressWarnings("serial")
public class FieldFeedbackPanel extends FencedFeedbackPanel {
    public FieldFeedbackPanel(String id, Component fence) {
        super(id, fence);
    }
    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        AttributeAppender.append("class", getCssClass());
    }
    protected String getCssClass() {
        if (anyMessage(FeedbackMessage.ERROR) || anyMessage(FeedbackMessage.FATAL))
            return "alert alert-danger";

        if (anyMessage(FeedbackMessage.WARNING))
            return "alert alert-warning";

        if (anyMessage(FeedbackMessage.INFO))
            return "alert alert-info";

        if (anyMessage(FeedbackMessage.SUCCESS))
            return "alert alert-success";

        if (anyMessage(FeedbackMessage.UNDEFINED) || anyMessage(FeedbackMessage.DEBUG))
            return "alert alert-secondary";

        return "";
    }
}