package org.wystriframework.core.util;

import javax.inject.Inject;

public class NewInstanceBeanLookup implements IBeanLookup {

    @Override
    public <T> T byType(Class<T> type) {
        return ReflectionUtils.newInstance(type);
    }

    @Override
    public <T> T inject(T bean) {
        ReflectionUtils.allDeclaredFields(bean.getClass())
            .filter(field -> field.isAnnotationPresent(Inject.class))
            .forEach(field -> {
                try {
                    field.set(bean, byType(field.getType()));
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    throw new RuntimeException(ex.getMessage(), ex);
                }
            });
        return bean;
    }
}
