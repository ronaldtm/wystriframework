package org.wystriframework.core.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.wystriframework.core.definition.IFieldView;

@SuppressWarnings("serial")
public class WystriWicketUtils {

    private static final MetaDataKey<IFieldView<?, ?>> FIELD_VIEW_KEY = new MetaDataKey<IFieldView<?, ?>>() {};

    public static void setFieldView(Component comp, IFieldView<?, ?> fieldView) {
        comp.setMetaData(FIELD_VIEW_KEY, fieldView);
    }
    public static boolean hasFieldView(Component comp) {
        return comp.getMetaData(FIELD_VIEW_KEY) != null;
    }
    public static IFieldView<?, ?> getFieldView(Component comp) {
        return comp.getMetaData(FIELD_VIEW_KEY);
    }
}
