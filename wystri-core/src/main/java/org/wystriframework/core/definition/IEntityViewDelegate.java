package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IEntityViewDelegate<T> extends Serializable {

    default void onCreation(IRecordView view, T object) {}
    default void onBeforeRespond(IRecordView view, T object) {}
}
