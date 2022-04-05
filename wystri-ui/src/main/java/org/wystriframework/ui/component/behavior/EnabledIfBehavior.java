package org.wystriframework.ui.component.behavior;

import org.apache.wicket.Component;
import org.danekja.java.util.function.serializable.SerializablePredicate;

@SuppressWarnings("serial")
public class EnabledIfBehavior extends ChainableBooleanConfigureBehavior<EnabledIfBehavior> {
    public EnabledIfBehavior(SerializablePredicate<Component> predicate) {
        super(predicate);
    }
    @Override
    protected void action(Component component, boolean state) {
        component.setEnabled(state);
    }
}