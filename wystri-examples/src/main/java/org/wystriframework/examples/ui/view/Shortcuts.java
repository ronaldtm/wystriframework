package org.wystriframework.examples.ui.view;

import org.apache.wicket.Component;
import org.wystriframework.core.wicket.component.jquery.JQuery;
import org.wystriframework.core.wicket.util.IBehaviorShortcutsMixin;
import org.wystriframework.core.wicket.util.IFormatShortcutsMixin;
import org.wystriframework.core.wicket.util.ILambdaShortcutsMixin;
import org.wystriframework.core.wicket.util.IModelShortcutsMixin;
import org.wystriframework.core.wicket.util.IValidatorShortcutsMixin;

public abstract class Shortcuts {
    private Shortcuts() {}

    public static final IBehaviorShortcutsMixin  $b = IBehaviorShortcutsMixin.$b;
    public static final ILambdaShortcutsMixin    $L = ILambdaShortcutsMixin.$L;
    public static final IModelShortcutsMixin     $m = IModelShortcutsMixin.$m;
    public static final IFormatShortcutsMixin    $f = IFormatShortcutsMixin.$f;
    public static final IValidatorShortcutsMixin $v = IValidatorShortcutsMixin.$v;

    public static JQuery $(Component first, Component... other) {
        return JQuery.$(first, other);
    }

}
