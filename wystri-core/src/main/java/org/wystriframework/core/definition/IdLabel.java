package org.wystriframework.core.definition;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class IdLabel<ID extends Serializable, T> implements Serializable {
    private final ID       id;
    private final String   label;
    private final Class<T> type;

    public IdLabel(ID id, String label, Class<T> type) {
        this.id = id;
        this.label = label;
        this.type = type;
    }

    public ID getId() {
        return id;
    }
    public String getLabel() {
        return label;
    }
    public Class<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IdLabel other = (IdLabel) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
}
