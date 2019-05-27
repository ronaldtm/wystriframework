package org.wystriframework.core.wicket;

import org.apache.wicket.Application;
import org.apache.wicket.IInitializer;
import org.apache.wicket.ISessionListener;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.model.IModel;
import org.wystriframework.core.filemanager.ITempFileManager;
import org.wystriframework.core.filemanager.SessionScopedTempFileManager;
import org.wystriframework.core.util.IBeanLookup;
import org.wystriframework.core.util.NewInstanceBeanLookup;

public class WystriConfiguration {

    //    private static final MetaDataKey<Map<String, List>>
    private static final WystriConfiguration INSTANCE        = new WystriConfiguration();

    private String                           bootstrapCssUrl = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css";
    private String                           bootstrapJsUrl  = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";

    private IBeanLookup                      beanLookup      = new NewInstanceBeanLookup();

    public WystriConfiguration() {}

    public IHeaderContributor getHeaderContributor() {
        return response -> {
            response.render(CssHeaderItem.forUrl(getBootstrapCssUrl()));
            response.render(JavaScriptHeaderItem.forUrl(getBootstrapJsUrl()));
        };
    }

    public ITempFileManager getTempFileManager() {
        return SessionScopedTempFileManager.get(Session.get());
    }

    public String localizedString(String key) {
        return Application.get().getResourceSettings().getLocalizer().getString(key, null, key);
    }
    public String localizedString(String key, IModel<?> model) {
        return Application.get().getResourceSettings().getLocalizer().getString(key, null, model, key);
    }

    public static WystriConfiguration get() {
        return INSTANCE;
    }

    //@formatter:off
    public String       getBootstrapCssUrl() { return bootstrapCssUrl; }
    public String       getBootstrapJsUrl()  { return bootstrapJsUrl ; }
    public IBeanLookup  getBeanLookup()      { return beanLookup     ; }
    public WystriConfiguration setBootstrapCssUrl(String bootstrapCssUrl) { this.bootstrapCssUrl = bootstrapCssUrl; return this; }
    public WystriConfiguration setBootstrapJsUrl (String  bootstrapJsUrl) { this.bootstrapJsUrl  = bootstrapJsUrl ; return this; }
    public WystriConfiguration setBeanLookup     (IBeanLookup beanLookup) { this.beanLookup      = beanLookup     ; return this; }
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
