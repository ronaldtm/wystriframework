package org.wystriframework.form.formbuilder;

import org.apache.wicket.model.IModel;

public interface ISnapshotModel<T> extends IModel<T> {

    T getLastSnapshot();
}
