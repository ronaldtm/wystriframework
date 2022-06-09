package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFieldView<E, T> extends Serializable {

    IField<E, T> getField();
    
    T getValue();
    void setValue(T value);
    
    void saveSnapshotValue();
    T getSnapshotValue();

    void setRequired(boolean required);
    void setVisible(boolean visible);
    void setEnabled(boolean enabled);
    void error(String msg);
    void info(String msg);
    void markDirty();
    void cleanUp();

    default String getName() {
        return getField().getName();
    }

    default Class<? extends T> getType() {
        return getField().getType();
    }
}
