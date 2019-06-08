package org.wystriframework.core.definition;

import java.io.Serializable;
import java.util.stream.Stream;

public interface IEntity<E> extends Serializable {

    String getName();
    Stream<? extends IField<E, ?>> fields();
    IFieldLayout<E> getLayout();

}
