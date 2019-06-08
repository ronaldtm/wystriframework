package org.wystriframework.examples.ui;

import javax.servlet.ServletContext;

import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.wystriframework.core.util.IBeanLookup;

public class SpringBeanLookup implements IBeanLookup {
    private final WebApplication application;
    public SpringBeanLookup(WebApplication application) {
        this.application = application;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> T byType(Class<T> type) {
        final ServletContext sc = application.getServletContext();
        final WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
        if (ac.getBeanNamesForType(type).length > 0)
            return ac.getBean(type);
        else {
            final AutowireCapableBeanFactory acbf = ac.getAutowireCapableBeanFactory();
            return (T) acbf.autowire(type, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        }
    }

    @Override
    public <T> T inject(T bean) {
        final ServletContext sc = application.getServletContext();
        final WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
        ac.getAutowireCapableBeanFactory().autowireBean(bean);
        return bean;
    }
}