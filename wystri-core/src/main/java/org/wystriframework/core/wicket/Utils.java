package org.wystriframework.core.wicket;

import org.apache.wicket.util.visit.IVisitFilter;
import org.danekja.java.util.function.serializable.SerializablePredicate;

abstract class Utils {

    static <C> IVisitFilter filter(Class<C> type, SerializablePredicate<C> visitObject) {
        return filter(type, visitObject, it -> true);
    }

    @SuppressWarnings("unchecked")
    static <C> IVisitFilter filter(Class<C> type, SerializablePredicate<C> visitObject, SerializablePredicate<C> visitChildren) {
        return new IVisitFilter() {
            @Override
            public boolean visitObject(Object object) {
                return type.isAssignableFrom(object.getClass()) && visitObject.test((C) object);
            }
            @Override
            public boolean visitChildren(Object object) {
                return type.isAssignableFrom(object.getClass()) && visitChildren.test((C) object);
            }
        };
    }
}
