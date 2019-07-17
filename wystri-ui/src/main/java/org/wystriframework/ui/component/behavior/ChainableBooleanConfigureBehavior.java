package org.wystriframework.ui.component.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.danekja.java.util.function.serializable.SerializableBiConsumer;
import org.danekja.java.util.function.serializable.SerializablePredicate;

abstract class ChainableBooleanConfigureBehavior<B extends ChainableBooleanConfigureBehavior<B>> extends Behavior {
    private final SerializablePredicate<Component>     predicate;
    private SerializableBiConsumer<Component, Boolean> after = (c, b) -> {};
    public ChainableBooleanConfigureBehavior(SerializablePredicate<Component> predicate) {
        this.predicate = predicate;
    }
    protected abstract void action(Component component, boolean state);
    @Override
    public void onConfigure(Component component) {
        boolean state = predicate.test(component);
        action(component, state);
        after.accept(component, state);
    }
    @SuppressWarnings("unchecked")
    public B andThen(SerializableBiConsumer<Component, Boolean> after) {
        this.after = this.after.andThen(after);
        return (B) this;
    }
}