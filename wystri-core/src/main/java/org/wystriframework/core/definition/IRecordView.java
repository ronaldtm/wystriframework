package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

public interface IRecordView<E> extends Serializable {

    IRecord<E> getRecord();
    <F> Stream<IFieldView<E, F>> fields();

    @SuppressWarnings("unchecked")
    default <F> IFieldView<E, F> field(String name) {
        return (IFieldView<E, F>) fields()
            .filter(it -> Objects.equals(name, it.getName()))
            .findFirst()
            .get();
    }

}
