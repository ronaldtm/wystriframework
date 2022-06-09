package org.wystriframework.core.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.protocol.http.WebApplication;

public class WystriInitializer implements IInitializer {

    // IInitializer
    @Override
    public void init(Application application) {
        final WebApplication webapp = (WebApplication) application;
        Wystri.init(webapp);
    }

    @Override
    public void destroy(Application application) {}
}
