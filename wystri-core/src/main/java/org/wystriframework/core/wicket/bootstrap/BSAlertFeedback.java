package org.wystriframework.core.wicket.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FencedFeedbackPanel;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.ComponentTag;
import org.wystriframework.core.wicket.util.WicketComponentUtils;

public class BSAlertFeedback extends FencedFeedbackPanel {

    public BSAlertFeedback(String id, Component fence, IFeedbackMessageFilter filter) {
        super(id, fence, filter);
    }

    public BSAlertFeedback(String id, Component fence) {
        super(id, fence);
    }

    public BSAlertFeedback(String id, IFeedbackMessageFilter filter) {
        super(id, filter);
    }

    public BSAlertFeedback(String id) {
        super(id);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        WicketComponentUtils.appendDistinctValueToAttribute(tag, "class", ' ', newValue());
    }

    protected String[] newValue() {
        if (anyMessage(FeedbackMessage.ERROR) || anyMessage(FeedbackMessage.FATAL))
            return new String[] { "alert", "alert-danger" };

        if (anyMessage(FeedbackMessage.WARNING))
            return new String[] { "alert", "alert-warning" };

        if (anyMessage(FeedbackMessage.INFO))
            return new String[] { "alert", "alert-info" };

        if (anyMessage(FeedbackMessage.SUCCESS))
            return new String[] { "alert", "alert-success" };

        if (anyMessage(FeedbackMessage.UNDEFINED) || anyMessage(FeedbackMessage.DEBUG))
            return new String[] { "alert", "alert-secondary" };

        return new String[0];
    }
}
