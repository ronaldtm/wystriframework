package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;

public interface IOptionsProvider<E, F> extends Serializable {

    List<? extends F> getOptions(IRecord<E> record);

    String objectToId(F object);

    F idToObject(String id, List<? extends F> options);

    String objectToDisplay(F object, List<? extends F> options);

}
