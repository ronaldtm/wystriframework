package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFieldView<T> extends Serializable {

    IField<T> getField();
    
    T getValue();
    void setValue(T value);
    
    void setRequired(boolean required);
    void setVisible(boolean visible);
    void setEnabled(boolean enabled);
    void error(String msg);
    void info(String msg);
    void markDirty();

    default String getName() {
        return getField().getName();
    }

    default Class<? extends T> getType() {
        return getField().getType();
    }
}
