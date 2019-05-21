package org.wystriframework.core.util;

public interface IBeanLookup {

    <T> T byType(Class<T> type);
}
