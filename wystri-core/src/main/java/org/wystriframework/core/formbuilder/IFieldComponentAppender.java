package org.wystriframework.core.formbuilder;

import java.io.Serializable;

import org.wystriframework.core.definition.IFieldView;

public interface IFieldComponentAppender<T> extends Serializable {

    IFieldView<T> append(FieldComponentContext<T> ctx);
}
