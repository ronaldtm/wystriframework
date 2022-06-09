package org.wystriframework.core.wicket;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.request.resource.ResourceReference;
import org.danekja.java.util.function.serializable.SerializableSupplier;
import org.wystriframework.core.util.IBeanLookup;
import org.wystriframework.core.util.NewInstanceBeanLookup;

public class WystriConfiguration {

    private static final String          DEFAULT_BOOTSTRAP_CSS_URL = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css";
    private static final String          DEFAULT_BOOTSTRAP_JS_URL  = "https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js";
    private SerializableSupplier<String> bootstrapCssUrl           = () -> DEFAULT_BOOTSTRAP_CSS_URL;
    private SerializableSupplier<String> bootstrapJsUrl            = () -> DEFAULT_BOOTSTRAP_JS_URL;

    private IBeanLookup                  beanLookup                = new NewInstanceBeanLookup();

    public WystriConfiguration() {}

    public IWystriResourceProvider getBootstrapResources() {
        return new IWystriResourceProvider() {
            @Override
            public ResourceReference[] getCSS() {
                return null;
            }
            @Override
            public ResourceReference[] getJS() {
                return null;
            }
        };
    }

    public IHeaderContributor getHeaderContributor() {
        return response -> {
            response.render(CssHeaderItem.forUrl(getBootstrapCssUrl()));
            response.render(JavaScriptHeaderItem.forUrl(getBootstrapJsUrl()));
        };
    }

    public static WystriConfiguration get() {
        return Wystri.get().getConfiguration();
    }

    //@formatter:off
    public String       getBootstrapCssUrl() { return bootstrapCssUrl.get(); }
    public String       getBootstrapJsUrl()  { return bootstrapJsUrl.get() ; }
    public IBeanLookup  getBeanLookup()      { return beanLookup     ; }
    public WystriConfiguration setBootstrapCssUrl(SerializableSupplier<String> bootstrapCssUrl) { this.bootstrapCssUrl = bootstrapCssUrl; return this; }
    public WystriConfiguration setBootstrapJsUrl (SerializableSupplier<String > bootstrapJsUrl) { this.bootstrapJsUrl  = bootstrapJsUrl ; return this; }
    public WystriConfiguration setBeanLookup     (IBeanLookup beanLookup) { this.beanLookup      = beanLookup     ; return this; }
    //@formatter:on
}
