package org.wystriframework.core.wicket.bootstrap;

import java.io.Serializable;

import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.util.convert.IConverter;

public class BSFormLayoutConfig implements Serializable {

    private BSSize componentsSize;

    public BSFormLayoutConfig(BSSize componentsSize) {
        this.componentsSize = componentsSize;
    }

    public BSSize getComponentsSize() {
        return componentsSize;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String format(Object value) {
        IConverter converter = Application.get().getConverterLocator().getConverter(value.getClass());
        return converter.convertToString(value, Session.get().getLocale());
    }
}