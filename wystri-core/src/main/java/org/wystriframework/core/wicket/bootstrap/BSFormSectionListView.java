package org.wystriframework.core.wicket.bootstrap;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.wicket.component.TemplatePanel;

public class BSFormSectionListView extends Panel {

    private final RepeatingView sections = new RepeatingView("sections");

    public BSFormSectionListView(String id) {
        super(id);
        super.add(sections);
    }

    public BSFormSectionListView appendSection(IModel<String> title, Component content) {
        sections.add(new TemplatePanel(sections.newChildId(), BSFormSectionListView::template)
            .add(new Label("title", title))
            .add(content));
        return this;
    }

    @Override
    public MarkupContainer add(Component... children) {
        throw new UnsupportedOperationException();
    }
    @Override
    public MarkupContainer replace(Component child) {
        throw new UnsupportedOperationException();
    }

    private static String template(TemplatePanel tp) {
        final String contentId = tp.streamChildren()
            .filter(it -> !"title".equals(it.getId()))
            .map(it -> it.getId())
            .findFirst()
            .get();
        return ""
            + "<legend wicket:id='title'></legend>"
            + "<div wicket:id='" + contentId + "'></div>";
    }
}
