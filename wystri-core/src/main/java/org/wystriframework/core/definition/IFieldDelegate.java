package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFieldDelegate<E, F> extends Serializable {

    void onAfterProcessed(IFieldView<E, F> view, IRecord<E> record);
}
