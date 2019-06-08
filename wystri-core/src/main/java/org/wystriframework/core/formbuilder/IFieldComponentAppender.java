package org.wystriframework.core.formbuilder;

import java.io.Serializable;

import org.wystriframework.core.definition.IFieldView;

public interface IFieldComponentAppender<T> extends Serializable {

    <E> IFieldView<E, T> append(FieldComponentContext<E , T> ctx);
}
