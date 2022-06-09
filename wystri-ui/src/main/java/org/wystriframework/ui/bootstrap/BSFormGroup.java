package org.wystriframework.ui.bootstrap;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.ObjectUtils.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupException;
import org.apache.wicket.markup.MarkupFragment;
import org.apache.wicket.markup.TagUtils;
import org.apache.wicket.markup.WicketTag;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ILabelProvider;
import org.apache.wicket.markup.html.panel.BorderMarkupSourcingStrategy;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.danekja.java.util.function.serializable.SerializableBiFunction;
import org.wystriframework.ui.util.WicketComponentUtils;

import com.google.common.collect.Lists;

public class BSFormGroup extends Border implements ILabelProvider<String> {

    public enum Mode {
        //@formatter:off
        DEFAULT("labelFirst", null),
        CHECK  ("bodyFirst" , "form-check");
        //@formatter:on

        final String templateId;
        final String containerCssClass;
        private Mode(String templateId, String containerCssClass) {
            this.templateId = templateId;
            this.containerCssClass = containerCssClass;
        }
        boolean shouldRenderContainer() {
            return containerCssClass != null;
        }
    }

    private static final IVisitor<Component, Serializable>             LABEL_RESOLVER_VISITOR    = new LabelResolverVisitor();
    private static final IVisitor<FormComponent<?>, Boolean>           REQUIRED_RESOLVER_VISITOR = new RequiredResolverVisitor();

    private SerializableBiFunction<String, MarkupContainer, Component> feedbackComponentFactory  = BSValidationFeedback::new;
    private IModel<String>                                             labelModel;
    private Mode                                                       mode                      = Mode.DEFAULT;

    private final WebMarkupContainer                                   container;
    private final Label                                                label;

    private BSColSize[]                                                groupColSizes;
    private BSColSize[]                                                labelColSizes;

    public BSFormGroup(String id) {
        super(id);

        container = newContainer("container");
        label = newLabel("label");

        addToBorder(container
            .add(label)
            .add(feedbackComponentFactory.apply("feedback", getBodyContainer())));
    }

    @Override
    public IMarkupFragment getMarkup(Component child) {

        // Border require an associated markup resource file
        IMarkupFragment markup = getAssociatedMarkup();
        if (markup == null)
            throw new MarkupException("Unable to find associated markup file for Border: " + this);

        // Find <wicket:border>
        IMarkupFragment borderMarkup = null;
        for (int i = 0; i < markup.size(); i++) {
            if (isWicketBorderTag(markup.get(i))) {
                borderMarkup = new MarkupFragment(markup, i);
                break;
            }
        }

        if (borderMarkup == null)
            throw new MarkupException(markup.getMarkupResourceStream(), "Unable to find <wicket:border> tag in associated markup file for Border: " + this);

        // If child == null, return the markup fragment starting with the <wicket:border> tag
        if (child == null)
            return borderMarkup;

        // Is child == BorderBody?
        final BorderBodyContainer body = getBodyContainer();
        if (child == body)
            return body.getMarkup(); // Get the <wicket:body> markup

        // Find the markup for the child component
        IMarkupFragment childMarkup = borderMarkup.find(child.getId());
        if (childMarkup != null)
            return childMarkup;

        return ((BorderMarkupSourcingStrategy) getMarkupSourcingStrategy()).findMarkupInAssociatedFileHeader(this, child);
    }

    protected Serializable resolveLabel() {
        if (getLabel() != null)
            return getLabel().getObject();
        else {
            return defaultIfNull(getBodyContainer().visitChildren(ILabelProvider.class, LABEL_RESOLVER_VISITOR), "");
        }
    }

    protected boolean resolveRequired() {
        return Boolean.TRUE.equals(getBodyContainer().visitChildren(FormComponent.class, REQUIRED_RESOLVER_VISITOR));
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);

        final List<String> classes = new ArrayList<>();
        classes.add("form-group");
        classes.add((resolveRequired()) ? "required" : "optional");
        if (getGroupColSizes() != null)
            classes.addAll(Stream.of(getGroupColSizes()).map(it -> it.cssClass()).collect(toList()));

        WicketComponentUtils.appendCssClasses(tag, classes);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(new CssResourceReference(BSFormGroup.class, BSFormGroup.class.getSimpleName() + ".css")));
    }

    private WebMarkupContainer newContainer(String id) {
        return new FormGroupContainer(id);
    }

    private Label newLabel(String id) {
        return new FormGroupLabel(id, this::resolveLabel);
    }

    private boolean isWicketBorderTag(MarkupElement elem) {
        return TagUtils.isWicketBorderTag(elem) && mode.templateId.equals(((WicketTag) elem).getId());
    }

    public SerializableBiFunction<String, MarkupContainer, Component> getFeedbackComponentFactory() {
        return feedbackComponentFactory;
    }
    public BSFormGroup setFeedbackComponentFactory(SerializableBiFunction<String, MarkupContainer, Component> feedbackComponentFactory) {
        this.feedbackComponentFactory = feedbackComponentFactory;
        return this;
    }
    //@formatter:off
    @Override
    public IModel<String> getLabel()         { return labelModel   ; }
    public Mode           getMode()          { return mode         ; }
    public BSColSize[]    getGroupColSizes() { return groupColSizes; }
    public BSColSize[]    getLabelColSizes() { return labelColSizes; }
    public BSFormGroup setMode         (Mode          mode) { this.mode          = mode ; return this; }
    public BSFormGroup setGroupColSizes(BSColSize... sizes) { this.groupColSizes = sizes; return this; }
    public BSFormGroup setLabelColSizes(BSColSize... sizes) { this.labelColSizes = sizes; return this; }
    //@formatter:on

    private static class LabelResolverVisitor implements IVisitor<Component, Serializable> {
        @Override
        public void component(Component comp, IVisit<Serializable> visit) {
            @SuppressWarnings("unchecked")
            IModel<Serializable> label = ((ILabelProvider<Serializable>) comp).getLabel();
            if ((label != null) && (label.getObject() != null))
                visit.stop(label.getObject());
        }
    }

    private static class RequiredResolverVisitor implements IVisitor<FormComponent<?>, Boolean> {
        @Override
        public void component(FormComponent<?> comp, IVisit<Boolean> visit) {
            visit.dontGoDeeper();
            if (comp.isRequired())
                visit.stop(true);
        }
    }

    protected class FormGroupContainer extends WebMarkupContainer {
        public FormGroupContainer(String id) {
            super(id);
        }
        @Override
        protected void onConfigure() {
            super.onConfigure();
            this.setRenderBodyOnly(!mode.shouldRenderContainer());
        }
        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            WicketComponentUtils.appendCssClasses(tag, mode.containerCssClass);
            if (resolveRequired())
                WicketComponentUtils.appendCssClasses(tag, "required");
        }
    }

    protected class FormGroupLabel extends Label {
        public FormGroupLabel(String id, IModel<?> model) {
            super(id, model);
        }
        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            if (mode == Mode.CHECK)
                WicketComponentUtils.appendCssClasses(tag, "form-check-label");

            if (getLabelColSizes() != null)
                WicketComponentUtils.appendCssClasses(tag, Lists.transform(asList(getLabelColSizes()), it -> it.cssClass()));
        }
    }
}
