package org.wystriframework.examples.ui.view;

import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@SuppressWarnings("serial")
public class BasePage extends WebPage implements IAjaxIndicatorAware {
    private static final long        serialVersionUID = 1L;

    private final WebMarkupContainer backdrop         = new WebMarkupContainer("backdrop");

    public BasePage(final PageParameters parameters) {
        super(parameters);
    }
    public BasePage(final IModel<?> model) {
        super(model);
    }

	@Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Link<Void>("home") {
            @Override
            public void onClick() {
                setResponsePage(getApplication().getHomePage());
            }
        });

        add(backdrop.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
    }

    @Override
    public String getAjaxIndicatorMarkupId() {
        return backdrop.getMarkupId();
    }
}
