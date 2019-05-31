package org.wystriframework.examples.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.response.filter.AjaxServerAndClientTimeFilter;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.springframework.stereotype.Component;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.core.wicket.component.fileupload.CustomDiskFileItemFactory;
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

        WystriConfiguration.get().setBeanLookup(new SpringBeanLookup(this));

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

    @Override
    public WebRequest newWebRequest(HttpServletRequest servletRequest, String filterPath) {
        return new ServletWebRequest(servletRequest, filterPath) {
            @Override
            public MultipartServletWebRequest newMultipartWebRequest(Bytes maxSize, String upload) throws FileUploadException {
                return newMultipartWebRequest(maxSize, upload, new CustomDiskFileItemFactory());
            }
        };
    }
}
