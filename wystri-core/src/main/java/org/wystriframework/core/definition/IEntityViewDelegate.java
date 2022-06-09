package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IEntityViewDelegate<E, F> extends Serializable {

    default void onCreation(IRecordView<E> view, F object) {}
    default void onBeforeRespond(IRecordView<E> view, F object) {}
}
