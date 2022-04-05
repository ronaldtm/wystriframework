package org.wystriframework.ui.component.model;

import org.apache.wicket.model.IObjectClassAwareModel;

@SuppressWarnings("serial")
public final class NullModel<T> implements IObjectClassAwareModel<T> {
    private final Class<T> type;
    public NullModel(Class<T> type) {
        this.type = type;
    }
    @Override
    public T getObject() {
        return null;
    }
    @Override
    public Class<T> getObjectClass() {
        return type;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NullModel<?> other = (NullModel<?>) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}