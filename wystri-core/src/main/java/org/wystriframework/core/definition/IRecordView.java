package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

public interface IRecordView extends Serializable {

    IRecord getRecord();
    <F> Stream<IFieldView<F>> fields();

    @SuppressWarnings("unchecked")
    default <F> IFieldView<F> field(String name) {
        return (IFieldView<F>) fields()
            .filter(it -> Objects.equals(name, it.getName()))
            .findFirst()
            .get();
    }

}
