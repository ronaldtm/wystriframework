package org.wystriframework.core.wicket.bootstrap;

import java.util.stream.Stream;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.border.Border;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.wystriframework.core.wicket.util.WicketComponentUtils;

import com.google.common.base.Preconditions;

public class BSFormRow extends Border {

    private SerializableBiFunction<String, MarkupContainer, Component> feedbackComponentFactory = (id, fence) -> new BSValidationFeedback(id, fence);

    public BSFormRow(String id) {
        super(id);
    }

    public BSFormGroup newFormGroup() {
        BSFormGroup group = new BSFormGroup(newChildId())
            .setFeedbackComponentFactory((id, fence) -> getFeedbackComponentFactory().apply(id, fence));
        add(group);
        return group;
    }

    public BSFormRow appendFormGroup(SerializableConsumer<BSFormGroup> callback) {
        final BSFormGroup group = newFormGroup();
        callback.accept(group);
        return (BSFormRow) this;
    }

    @Override
    public Border add(Component... children) {
        Stream.of(children).forEach(c -> Preconditions.checkArgument(c instanceof BSFormGroup));
        return super.add(children);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        WicketComponentUtils.appendCssClasses(tag, "form-row");
    }

    private String newChildId() {
        for (int i = getBodyContainer().size();; i++) {
            final String newId = "_" + i;
            if (this.get(newId) == null)
                return newId;
        }
    }
    //@formatter:off
    public SerializableBiFunction<String, MarkupContainer, Component> getFeedbackComponentFactory() { return feedbackComponentFactory; }
    public BSFormRow setFeedbackComponentFactory(SerializableBiFunction<String, MarkupContainer, Component> feedbackComponentFactory) { this.feedbackComponentFactory = feedbackComponentFactory; return this; }
    //@formatter:on
}
