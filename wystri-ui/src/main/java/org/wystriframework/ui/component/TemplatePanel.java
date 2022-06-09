package org.wystriframework.ui.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.IMarkupCacheKeyProvider;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.IMarkupSourcingStrategy;
import org.apache.wicket.markup.html.panel.PanelMarkupSourcingStrategy;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.danekja.java.util.function.serializable.SerializableFunction;

public class TemplatePanel extends WebMarkupContainer
    implements IMarkupResourceStreamProvider, IMarkupCacheKeyProvider {

    private final SerializableFunction<TemplatePanel, CharSequence> markupSupplier;

    public TemplatePanel(String id, SerializableFunction<TemplatePanel, CharSequence> markupSupplier) {
        super(id);
        this.markupSupplier = markupSupplier;
    }

    @Override
    protected IMarkupSourcingStrategy newMarkupSourcingStrategy() {
        return new PanelMarkupSourcingStrategy(false);
    }

    @Override
    public String getCacheKey(MarkupContainer container, Class<?> containerClass) {
        return null;
    }

    @Override
    public IResourceStream getMarkupResourceStream(MarkupContainer container, Class<?> containerClass) {
        return new StringResourceStream(new StringBuilder().append("<wicket:panel>").append(markupSupplier.apply(this)).append("</wicket:panel>"));
    }
}
