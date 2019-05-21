package org.wystriframework.core.wicket.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.IModel;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.wystriframework.core.util.UncheckedAppendable;
import org.wystriframework.core.wicket.component.TemplatePanel;
import org.wystriframework.core.wicket.component.behavior.RefreshOnAjaxBehavior;

public class BSFormRowLayout extends TemplatePanel implements IBSFormGroupLayout {

    private BSFormLayoutConfig                                         layoutConfig              = new BSFormLayoutConfig(BSSize.sm);
    private boolean                                                    ajaxUpdateEnabled         = true;
    private BSFormControlMarkupResolver                                formControlMarkupResolver = new BSFormControlMarkupResolver();
    private SerializableBiFunction<String, MarkupContainer, Component> feedbackComponentFactory  = (id, fence) -> {
                                                                                                     BSValidationFeedback feedback = new BSValidationFeedback(id, fence);
                                                                                                     if (isAjaxUpdateEnabled())
                                                                                                         feedback.add(new RefreshOnAjaxBehavior());
                                                                                                     return feedback;
                                                                                                 };

    public BSFormRowLayout(String id, IModel<?> model) {
        super(id, BSFormRowLayout::template);
        setDefaultModel(model);
    }

    public BSFormRowLayout(String id) {
        super(id, BSFormRowLayout::template);
    }

    @Override
    public BSFormRowLayout appendFormGroup(SerializableConsumer<BSFormGroup> callback) {
        final BSFormGroup group = newFormGroup();
        callback.accept(group);
        return (BSFormRowLayout) this;
    }

    @Override
    public BSFormGroup newFormGroup() {
        BSFormGroup group = new BSFormGroup(this.newChildId())
            .setFeedbackComponentFactory(getFeedbackComponentFactory());
        add(group);
        return group;
    }

    @Override
    public BSFormLayoutConfig getLayoutConfig() {
        return layoutConfig;
    }

    public BSFormRowLayout appendFormRow(SerializableConsumer<BSFormRow> callback) {
        final BSFormRow row = newFormRow();
        callback.accept(row);
        return (BSFormRowLayout) this;
    }

    public BSFormRow newFormRow() {
        final BSFormRow row = new BSFormRow(this.newChildId())
            .setLayoutConfig(getLayoutConfig())
            .setFeedbackComponentFactory(getFeedbackComponentFactory());
        add(row);
        return row;
    }

    //@formatter:off
    @Override public BSFormRowLayout add         (Behavior... behaviors) { return (BSFormRowLayout) super.add(behaviors); }
    @Override public BSFormRowLayout add         (Component... children) { return (BSFormRowLayout) super.add(children); }
    @Override public BSFormRowLayout addOrReplace(Component... children) { return (BSFormRowLayout) super.addOrReplace(children); }
    @Override public BSFormRowLayout replace     (Component       child) { return (BSFormRowLayout) super.replace(child); }
    
    public boolean isAjaxUpdateEnabled() { return ajaxUpdateEnabled; }
    public BSFormRowLayout setAjaxUpdateEnabled(boolean ajaxUpdateEnabled) { this.ajaxUpdateEnabled = ajaxUpdateEnabled; return this; }
    
    //@formatter:on

    private String newChildId() {
        for (int i = this.size();; i++) {
            final String newId = "_" + i;
            if (this.get(newId) == null)
                return newId;
        }
    }

    private static CharSequence template(TemplatePanel tp) {
        final BSFormRowLayout layout = (BSFormRowLayout) tp;
        final StringBuilder sb = new StringBuilder();
        final UncheckedAppendable ta = new TemplateAppender(sb);

        for (final Component child : layout) {

            if (child instanceof BSFormGroup) {
                appendFormGroupMarkup(ta, layout, (BSFormGroup) child);

            } else if (child instanceof BSFormRow) {
                appendFormRowMarkup(ta, layout, (BSFormRow) child);

            } else {
                layout.formControlMarkupResolver.appendFieldMarkup(ta, layout.getLayoutConfig(), child);
            }
        }
        return sb;
    }

    private static void appendFormGroupMarkup(final UncheckedAppendable ta, final BSFormRowLayout layout, final BSFormGroup group) {
        ta.append("<div wicket:id='").append(group.getId()).append("'>");
        for (final Component groupChild : group.getBodyContainer())
            layout.formControlMarkupResolver.appendFieldMarkup(ta, layout.getLayoutConfig(), groupChild);
        ta.append("</div>");
    }

    private static void appendFormRowMarkup(final UncheckedAppendable ta, final BSFormRowLayout layout, final BSFormRow row) {
        ta.append("<div wicket:id='").append(row.getId()).append("'>");
        for (final Component rowChild : row.getBodyContainer())
            appendFormGroupMarkup(ta, layout, (BSFormGroup) rowChild);
        ta.append("</div>");
    }

    @Override
    public SerializableBiFunction<String, MarkupContainer, Component> getFeedbackComponentFactory() {
        return feedbackComponentFactory;
    }

    private static class TemplateAppender implements UncheckedAppendable {
        private final StringBuilder sb;
        public TemplateAppender(StringBuilder sb) {
            this.sb = sb;
        }

        //@formatter:off
        @Override public TemplateAppender append(char         s) { sb.append(s); return this; }
        @Override public TemplateAppender append(CharSequence s) { sb.append(s); return this; }
        @Override public TemplateAppender append(CharSequence s, int start, int end) { sb.append(s, start, end); return this; }
        //@formatter:on
    }
}
