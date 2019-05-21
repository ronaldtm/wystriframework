package org.wystriframework.core.definition;

import java.io.Serializable;

public interface IFieldDelegate<T> extends Serializable {

    void onAfterProcessed(IFieldView<T> view, IRecord record);
}
