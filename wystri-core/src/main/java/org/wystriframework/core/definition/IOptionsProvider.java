package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public interface IOptionsProvider<E, F> extends Serializable {

    List<? extends F> getOptions(IRecord<E> record);

//    String objectToId(F object);
//
//    F idToObject(String id, List<? extends F> options);
//
//    String objectToDisplay(F object, List<? extends F> options);

    default String objectToId(F object) {
        return String.valueOf(object);
    }

    default F idToObject(String id, List<? extends F> options) {
        return options.stream()
            .filter(it -> Objects.equals(objectToId(it), id))
            .findFirst()
            .orElse(null);
    }

    default String objectToDisplay(F object, List<? extends F> options) {
        return objectToId(object);
    }

}
