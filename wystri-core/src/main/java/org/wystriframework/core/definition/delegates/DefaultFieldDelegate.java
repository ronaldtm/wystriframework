package org.wystriframework.core.definition.delegates;

import org.wystriframework.core.definition.IFieldDelegate;
import org.wystriframework.core.definition.IFieldView;
import org.wystriframework.core.definition.IRecord;

public class DefaultFieldDelegate implements IFieldDelegate<Object> {

    @Override
    public void onAfterProcessed(IFieldView<Object> view, IRecord record) {}

}
