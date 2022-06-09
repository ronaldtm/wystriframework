package org.wystriframework.examples.ui;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.resource.JQueryResourceReference;

public class Initializer implements IInitializer {

    @Override
    public void init(Application application) {
        application.getMarkupSettings()
            .setDefaultMarkupEncoding("utf-8")
            .setStripComments(true)
            .setStripWicketTags(true)
            .setCompressWhitespace(true);

        application.getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);

        application.getJavaScriptLibrarySettings().setJQueryReference(JQueryResourceReference.getV3());

        application.getComponentPreOnBeforeRenderListeners().add(c -> c
            .setOutputMarkupId(!c.getRenderBodyOnly())
            .setOutputMarkupPlaceholderTag(!c.getRenderBodyOnly()));

        if (RuntimeConfigurationType.DEVELOPMENT == application.getConfigurationType()) {
            application.getMarkupSettings()
                .setCompressWhitespace(false)
                .setStripComments(false);
            application.getDebugSettings()
                .setComponentPathAttributeName("data-wicket-path")
                .setDevelopmentUtilitiesEnabled(true)
                .setLinePreciseReportingOnAddComponentEnabled(true)
                .setLinePreciseReportingOnNewComponentEnabled(true)
                .setOutputMarkupContainerClassName(true);
        }
    }

    @Override
    public void destroy(Application application) {}

}
