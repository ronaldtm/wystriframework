package org.wystriframework.form.formbuilder;

import java.io.Serializable;

import org.wystriframework.core.definition.IFieldView;

public interface IFieldComponentAppender<T> extends Serializable {

    <E> IFieldView<E, T> append(FieldComponentContext<E , T> ctx);
}
