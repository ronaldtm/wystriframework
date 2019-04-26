package org.wystriframework.examples.ui;

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;
import org.wystriframework.examples.ui.view.crudgen.CrudgenDemoPage;
import org.wystriframework.examples.ui.view.home.HomePage;

@Component
public class WystriExamplesApplication extends WebApplication {

    @Override
    public void init() {
        super.init();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
        getRequestCycleSettings().addResponseFilter(new AjaxServerAndClientTimeFilter());

        mountPage("home", HomePage.class);

        mountPage("crudgen", CrudgenDemoPage.class);
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    public static WystriExamplesApplication get() {
        return (WystriExamplesApplication) Application.get();
    }
}
