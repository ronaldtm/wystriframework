package org.wystriframework.core.wicket;

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxRequestTarget.IJavaScriptResponse;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.visit.Visits;
import org.wystriframework.core.definition.IConverter;
import org.wystriframework.core.filemanager.ITempFileManager;
import org.wystriframework.core.filemanager.SessionScopedTempFileManager;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

public class Wystri {

    private static final MetaDataKey<Wystri> WYSTRI_KEY    = new MetaDataKey<Wystri>() {};

    private final WystriConfiguration        configuration = new WystriConfiguration();

    private Wystri(WebApplication app) {
        new ListenerImpl().init(app);
    }

    public static Wystri get() {
        return Application.get().getMetaData(WYSTRI_KEY);
    }

    public static void init(Application application) {
        if (application.getMetaData(WYSTRI_KEY) == null) {
            application.setMetaData(WYSTRI_KEY, new Wystri((WebApplication) application));
        }
    }

    public WystriConfiguration getConfiguration() {
        return configuration;
    }

    public ITempFileManager getTempFileManager() {
        return SessionScopedTempFileManager.get(Session.get());
    }

    public <T> IConverter<T> getConverter(Class<T> type) {
        return new ConverterImpl<>(type);
    }

    public String localizedString(String key) {
        return (Application.exists())
            ? Application.get().getResourceSettings().getLocalizer().getString(key, null, key)
            : key;
    }

    public String localizedString(String key, IModel<? extends Map<String, Object>> model) {
        String templateString = Application.get().getResourceSettings().getLocalizer().getString(key, null, model, key);
        Template template = Mustache.compiler()
            .emptyStringIsFalse(true)
            .defaultValue("[]")
            .compile(templateString);
        return template.execute(model.getObject());
    }

    private static class ListenerImpl implements AjaxRequestTarget.IListener, IRequestCycleListener {

        private static final MetaDataKey<ListenerImpl> KEY = new MetaDataKey<ListenerImpl>() {};

        private ListenerImpl() {}

        void init(WebApplication application) {
            if (application.getMetaData(KEY) == null) {
                application.setMetaData(KEY, this);
                application.getRequestCycleListeners().add(this);
                application.getAjaxRequestTargetListeners().add(this);
            } else {
                throw new IllegalArgumentException();
            }
        }

        // IInitializer
        @Override
        public void onBeforeRespond(Map<String, Component> map, AjaxRequestTarget target) {}

        @Override
        public void onAfterRespond(Map<String, Component> map, IJavaScriptResponse response) {}

        // IRequestCycleListener
        @Override
        public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
            if (handler instanceof IPageRequestHandler) {
                IRequestablePage page = ((IPageRequestHandler) handler).getPage();
                if (page instanceof Page)
                    Visits.visitChildren((Page) page,
                        (c, v) -> WystriWicketUtils.getFieldView(c).saveSnapshotValue(),
                        Utils.filter(Component.class, WystriWicketUtils::hasFieldView));
            }
        }
    }

    private static final class ConverterImpl<T> implements IConverter<T> {
        private final Class<T> type;
        protected ConverterImpl(Class<T> type) {
            this.type = type;
        }
        @Override
        public T stringToObject(String s) {
            org.apache.wicket.IConverterLocator converterLocator = Application.get().getConverterLocator();
            org.apache.wicket.util.convert.IConverter<T> converter = converterLocator.getConverter(type);
            Locale locale = Session.get().getLocale();
            return converter.convertToObject(s, locale);
        }
        @Override
        public String objectToString(T o) {
            org.apache.wicket.IConverterLocator converterLocator = Application.get().getConverterLocator();
            org.apache.wicket.util.convert.IConverter<T> converter = converterLocator.getConverter(type);
            Locale locale = Session.get().getLocale();
            return converter.convertToString(o, locale);
        }
    }
}
