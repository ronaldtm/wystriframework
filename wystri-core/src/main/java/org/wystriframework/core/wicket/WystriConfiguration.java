package org.wystriframework.core.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.ISessionListener;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;

public class WystriConfiguration implements IHeaderContributor {

    //    private static final MetaDataKey<Map<String, List>>
    private static final WystriConfiguration INSTANCE        = new WystriConfiguration();

    private String                           bootstrapCssUrl = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css";
    private String                           bootstrapJsUrl  = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";

    public WystriConfiguration() {}

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forUrl(getBootstrapCssUrl()));
        response.render(JavaScriptHeaderItem.forUrl(getBootstrapJsUrl()));
    }

    public static WystriConfiguration get() {
        return INSTANCE;
    }

    //@formatter:off
    public String getBootstrapCssUrl() { return bootstrapCssUrl; }
    public String getBootstrapJsUrl()  { return bootstrapJsUrl ; }
    public WystriConfiguration setBootstrapCssUrl(String bootstrapCssUrl) { this.bootstrapCssUrl = bootstrapCssUrl; return this; }
    public WystriConfiguration setBootstrapJsUrl(String   bootstrapJsUrl) { this.bootstrapJsUrl  = bootstrapJsUrl ; return this; }
    //@formatter:on

    public static class Initializer implements IInitializer, ISessionListener {
        @Override
        public void init(Application application) {
            application.getSessionListeners().add(this);
        }
        @Override
        public void destroy(Application application) {}

        @Override
        public void onCreated(Session session) {
            ISessionListener.super.onCreated(session);
        }
        @Override
        public void onUnbound(String sessionId) {
            //application.getMetaData(key)
        }
    }
}
