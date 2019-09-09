package org.wystriframework.examples.ui;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.wystriframework.core.wicket.WystriConfiguration;
import org.wystriframework.examples.ui.view.home.HomePage;
import org.wystriframework.ui.component.fileupload.CustomDiskFileItemFactory;

@org.springframework.stereotype.Component
public class WystriExamplesApplication extends WebApplication {

    @Override
    public void init() {
        super.init();

        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getResourceSettings().setThrowExceptionOnMissingResource(false);
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);

        WystriConfiguration.get().setBeanLookup(new SpringBeanLookup(this));

        mountPage("home", HomePage.class);
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
