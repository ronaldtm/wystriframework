package org.wystriframework.core.wicket;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.IInitializer;
import org.apache.wicket.ISessionListener;
import org.apache.wicket.Session;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.wystriframework.core.filemanager.ITempFileManager;
import org.wystriframework.core.filemanager.SessionScopedTempFileManager;
import org.wystriframework.core.util.IBeanLookup;
import org.wystriframework.core.util.NewInstanceBeanLookup;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class WystriConfiguration {

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

    public <T> SerializableFunction<String, T> getConverter(Class<T> type) {
        return new ConverterImpl<>(type);
    }

    public String localizedString(String key) {
        return Application.get().getResourceSettings().getLocalizer().getString(key, null, key);
    }
    public String localizedString(String key, IModel<? extends Map<String, Object>> model) {
        String templateString = Application.get().getResourceSettings().getLocalizer().getString(key, null, model, key);
        Template template = Mustache.compiler()
            .emptyStringIsFalse(true)
            .defaultValue("[]")
            .compile(templateString);
        return template.execute(model.getObject());
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

    private static final class ConverterImpl<T> implements SerializableFunction<String, T> {
        private final Class<T> type;
        protected ConverterImpl(Class<T> type) {
            this.type = type;
        }
        @Override
        public T apply(String s) {
            IConverterLocator converterLocator = Application.get().getConverterLocator();
            IConverter<T> converter = converterLocator.getConverter(type);
            Locale locale = Session.get().getLocale();
            return converter.convertToObject(s, locale);
        }
    }

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
