package org.wystriframework.core.definition.delegates;

import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;

@SuppressWarnings("serial")
public class DefaultFieldDelegate implements IFieldDelegate<Object, Object> {

    @Override
    public void onAfterProcessed(IFieldView<Object, Object> view, IRecord<Object> record) {}

}
