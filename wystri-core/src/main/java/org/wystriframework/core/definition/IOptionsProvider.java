package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

public interface IOptionsProvider<T> extends Serializable {

    List<? extends T> getOptions(IRecord record);

    String objectToId(T object);

    T idToObject(String id, List<? extends T> options);

    String objectToDisplay(T object, List<? extends T> options);

}
