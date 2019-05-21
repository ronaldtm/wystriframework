package org.wystriframework.core.util;

public class NewInstanceBeanLookup implements IBeanLookup {

    @Override
    public <T> T byType(Class<T> type) {
        return ReflectionUtils.newInstance(type);
    }
}
