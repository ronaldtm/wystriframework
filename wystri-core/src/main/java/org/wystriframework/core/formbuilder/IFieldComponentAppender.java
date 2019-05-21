package org.wystriframework.core.formbuilder;

import java.io.Serializable;

import org.wystriframework.core.definition.IField;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;
import org.wystriframework.core.wicket.bootstrap.IBSFormGroupLayout;

public interface IFieldComponentAppender<T> extends Serializable {

    IFieldView<T> append(final IBSFormGroupLayout layout, RecordModel<? extends IRecord> record, IField<T> field);
}
